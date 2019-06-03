package ru.snek;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static ru.snek.Logger.handleException;
import static ru.snek.Printer.log;

public class Utils {
    private static Scanner scan = new Scanner(System.in);

    public static String getConsoleInput() {
        String str = null;
        try {
            str = scan.nextLine();
        } catch(NoSuchElementException e) {
            System.exit(1);
        }
        return str;
    }

    public static int countInStr(String str, char ch) {
        int count = 0;
        int last = 0;
        while (str.indexOf(ch, last) > -1) {
            ++count;
            last = str.indexOf(ch, last) + 1;
        }
        return count;
    }

    public static int countInStr(StringBuilder strb, char ch) {
        return countInStr(strb.toString(), ch);
    }

    public static ArrayList<String> splitStrByIndex(String str, ArrayList<Integer> arr) {
        ArrayList<String> strArr = new ArrayList<>();
        if(arr.isEmpty()) return strArr;
        int last = -1;
        for(Integer index : arr) {
            strArr.add(str.substring(last+1, index).trim());
            last = index;
        }
        strArr.add(str.substring(last+1).trim());
        return strArr;
    }

    public static ArrayList<Integer> getExtCommas(String str) {
        ArrayList<Integer> commas = new ArrayList<>();
        int lastIndex = 0;
        while(str.indexOf(',',lastIndex) > -1) {
            int index = str.indexOf(',', lastIndex);
            String s = str.substring(0, index);
            if ((countInStr(s,'{') == countInStr(s,'}'))
                    && (countInStr(s,'[') == countInStr(s,']'))
                    && countInStr(s, '"') % 2 == 0) {
                commas.add(index);
            }
            lastIndex = index + 1;
        }
        return commas;
    }

    public static int getPercentage(long part, long total) {
        if(part == 0) return 0;
        if(total == 0) return 0;
        if(part > total) return 100;
        int percentage = (int)((100*part) / total);
        if (percentage > 100) percentage = 100;
        return percentage;
    }

    public static byte[] objectAsByteArray(Object obj) throws IOException {
        if(obj == null) return null;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }
    }

    public static Object objectFromByteArray(byte[] buf) throws IOException{
        try(ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            Object obj = ois.readObject();
            return obj;
        } catch(ClassNotFoundException e) { handleException(e);}
        return null;
    }

    public static byte[] containsMore(byte[] buf) throws IOException {
        try(ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            ois.readObject();
            int left = bais.available();
            if(left == 0) return null;
            byte[] extra = new byte[left];
            bais.read(extra);
            return extra;
        } catch(ClassNotFoundException e) { handleException(e);}
        return null;
    }

    /*public static byte[] getSizeArr(byte[] obj) throws IOException {
        Message message = new Message<>("size", obj.length);
        return objectAsByteArray(message);
    }

    public static int getSizeFromArr(byte[] sizeArr) throws IOException {
        Message message = (Message) objectFromByteArray(sizeArr);
        if(!message.getCommand().equals("size")) return 0;
        return (Integer) message.getData();
    }*/

}
