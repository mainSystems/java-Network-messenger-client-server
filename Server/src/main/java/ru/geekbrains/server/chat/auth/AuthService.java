package ru.geekbrains.server.chat.auth;

import java.sql.SQLException;

import ru.geekbrains.commands.SqlCommandType;
import ru.geekbrains.server.chat.SqliteHandler;

public class AuthService {
    private static String resultAuth = null;

    public String authUser(String login, String password) {
        try {
            SqliteHandler.connect();
            System.out.println("Connection to SQLite has been established.");
            resultAuth = SqliteHandler.sqlTask(SqlCommandType.SELECT_USERNAME_AUTH, login, password);
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

        return resultAuth;
    }
}
