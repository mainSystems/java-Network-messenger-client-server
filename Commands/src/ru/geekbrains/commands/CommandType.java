package ru.geekbrains.commands;

public enum CommandType {
    AUTH,
    PUBLIC_MESSAGE,
    PRIVATE_MESSAGE,
    CLIENT_MESSAGE,
    ERROR,
    AUTH_OK,
    AUTH_TIMER,
    AUTH_TIMER_START,
    AUTH_TIMER_STOP,
    UPDATE_USERS_LIST;
}
