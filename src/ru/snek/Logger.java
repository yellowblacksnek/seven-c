package ru.snek;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Stack;
import static ru.snek.Printer.*;

public class Logger {
    public static final boolean showAll = false;
    public static final boolean showIOExceptions = false;
    public static final boolean showExceptions = true;

    private static Stack<Exception> logs = new Stack<>();

    public static void addToLogs(Exception e) {
        logs.push(e);
    }
    public static void printLogs() {
        if(logs.empty()) {
            errprintln("Пусто");
            return;
        }
        for(Exception e : logs) {
            e.printStackTrace();
        }
        logs.clear();
    }

    public static void handleException(Exception exception) {
        try {
            addToLogs(exception);
            throw exception;
        } catch (SocketTimeoutException e) {
            errprintln("\r"+ "Время ожидания ответа истекло.");
        } catch (IOException e) {
            if(showAll || showIOExceptions) errprintln("\r"+ e.getMessage());
        } catch (Exception e) {
            if(showAll || showExceptions) errprintln("\r"+ e.getMessage());
        }
    }
}
