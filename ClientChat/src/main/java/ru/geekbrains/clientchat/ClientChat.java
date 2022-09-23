package ru.geekbrains.clientchat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.clientchat.controllers.AuthController;
import ru.geekbrains.clientchat.controllers.ClientController;
import ru.geekbrains.clientchat.dialogs.Dialogs;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class ClientChat extends Application {

    private static ClientChat INSTANCE;

    private final int AUTH_TIME = 120;
    private static Timer timer;
    private static TimerTask task;
    public static final String AUTH_TIMER_START = "start";
    public static final String AUTH_TIMER_STOP = "stop";
    private Stage chatStage;
    private Stage authStage;
    private static String login;

    private FXMLLoader chatWindowLoader;
    private FXMLLoader authLoader;

    private static final Logger logger = LogManager.getLogger(ClientChat.class);


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
//        chatStage.setScene(new Scene(root));
        Scene scene = new Scene(root);
//        root.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        scene.getStylesheets().add("css/style.css");
        chatStage.setScene(scene);
    }

    private void initAuthDialog() throws IOException {
        authLoader = new FXMLLoader();
        authLoader.setLocation(ClientChat.class.getResource("authDialog.fxml"));
        AnchorPane authDialogPanel = authLoader.load();

        authStage = new Stage();
        authStage.initOwner(chatStage);
        authStage.initStyle(StageStyle.TRANSPARENT);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setScene(new Scene(authDialogPanel));
    }

    public void authTimer(String command) {
        logger.info("auth start: " + new Date());
        if (timer != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("auth timer already set!! \"fixed with sweat and blood.\" ");
            }
            timer.cancel();
        } else {
            timer = new Timer("auth timer");
        }

        task = authTask();

        switch (command) {
            case AUTH_TIMER_START:
                timer.schedule(task, AUTH_TIME * 1000);
                logger.info("timer start");
                break;
            case AUTH_TIMER_STOP:
                timer.cancel();
                logger.info("timer stopped");
                break;
        }
    }

    private TimerTask authTask() {
        return new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("auth canceled: " + new Date());
                        Dialogs.AuthError.TIMEOUT.show();
                        logger.error("{}: {}", Dialogs.AuthError.TIMEOUT, "close connection");
                        getAuthController().close();
                        getAuthStage().close();
                        getChatStage().close();
                    }
                });
            }
        };
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

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        ClientChat.login = login;
    }
}