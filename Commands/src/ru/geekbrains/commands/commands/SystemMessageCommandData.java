package ru.geekbrains.commands.commands;

import java.io.Serializable;

public class SystemMessageCommandData implements Serializable {
    private final String systemMessage;

    public SystemMessageCommandData(String message) {
        this.systemMessage = message;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

}
