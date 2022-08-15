package ru.geekbrains.clientchat.dialogs;

import javafx.scene.control.Alert;
import ru.geekbrains.clientchat.ClientChat;

public class Dialogs {
    public enum AuthError {
        EMPTY_CREDENTIALS("Логин и пароль должны быть указаны"),
        INVALID_CREDENTIALS("Логин и пароль заданы некорректно"),
        TIMEOUT("Не авторизрвались во время"),
        NO_SUCH_USER("Такого пользователя нет");

        private static final String TITLE = "Ошибка аутентификации";
        private static final String TYPE = TITLE;

        private final String message;

        AuthError(String message) {
            this.message = message;
        }

        public void show() {
            showDialog(Alert.AlertType.ERROR, TITLE, TYPE, message);
        }
    }

    public enum NetworkError {
        CANT_SEND_MESSAGE_TO_SERVER("Не возможно отправить сообщение"),
        CANT_CONNECT_TO_SERVER("Не возможно подключиться к серверу"),
        CANT_CLOSE_CONNECT_TO_SERVER("Соединение с сервером не закрыто"),
        ERROR_NETWORK_COMMUNICATION("Ошибка сети"),
        FAILED_TO_GET_MESSAGE("Не удалось получить сообщение"),
        FAILED_TO_BIND_PORT("Порт занят");

        private static final String TITLE = "Сетевая ошибка";
        private static final String TYPE = "Ошибка передачи данных по сети";
        private final String message ;

        NetworkError(String message) {
            this.message = message;
        }

        public void show() {
            showDialog(Alert.AlertType.ERROR, TITLE, TYPE, message);
        }

    }

    private static void showDialog(Alert.AlertType dialogType, String title, String type, String message) {
        Alert alert = new Alert(dialogType);
        alert.initOwner(ClientChat.getInstance().getChatStage());
        alert.setTitle(title);
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
