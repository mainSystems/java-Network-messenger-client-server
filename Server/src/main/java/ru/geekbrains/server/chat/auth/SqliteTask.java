package ru.geekbrains.server.chat.auth;


import ru.geekbrains.commands.SqlCommandType;

import java.sql.*;

public class SqliteTask {
    private static final String DB = "auth";
    private static final String sqliteUrl = "jdbc:sqlite:C:\\java\\javaGB\\Server\\" + DB + ".db";
    private static Connection connection = null;
    private static Statement statement = null;


    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(sqliteUrl);
        statement = connection.createStatement();
    }

    //    public static String sqlTask(SqlCommandType command, String login, String password) throws SQLException {
    public static String sqlTask(SqlCommandType command, String... arg) throws SQLException {
        switch (command) {
            case SELECT:
                ResultSet rs = statement.executeQuery(String.format("Select * from " + DB + " where login = '%s' and password = '%s'", arg[0], arg[1]));
                return (rs.getString("username"));
            case UPDATE:
                statement.executeUpdate(String.format("update " + DB + " set username = '%s' where login = '%s'", arg[1], arg[0]));
                return "update done";
            case INSERT:
                statement.executeQuery(String.format("insert into " + DB + " (login, password, username) values ('%s','%s','%s'))", arg[0], arg[1], arg[0]));
                return "done";
            default:
                return null;
        }
    }

    public static void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
