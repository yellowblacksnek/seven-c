package ru.snek;

import java.io.Serializable;

public class Message<T extends Serializable> implements Serializable {
    public enum Type implements Serializable{
        CON, COM, NOTIF
    }
    public enum Status implements Serializable {
        NONE,
        OK,
        USER_EXIST,
        ERROR,
        NO_MAIL,
        USER_IN_SYSTEM,
        USER_NOT_FOUND,
        WRONG_PASSWORD,
        WRONG_TOKEN,
        EXPIRED_TOKEN
    }
    private Type type;
    private Status status;
    private String token;
    private String[] stringData;
    private T data;


    public Message(Type type, String[] stringData, T data) {
        this.type = type;
        this.stringData = stringData;
        this.data = data;
    }

    public Message(Type type, String[] stringData) {
        this(type, stringData, null);
    }

    public Message(Type type, String str) {
        this(type, new String[1], null);
        stringData[0] = str;
    }

    public void setType(Type type) {this.type = type;}
    public void setStatus(Status status) {this.status = status;}
    public void setToken(String token) {this.token = token;}
    public void setStringData(String[] stringData) {this.stringData = stringData;}
    public void setData(T data) {this.data = data;}

    public Type getType() {return type;}
    public Status getStatus() {return status;}
    public String getToken() {return token;}
    public T getData() { return data; }
    public String[] getStringData() { return stringData; }
}