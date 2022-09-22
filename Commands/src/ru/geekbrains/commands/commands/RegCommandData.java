package ru.geekbrains.commands.commands;

import java.io.Serializable;

public class RegCommandData implements Serializable {
    private final String login;
    private final String password;

    public RegCommandData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
