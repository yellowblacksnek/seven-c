package ru.snek;

import java.io.*;
import java.util.ArrayList;

import static ru.snek.Printer.*;

public class FileInteractor {

    public static File openFile(String path) throws Exception{
        File file = new File(path);
        if(!file.exists()) {
            throw new Exception("Файл не существует: " + file.getPath());
        }
        if(!(file.canRead() && file.canWrite())) {
            throw new Exception("Нет нужных прав для работы с файлом!");
        }
        return file;
    }

    public static String getFileString(File file) {
        StringBuilder strb = new StringBuilder();
        try(BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(buf, "UTF-8"))) {
            int c;
            while ((c = reader.read()) > 0) {
                strb.append((char)c);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return strb.toString();
    }

    public static boolean toFile(ArrayList<String > arr, File file) {
        if(!file.exists()) println("Файл куда-то пропал, но ничего страшного, сейчас будет новый.");
		if(!file.canWrite()) {
			errprintln("Нет прав для записи в файл: " + file.getPath());
			return false;
		}
        try(BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(file))) {
            StringBuilder strb = new StringBuilder();
            for(int i = 0; i < arr.size(); ++i) {
                strb.append(arr.get(i));
                if(i < arr.size()-1) strb.append("\n");
            }
            buf.write(strb.toString().getBytes());
        } catch(IOException e) {
            e.printStackTrace();
        }
		return true;
    }
}
