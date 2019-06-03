package ru.snek;

public class Printer {
    public static void print(Object obj) { System.out.print(obj); }
    public static void println(Object obj) { System.out.println(obj); }
    public static void errprint(Object obj) { System.err.print(obj); }
    public static void errprintln(Object obj) { System.err.println(obj); }
    public static void log(Object obj) { System.err.println("LOG: " + obj); }
    public static void clearLine() { System.out.print("\033[2K\r");}

}
