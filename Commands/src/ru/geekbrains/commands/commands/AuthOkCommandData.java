package ru.geekbrains.commands.commands;

import java.io.Serializable;

public class AuthOkCommandData implements Serializable {
    private final String username;

    public AuthOkCommandData(String username) {
        this.username = username;
    }

    public String getUserName() {
        return username;
    }
}
