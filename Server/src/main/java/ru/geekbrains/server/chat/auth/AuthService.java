package ru.geekbrains.server.chat.auth;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.commands.SqlCommandType;
import ru.geekbrains.server.chat.SqliteHandler;

public class AuthService {
    private static String resultAuth = null;
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    public String authUser(String login, String password) {
        try {
            SqliteHandler.connect();
            logger.info("Connection to SQLite has been established.");
            resultAuth = SqliteHandler.sqlTask(SqlCommandType.SELECT_USERNAME_AUTH, login, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            logger.error("Connection to SQLite has NOT been established.");
        } finally {
            try {
                SqliteHandler.disconnect();
                logger.info("Connection to SQLite has been closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultAuth;
    }
}
