package ru.snek;

import java.io.Serializable;

public class Command <T extends Serializable> implements Serializable {
    enum Commands implements Serializable {
        REGISTER, LOGIN, SHOW, CLEAR, SAVE, INFO, HELP, QUIT, INSERT, REMOVE, REMOVE_GREATER_KEY, TEST, LOAD, IMPORT, LOG;

        public boolean isOneWord() {
            switch (this) {
                case INSERT:
                case REMOVE:
                case REMOVE_GREATER_KEY:
                case IMPORT:
                case REGISTER:
                case LOGIN:
                    return false;
                default:
                    return true;
            }
        }
    }
    private Commands type = null;
    private String[] strData = null;
    private T data = null;
    private String token;

    public Command() {}
    public Command(Commands type) {
        this.type = type;
    }

    public Commands getType() {
        return type;
    }

    public Command setType(Commands type) {
        this.type = type;
        return this;
    }

    public String[] getStrData() {
        return strData;
    }

    public Command setStrData(String[] strData) {
        this.strData = strData;
        return this;
    }

    public Command setStrData(String strData) {
        this.strData = new String[1];
        this.strData[0] = strData;
        return this;
    }

    public T getData() {
        return data;
    }

    public Command setData(T data) {
        this.data = data;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Command setToken(String token) {
        this.token = token;
        return this;
    }
}
