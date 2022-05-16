package ru.geekbrains.clientchat;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.geekbrains.clientchat.controllers.AuthController;
import ru.geekbrains.clientchat.controllers.ClientController;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static ru.geekbrains.clientchat.Network.SERVER_ADDR;


public class ClientChat extends Application {

    private final int AUTH_TIME = 120;
    private Stage chatStage;
    private Stage authStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.chatStage = primaryStage;

        ClientController controller = createChatDialog(primaryStage);
        connectToServer(controller);

        createAuthDialog(primaryStage);

        controller.initMessageHandler();

    }

    private ClientController createChatDialog(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ClientChat.class.getResource("chat-template.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        this.chatStage.setTitle("Chat!");
        this.chatStage.setScene(scene);

        ClientController controller = fxmlLoader.getController();
        controller.userList.getItems().addAll("user1", "user2");

        primaryStage.show();

        return controller;
    }

    private void createAuthDialog(Stage primaryStage) throws IOException {
        FXMLLoader authLoader = new FXMLLoader();
        authLoader.setLocation(ClientChat.class.getResource("authDialog.fxml"));
        AnchorPane authDialogPanel = authLoader.load();

        authStage = new Stage();
        authStage.initOwner(primaryStage);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setScene(new Scene(authDialogPanel));
        AuthController authController = authLoader.getController();
        authController.setClientChat(this);
        authController.initMessageHandler();
        authStage.showAndWait();
    }

    public void authTimer(String command) {
        System.out.println("auth start: " + new Date());
        Timer timer = new Timer("auth timer");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("auth canceled: " + new Date());
                System.out.println("We are not authorized /nclose connection");
                Network.getInstance().close();
                authStage.close();
                chatStage.close();
            }
        };

        switch (command) {
            case "start":
                timer.schedule(task, AUTH_TIME * 1000);
                System.out.println("We are start");
                break;
            case "stop":
                timer.cancel();
                System.out.println("we are stopped");
                break;
        }
    }

    private void connectToServer(ClientController clientController) {
        boolean resultConnectToServer = Network.getInstance().connect();
        if (!resultConnectToServer) {
            showErrorDialog(ErrorMessage.CANT_CONNECT_TO_SERVER);
            System.err.printf("%s: %s%n", ErrorMessage.CANT_CONNECT_TO_SERVER, SERVER_ADDR);
        }


        clientController.setApplication(this);

        chatStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Network.getInstance().close();
            }
        });
    }

    public void showErrorDialog(ErrorMessage errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(String.valueOf(errorMessage));
        alert.showAndWait();
    }

    public Stage getAuthStage() {
        return authStage;
    }

    public Stage getChatStage() {
        return chatStage;
    }
}