package ru.geekbrains.server.chat.auth;

import java.sql.SQLException;

import ru.geekbrains.commands.SqlCommandType;

public class AuthService {
    private static String resultAuth = null;

    public String authUser(String login, String password) {
        try {
            SqliteTask.connect();
            System.out.println("Connection to SQLite has been established.");
            resultAuth = SqliteTask.sqlTask(SqlCommandType.SELECT, login, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection to SQLite has NOT been established.");
        } finally {
            try {
                SqliteTask.disconnect();
                System.out.println("Connection to SQLite has been closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultAuth;
    }
}
