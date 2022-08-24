package ru.geekbrains.server.chat;

import ru.geekbrains.commands.Command;
import ru.geekbrains.commands.CommandType;
import ru.geekbrains.commands.commands.AuthCommandData;
import ru.geekbrains.commands.commands.PrivateMessageCommandData;
import ru.geekbrains.commands.commands.PublicMessageCommandData;
import ru.geekbrains.commands.commands.SystemMessageCommandData;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    public static final String AUTH_COMMAND = "/auth";
    public static final String AUTH_OK_COMMAND = "/authOk";
    public static final String CHANGE = "/change";
    private MyServer server;
    private final Socket clientSocket;
    private ObjectInputStream inputSocket;
    private ObjectOutputStream outputSocket;
    private String username;
    private String login;


    public String getLogin() {
        return login;
    }

    public ClientHandler(MyServer server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public void handle() throws IOException {
        outputSocket = new ObjectOutputStream(clientSocket.getOutputStream());
        inputSocket = new ObjectInputStream(clientSocket.getInputStream());

        new Thread(() -> {
            try {
                authenticate();
                readMessages();
            } catch (IOException e) {
                System.err.println("Failed to read message from client");
                e.printStackTrace();
            } finally {
                try {
                    closeConnection();
                } catch (IOException e) {
                    System.err.println("Failed to cloe connection");
                }
            }
        }).start();
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
                    //this.username = username;
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
            System.err.println("Failed to read Command class");
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
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommandData data = (PrivateMessageCommandData) command.getData();
                    String receiver = data.getReceiver();
                    String privateMessage = data.getMessage();
                    server.sendPrivateMessage(this, receiver, privateMessage);
                    break;
                }
                case SYSTEM_MESSAGE: {
                    SystemMessageCommandData data = (SystemMessageCommandData) command.getData();
                    server.systemCommandMessage(login, data.getSystemMessage());
                    break;
                }
                case PUBLIC_MESSAGE:
                    PublicMessageCommandData data = (PublicMessageCommandData) command.getData();
                    processMessage(data.getMessage());
                    break;
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

    public String getUsername() {
        return username;
    }
}
