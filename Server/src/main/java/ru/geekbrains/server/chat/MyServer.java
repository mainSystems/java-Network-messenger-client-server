package ru.geekbrains.server.chat;

import ru.geekbrains.commands.Command;
import ru.geekbrains.commands.SqlCommandType;
import ru.geekbrains.server.chat.auth.AuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private AuthService authService;
    private final List<ClientHandler> clients = new ArrayList<>();

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Server started: %s%nWaiting connection...%n", serverSocket);
            authService = new AuthService();
            while (true) {
                waitAndProcessClientConnection(serverSocket);
            }
        } catch (IOException e) {
            System.err.println("Failed to bind port: " + port);
        }
    }

    private void waitAndProcessClientConnection(ServerSocket serverSocket) throws IOException {
        System.out.println("Waiting client connection...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket.toString());

        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.handle();
    }

    public synchronized boolean isUserNameBusy(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    protected synchronized void subscribe(ClientHandler clientHandler) throws IOException {
        clients.add(clientHandler);
        notifyUserListUpdated();
    }

    protected synchronized void unsubscribe(ClientHandler clientHandler) throws IOException {
        clients.remove(clientHandler);
        notifyUserListUpdated();
    }

    public AuthService getAuthService() {
        return authService;
    }

    protected synchronized void broadcastMessages(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                ServerFileHandler.appendToFile(sender.getLogin(), client.getUsername(), message);
            }

            if (client != sender) {
                ServerFileHandler.appendToFile(client.getLogin(), sender.getUsername(), message);
                client.sendCommand(Command.clientMessageCommand(sender.getUsername(), message));
            }
        }
    }

    public synchronized void sendPrivateMessage(ClientHandler sender, String receiver, String message) throws IOException {
        try {
            SqliteHandler.connect();
            String loginReceiver = SqliteHandler.sqlTask(SqlCommandType.SELECT_LOGIN, receiver);
            ServerFileHandler.appendToFile(sender.getLogin(), receiver, message);
            ServerFileHandler.appendToFile(loginReceiver, sender.getLogin(), message);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                SqliteHandler.disconnect();
                System.out.println("Connection to SQLite has been closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (ClientHandler client : clients) {
            if (client != sender && client.getUsername().equals(receiver)) {
                client.sendCommand(Command.clientMessageCommand(sender.getUsername(), message));
            }
        }
    }

    public synchronized void systemCommandMessage(String login, String message) throws IOException {
        try {
            SqliteHandler.connect();
            System.out.println("Connection to SQLite has been established.");
            String[] parts = message.split(" ");
            if (parts.length > 1) {
                String newUsername = parts[1];
                String resultSystemCommand = SqliteHandler.sqlTask(SqlCommandType.UPDATE, login, newUsername);
                System.out.println(resultSystemCommand);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection to SQLite has NOT been established.");
        } finally {
            try {
                SqliteHandler.disconnect();
                System.out.println("Connection to SQLite has been closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyUserListUpdated() throws IOException {
        List<String> users = new ArrayList<>();
        for (ClientHandler client : clients) {
            users.add(client.getUsername());
        }

        for (ClientHandler client : clients) {
            client.sendCommand(Command.updateUserListCommand(users));
        }
    }

}