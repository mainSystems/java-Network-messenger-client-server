package ru.geekbrains.commands.commands;

import java.io.Serializable;

public class authTimerCommandData implements Serializable {
    private final String controlTimer;
    private final int authTime;

    public authTimerCommandData(String controlTimer, int authTime) {
        this.controlTimer = controlTimer;
        this.authTime = authTime;
    }

    public String getControlTimer() {
        return controlTimer;
    }

    public int getAuthTime() {
        return authTime;
    }
}
