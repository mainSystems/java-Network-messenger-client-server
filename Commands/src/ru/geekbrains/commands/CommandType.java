package ru.geekbrains.commands;

public enum CommandType {
    REG,
    AUTH,
    SYSTEM_MESSAGE,
    PUBLIC_MESSAGE,
    PRIVATE_MESSAGE,
    CLIENT_MESSAGE,
    ERROR,
    AUTH_OK,
    UPDATE_USERS_LIST;
}
