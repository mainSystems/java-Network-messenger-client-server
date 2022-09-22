package ru.geekbrains.server.chat.auth;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.commands.SqlCommandType;
import ru.geekbrains.server.chat.SqliteHandler;

public class RegService {
    private static String resultReg = null;
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    public String regUser(String login, String password) {
        try {
            SqliteHandler.connect();
            logger.info("Connection to SQLite has been established.");
            resultReg = SqliteHandler.sqlTask(SqlCommandType.INSERT, login, password);
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

        return resultReg;
    }
}
