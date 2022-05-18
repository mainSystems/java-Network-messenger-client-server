package ru.geekbrains.clientchat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.geekbrains.clientchat.controllers.AuthController;
import ru.geekbrains.clientchat.controllers.ClientController;
import ru.geekbrains.clientchat.dialogs.Dialogs;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ClientChat extends Application {

    private static ClientChat INSTANCE;

    private final int AUTH_TIME = 10;
    public static final String AUTH_TIMER_START = "start";
    public static final String AUTH_TIMER_STOP = "stop";
    private Stage chatStage;
    private Stage authStage;

    private FXMLLoader chatWindowLoader;
    private FXMLLoader authLoader;

    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.chatStage = primaryStage;

        initViews();
        getChatStage().show();
        getAuthStage().show();
        getAuthController().initMessageHandler();
    }

    private void initViews() throws IOException {
        initChatWindow();
        initAuthDialog();
    }

    private void initChatWindow() throws IOException {
        chatWindowLoader = new FXMLLoader();
        chatWindowLoader.setLocation(ClientChat.class.getResource("chat-template.fxml"));

        Parent root = chatWindowLoader.load();
        chatStage.setScene(new Scene(root));
        //getChatController().initializeMessageHandler();
    }

    private void initAuthDialog() throws IOException {
        authLoader = new FXMLLoader();
        authLoader.setLocation(ClientChat.class.getResource("authDialog.fxml"));
        AnchorPane authDialogPanel = authLoader.load();

        authStage = new Stage();
        authStage.initOwner(chatStage);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setScene(new Scene(authDialogPanel));
    }

    public void authTimer(String command) {
        System.out.println("auth start: " + new Date());
        Timer timer = new Timer("auth timer");
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.err.println("auth canceled: " + new Date());
                        Dialogs.AuthError.TIMEOUT.show();
                        System.err.println(Dialogs.AuthError.TIMEOUT + " /nclose connection");
                        authStage.close();
                        chatStage.close();
                    }
                });
            }
        };

        switch (command) {
            case AUTH_TIMER_START:
                timer.schedule(task, AUTH_TIME * 1000);
                System.out.println("We are start");
                break;
            case AUTH_TIMER_STOP:
                timer.cancel();
                System.out.println("we are stopped");
                break;
        }
    }

    public void authSchedule(String command) {
        System.out.println("auth start: " + new Date());

        switch (command) {
            case AUTH_TIMER_START:
                System.out.println("We are start");
                executor.schedule(() -> {
                    System.err.println("auth canceled: " + new Date());
                    Dialogs.AuthError.TIMEOUT.show();
                    System.err.println(Dialogs.AuthError.TIMEOUT + " /nclose connection");
                    authStage.close();
                    chatStage.close();
                }, AUTH_TIME, TimeUnit.SECONDS);
                break;
            case AUTH_TIMER_STOP:
                executor.shutdown();
                System.out.println("we are stopped");
                break;
        }
    }

    public void switchToMainChatWindow(String username) {
        getChatStage().setTitle(username);
        getAuthController().close();
        getAuthStage().close();
        getChatController().initMessageHandler();
    }

    @Override
    public void init() throws Exception {
        INSTANCE = this;
    }

    public Stage getAuthStage() {
        return authStage;
    }

    public Stage getChatStage() {
        return chatStage;
    }

    public static ClientChat getInstance() {
        return INSTANCE;
    }

    public ClientController getChatController() {
        return chatWindowLoader.getController();
    }

    public AuthController getAuthController() {
        return authLoader.getController();
    }
}