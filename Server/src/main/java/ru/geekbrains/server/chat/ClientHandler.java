package ru.geekbrains.server.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.commands.Command;
import ru.geekbrains.commands.CommandType;
import ru.geekbrains.commands.commands.AuthCommandData;
import ru.geekbrains.commands.commands.PrivateMessageCommandData;
import ru.geekbrains.commands.commands.PublicMessageCommandData;
import ru.geekbrains.commands.commands.SystemMessageCommandData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    private final MyServer server;
    private final Socket clientSocket;
    private ObjectInputStream inputSocket;
    private ObjectOutputStream outputSocket;
    private String username;
    private String login;
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);


    public ClientHandler(MyServer server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public String getLogin() {
        return login;
    }

    public String getUsername() {
        return username;
    }

    public void handle() throws IOException {
        outputSocket = new ObjectOutputStream(clientSocket.getOutputStream());
        inputSocket = new ObjectInputStream(clientSocket.getInputStream());

        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    authenticate();
                    readMessages();
                } catch (IOException e) {
                    logger.error("Failed to read message from client");
                    e.printStackTrace();
                } finally {
                    try {
                        closeConnection();
                        pool.shutdownNow();
                    } catch (IOException e) {
                        logger.error("Failed to close connection");
                    }
                }
            }
        });
    }

    private void authenticate() throws IOException {
        while (true) {
            Command command = readCommand();

            if (command == null) {
                continue;
            }

            if (command.getType() == CommandType.AUTH) {
                AuthCommandData data = (AuthCommandData) command.getData();
                login = data.getLogin();
                String password = data.getPassword();
                username = this.server.getAuthService().authUser(login, password);
                if (username == null) {
                    sendCommand(Command.errorCommand("Некорректные имя пользователя или пароль"));
                } else if (server.isUserNameBusy(username)) {
                    sendCommand(Command.errorCommand("Такой пользователь уже существует"));
                } else {
                    sendCommand(Command.authOkCommand(username));
                    server.subscribe(this);
                    return;
                }
            }
        }
    }

    private Command readCommand() throws IOException {
        Command command = null;

        try {
            command = (Command) inputSocket.readObject();
        } catch (ClassNotFoundException e) {
            logger.error("Failed to read Command class");
            e.printStackTrace();
        }

        return command;
    }

    public void sendCommand(Command command) throws IOException {
        outputSocket.writeObject(command);
    }

    private void readMessages() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case PRIVATE_MESSAGE -> {
                    PrivateMessageCommandData data = (PrivateMessageCommandData) command.getData();
                    String receiver = data.getReceiver();
                    String privateMessage = data.getMessage();
                    server.sendPrivateMessage(this, receiver, privateMessage);
                }
                case SYSTEM_MESSAGE -> {
                    SystemMessageCommandData data = (SystemMessageCommandData) command.getData();
                    server.systemCommandMessage(login, data.getSystemMessage());
                }
                case PUBLIC_MESSAGE -> {
                    PublicMessageCommandData data = (PublicMessageCommandData) command.getData();
                    processMessage(data.getMessage());
                }
            }
        }
    }

    private void processMessage(String message) throws IOException {
        this.server.broadcastMessages(message, this);
    }

    private void closeConnection() throws IOException {
        inputSocket.close();
        outputSocket.close();
        clientSocket.close();
        server.unsubscribe(this);
    }
}
