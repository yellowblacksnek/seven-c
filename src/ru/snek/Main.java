package ru.snek;

import java.io.File;
import java.io.IOException;

import static ru.snek.FileInteractor.*;
import static ru.snek.Printer.*;

public class Main {
    public static void main(String []args) {
        if(args.length < 1) {
            errprintln("Необходимо указать адрес и порт. (Или только порт)");
            System.exit(1);
        }
        String addr = null;
        int port = -1;
        if(args.length == 1) {
            port = Integer.valueOf(args[0]);
        }
        if(args.length > 1) {
            addr = args[0];
            port = Integer.valueOf(args[1]);
        }
        if(port < 0 || port > 65535) {
            errprintln("Неправильный порт.");
            System.exit(1);
        }
        Client client = new Client();
        client.start(addr, port);
    }
}