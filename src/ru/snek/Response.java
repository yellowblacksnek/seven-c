package ru.snek;

import ru.snek.Command.Commands;

import java.io.Serializable;

public class Response <T extends Serializable> implements Serializable{
    enum Status implements Serializable {
        OK, ERROR, WRONG_TOKEN, EXPIRED_TOKEN
//        USER_EXIST,
//        NO_MAIL,
//        USER_IN_SYSTEM,
//        USER_NOT_FOUND,
//        WRONG_PASSWORD,
//        WRONG_TOKEN,
//        EXPIRED_TOKEN
    }

    private boolean notification = false;
    private Status status = null;
    private Commands commandType = null;
    private T data;

    public Response() {}

    public Response(Status status, Commands commandType, T data) {
        this.status = status;
        this.commandType = commandType;
        this.data = data;
    }

    public Response(Commands commandType, T data) {
        this.status = Status.OK;
        this.commandType = commandType;
        this.data = data;
    }

    public Response(Status status, T data) {
        notification = true;
        this.status = status;
        this.data = data;
    }

    public boolean isNotification() {
        return notification;
    }

    public Response setNotification(boolean notification) {
        this.notification = notification;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Response setStatus(Status status) {
        this.status = status;
        return this;
    }


    public Commands getCommandType() {
        return commandType;
    }

    public Response setCommandType(Commands commandType) {
        this.commandType = commandType;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response setData(T data) {
        this.data = data;
        return this;
    }
}