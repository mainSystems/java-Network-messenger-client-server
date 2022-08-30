package ru.geekbrains.clientchat.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.geekbrains.clientchat.ChatFileHandler;
import ru.geekbrains.clientchat.ClientChat;
import ru.geekbrains.clientchat.dialogs.Dialogs;
import ru.geekbrains.clientchat.model.Network;
import ru.geekbrains.clientchat.model.ReadMessageListener;
import ru.geekbrains.commands.Command;
import ru.geekbrains.commands.CommandType;
import ru.geekbrains.commands.commands.ClientMessageCommandData;
import ru.geekbrains.commands.commands.UpdateUserListCommandData;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class ClientController {

    @FXML
    public TextField messageTextArea;
    @FXML
    public Button sendMessageButton;
    @FXML
    public TextArea chatTextArea;
    @FXML
    public ListView userList;

    private String recipient = null;


    public void sendMessage() {
        String sender = ClientChat.getLogin();
        String message = messageTextArea.getText();

        if (message.isEmpty()) {
            messageTextArea.clear();
            return;
        }

        if (!userList.getSelectionModel().isEmpty()) {
            recipient = userList.getSelectionModel().getSelectedItem().toString();
        }

        try {
            if (recipient != null) {
                Network.getInstance().sendPrivateMessage(recipient, message);
            } else {
                if (message.startsWith("/")) {
                    Network.getInstance().sendSystemMessage(message);
                } else {
                    Network.getInstance().sendMessage(message);
                }
            }

        } catch (IOException e) {
            Dialogs.NetworkError.CANT_SEND_MESSAGE_TO_SERVER.show();
        }
        appendMessageToChat(sender, message);
    }

    public void appendMessageToChat(String sender, String message) {
        chatTextArea.appendText(DateFormat.getDateInstance().format(new Date()) + ": ");

        if (sender != null) {
            chatTextArea.appendText(sender + ": ");
        }
        chatTextArea.appendText(message);
        chatTextArea.appendText(System.lineSeparator());
        chatTextArea.appendText(System.lineSeparator());
        messageTextArea.requestFocus();
        messageTextArea.clear();
    }

    public void initMessageHandler() {
        String login = ClientChat.getLogin();
        if (ChatFileHandler.checkFile(login)) {
            String messageHistory = ChatFileHandler.appendFromFile(login);
            chatTextArea.appendText("loading history...");
            chatTextArea.appendText(System.lineSeparator());
            chatTextArea.appendText(messageHistory);
            chatTextArea.appendText("that`s all");
            chatTextArea.appendText(System.lineSeparator());
        }

        Network.getInstance().addReadMessageListener(new ReadMessageListener() {
            @Override
            public void processReceiveCommand(Command command) {
                if (command.getType() == CommandType.CLIENT_MESSAGE) {
                    ClientMessageCommandData data = (ClientMessageCommandData) command.getData();
                    appendMessageToChat(data.getSender(), data.getMessage());
                } else if (command.getType() == CommandType.UPDATE_USERS_LIST) {
                    UpdateUserListCommandData data = (UpdateUserListCommandData) command.getData();
                    Platform.runLater(() -> {
                        userList.setItems(FXCollections.observableArrayList(data.getUsers()));
                    });
                }
            }
        });
    }
}