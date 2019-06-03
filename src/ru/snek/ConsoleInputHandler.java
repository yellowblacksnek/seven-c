package ru.snek;

import static ru.snek.Printer.println;

import ru.snek.Collection.Malefactor;
import ru.snek.Collection.MapWrapperUtils;
import ru.snek.Command.Commands;

public class ConsoleInputHandler {
    private boolean multiline;
    private String bufferred;
    private Command command;
    private boolean exit;
    private boolean authorise;

    public ConsoleInputHandler() {
        multiline = false;
        bufferred = "";
        command = null;
        authorise = false;
        exit = false;
    }

    public boolean process(String s) {
        String com = s;
        boolean wasMultilined = multiline;
        boolean correct = false;
        if (wasMultilined) {
            if (com.trim().equals("")) multiline = false;
            else multiline = isMultilined(bufferred + " " + com);
            correct = isCorrect(bufferred + " " + com);
        } else {
            if (com.trim().equals("")) return false;
            multiline = isMultilined(com);
            correct = isCorrect(com);
        }
        if (multiline) {
            if (bufferred.trim().equals("")) bufferred += com;
            else bufferred = bufferred + " " + com;
        }
        if (wasMultilined && !multiline) {
            com = bufferred + " " + com;
            bufferred = "";
        }
        if (!multiline) {
            if (!correct) println(wrongCommand(com));
            else {
                if (com.trim().equals("exit") || com.trim().equals("quit")) {
                    com = "quit";
                    exit = true;
                }
                if (com.trim().startsWith("register") || com.trim().startsWith("login")) {
                    authorise = true;
                } else authorise = false;
                command = wrapCommand(com);
                return true;
            }
        }
        return false;
    }

    public Command getCommand() {
        return command;
    }

    public boolean getExit() {
        return exit;
    }

    public boolean isAuthorise() { return authorise; }

    private boolean isMultilined(String s) {
        String temp = s.replaceAll("\\s+", " ");
        String[] splitted = temp.split(" ");
        switch (splitted[0]) {
            case "insert":
                if(splitted.length < 2) return false;
                if(!splitted[1].startsWith("\"")) return false;
                int x = temp.indexOf("\"",temp.indexOf("\"")+1);
                boolean keyDone = x > -1;
                if(keyDone) {
                    if(temp.length() > x + 1) {
                        if(temp.charAt(x+1)!=' ') return false;
                        if (temp.length() > x + 2) {
                            if (temp.charAt(x + 2) != '{') {
                                return false;
                            } else {
                                if (temp.indexOf("}", x + 3) > 0) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
            case "remove":
            case "remove_greater_key":
                return (Utils.countInStr(s, '\"') % 2 != 0);
        }
        return false;
    }

    private boolean isCorrect(String s) {
        String[] splitted = s.split(" ");
        Commands type = null;
        try {
            type = Commands.valueOf(splitted[0].toUpperCase());
        } catch (IllegalArgumentException e) { }
        if(type == null) return false;
        if(splitted.length == 1 && !type.isOneWord()) return false;

        else {
            switch (type) {
                case INSERT:
                    if(!splitted[1].startsWith("\"") ||
                            Utils.countInStr(s, '\"') % 2 != 0 ||
                            Utils.countInStr(s, '{') == 0 ||
                            Utils.countInStr(s, '}') == 0 ) return false;
                    String temp = s.replaceAll("\\s+", " ");
                    int x = temp.indexOf("\"",temp.indexOf("\"")+1);
                    boolean keyDone = x > -1;
                    if(keyDone) {
                        if(temp.length() > x+1) {
                            if(temp.charAt(x+1) != ' ') return false;
                            if (temp.length() > x + 2) {
                                if (temp.charAt(x + 2) != '{') return false;
                                if (temp.indexOf("}", temp.indexOf("{", x + 1) + 1) < 0) return false;
                            }
                        }
                    }
                    break;
                case REMOVE:
                case REMOVE_GREATER_KEY:
                    if(!splitted[1].startsWith("\"") ||
                            Utils.countInStr(s, '\"') % 2 != 0) return false;
                    break;
                case HELP:
                    if(splitted.length > 1 && !splitted[1].equals("insert")) return false;
                    break;
                case IMPORT:
                    break;
                case LOGIN:
                    if(splitted.length < 3) return false;
                    break;
                case REGISTER:
                    if(splitted.length < 4) return false;
                    break;
            }
        }
        return true;
    }

    private String wrongCommand(String s) {
        String[] splitted = s.split(" ");
        Commands type = null;
        try {
            type = Commands.valueOf(splitted[0]);
        } catch (IllegalArgumentException e) {}
        if(type == null) return "Нет такой команды!";
        String message = "Неверный формат команды!\n";
        switch (type) {
            case INSERT:
                message += "insert \"String key\" {element}";
                break;
            case REMOVE:
                message += "remove \"String key\"";
                break;
            case REMOVE_GREATER_KEY:
                message += "remove_greater_key \"String key\"";
                break;
            case IMPORT:
                message += "import path";
                break;
            case HELP:
                message += "help (insert)";
                break;
        }
        return message;
    }

    private Command wrapCommand(String com) {
        String[] splitted = com.split(" ");
        Command command = new Command(Commands.valueOf(splitted[0].toUpperCase()));
        if(splitted.length > 1) {
            String data = com.substring(com.indexOf(' '));
            int sec = data.indexOf("\"", data.indexOf("\"")+1);
            switch (command.getType()) {
                case INSERT:
                    String key = data.substring(2, sec);
                    String json = data.substring(sec+1);
                    String[] arr = new String[] {key, json};
                    command.setStrData(arr);
                    return command;
                case REMOVE:
                case REMOVE_GREATER_KEY:
                    command.setStrData(data.substring(2, sec));
                    return command;
            }
            String[] arr = new String[splitted.length-1];
            for(int i =0; i < splitted.length-1; ++i) {
                arr[i] = splitted[i+1];
            }
            command.setStrData(arr);
        }
        return command;
    }
}
