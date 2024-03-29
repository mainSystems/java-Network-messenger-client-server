package ru.geekbrains.clientchat.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.clientchat.dialogs.Dialogs;
import ru.geekbrains.commands.Command;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Network {
    private List<ReadMessageListener> listeners = new CopyOnWriteArrayList<>();
    private static Network INSTANCE;
    public static final String SERVER_ADDR = "127.0.0.1";
    protected static final int SERVER_PORT = 8189;

    private Socket socket;

    private ObjectInputStream socketInput;
    private ObjectOutputStream socketOutput;
    private Thread readMessageProcess;

    private final String host;
    private final int port;
    private boolean connected = false;

    private static final Logger logger = LogManager.getLogger(Network.class);

    private Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Network() {
        this(SERVER_ADDR, SERVER_PORT);
    }

    public static Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();
        }
        return INSTANCE;
    }

    public boolean connect() {
        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);
            socketInput = new ObjectInputStream(socket.getInputStream());
            socketOutput = new ObjectOutputStream(socket.getOutputStream());
            readMessageProcess = startReadMessageProcess();
            connected = true;
        } catch (IOException e) {
            logger.error("Network module: {}: {}", Dialogs.NetworkError.CANT_CONNECT_TO_SERVER, SERVER_ADDR);
            e.printStackTrace();
        }
        return connected;
    }

    public void sendCommand(Command command) throws IOException {
        try {
            socketOutput.writeObject(command);
        } catch (IOException e) {
            logger.error("Не удалось отправить сообщение на сервер");
            e.printStackTrace();
            throw e;
        }
    }

    private Command readCommand() throws IOException {
        Command command = null;

        try {
            command = (Command) socketInput.readObject();
        } catch (ClassNotFoundException e) {
            logger.error("Failed to read Command class");
            e.printStackTrace();
        }

        return command;
    }

    public void sendMessage(String message) throws IOException {
        sendCommand(Command.publicMessageCommand(message));
    }

    public void sendPrivateMessage(String receiver, String message) throws IOException {
        sendCommand(Command.privateMessageCommand(receiver, message));
    }

    public void sendRegMessage(String login, String password) throws IOException {
        sendCommand(Command.regCommand(login, password));
    }

    public void sendAuthMessage(String login, String password) throws IOException {
        sendCommand(Command.authCommand(login, password));
    }

    public void sendSystemMessage(String message) throws IOException {
        sendCommand(Command.systemMessageCommand(message));
    }

    public Thread startReadMessageProcess() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (Thread.currentThread().isInterrupted()) {
                            return;
                        }

                        Command command = readCommand();

                        for (ReadMessageListener listener : listeners) {
                            listener.processReceiveCommand(command);
                        }

                    } catch (IOException e) {
                        logger.error(Dialogs.NetworkError.FAILED_TO_GET_MESSAGE);
                        e.printStackTrace();
                        close();
                        break;
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    public ReadMessageListener addReadMessageListener(ReadMessageListener listener) {
        this.listeners.add(listener);
        return listener;
    }

    public void removeReadMessageListener(ReadMessageListener listener) {
        this.listeners.remove(listener);
    }

    public void close() {
        try {
            connected = false;
//            socketInput.close();
//            socketOutput.close();
            socket.close();
            readMessageProcess.interrupt();
        } catch (IOException e) {
            logger.error(Dialogs.NetworkError.CANT_CLOSE_CONNECT_TO_SERVER);
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }
}