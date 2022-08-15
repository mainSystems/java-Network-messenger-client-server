package ru.geekbrains.clientchat.model;

import ru.geekbrains.commands.Command;

public interface ReadMessageListener {
    void processReceiveCommand(Command command);
}
