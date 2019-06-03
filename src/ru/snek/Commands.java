package ru.snek;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

//public class CommandList {
    //private static final String[] array = {"register", "login", "show", "clear", "save", "info", "help", "quit", "exit", "insert", "remove", "remove_greater_key", "test", "load", "import", "log"};
    //private static ArrayList<String> commands = new ArrayList<>(Arrays.asList(array));
    /*enum Commands implements Serializable {
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
    }*/

    /*public static boolean exists(String command) {
        return commands.contains(command);
    }

    public static boolean isOneWord(String command) {
        if(!exists(command)) return false;
        switch (command) {
            case "insert":
            case "remove":
            case "remove_greater_key":
            case "import":
            case "register":
            case "login":
                return false;
            default:
                return true;
        }
    }*/
//}
