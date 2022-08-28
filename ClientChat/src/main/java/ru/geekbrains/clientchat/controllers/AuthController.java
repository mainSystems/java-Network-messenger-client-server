package ru.geekbrains.clientchat.controllers;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.geekbrains.clientchat.ClientChat;
import ru.geekbrains.clientchat.dialogs.Dialogs;
import ru.geekbrains.clientchat.model.Network;
import ru.geekbrains.clientchat.model.ReadMessageListener;
import ru.geekbrains.commands.Command;
import ru.geekbrains.commands.CommandType;
import ru.geekbrains.commands.commands.AuthOkCommandData;

import java.io.IOException;

public class AuthController {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button authButton;

    public ReadMessageListener readMessageListener;


    @FXML
    public void executeAuth() {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login == null || password == null || login.isBlank() || password.isBlank()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
            System.err.println(Dialogs.AuthError.EMPTY_CREDENTIALS);
            return;
        }

        if (!isConnectedToServer()) {
            Dialogs.NetworkError.CANT_CONNECT_TO_SERVER.show();
            System.err.println(Dialogs.NetworkError.CANT_CONNECT_TO_SERVER);
        }

        try {
            ClientChat.setLogin(login);
            Network.getInstance().sendAuthMessage(login, password);
        } catch (IOException e) {
            System.err.println(Dialogs.NetworkError.ERROR_NETWORK_COMMUNICATION);
        }

    }

    public void initMessageHandler() {
        ClientChat.getInstance().authTimer(ClientChat.AUTH_TIMER_START);

        readMessageListener = getNetwork().addReadMessageListener(new ReadMessageListener() {
            @Override
            public void processReceiveCommand(Command command) {
                if (command.getType() == CommandType.AUTH_OK) {
                    ClientChat.getInstance().authTimer(ClientChat.AUTH_TIMER_STOP);

                    AuthOkCommandData data = (AuthOkCommandData) command.getData();
                    String username = data.getUserName();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ClientChat.getInstance().switchToMainChatWindow(username);
                        }
                    });
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Dialogs.AuthError.INVALID_CREDENTIALS.show();
                        }
                    });
                }
            }
        });
    }

    public boolean isConnectedToServer() {
        Network network = Network.getInstance();
        return network.isConnected() || network.connect();
    }

    private Network getNetwork() {
        return Network.getInstance();
    }

    public void close() {
        getNetwork().removeReadMessageListener(readMessageListener);
    }
}