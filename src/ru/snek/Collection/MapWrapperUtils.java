package ru.snek.Collection;

import ru.snek.Utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class MapWrapperUtils {

    public static String parseJson(String str) throws Exception{
        class WrongFormatException extends Exception{
            @Override
            public String getMessage() {
                return "Неверный формат данных!";
            }
        }
        str = str.replaceAll("\\s+", "");
        str = str.replaceAll("\n", "");
        if(str.length() < 2) throw new WrongFormatException();
        str = str.substring(1, str.length()-1);
        if(Utils.countInStr(str, '"') % 2 != 0) throw new WrongFormatException();
        ArrayList<Integer> commas = Utils.getExtCommas(str);
        ArrayList<String> strArr = Utils.splitStrByIndex(str, commas);
        if(strArr.size() < 1 && !str.isEmpty())  strArr.add(str);
		String[] fields = new String[8];
		fields[0] = "\"Безымянный\"";
        fields[1] = Integer.toString(18 + (new Random().nextInt(40)));
        fields[2] = Integer.toString(new Random().nextInt(100));
        fields[3] = Integer.toString(new Random().nextInt(100));
        fields[4] = Long.toString(new Date().getTime());
        fields[5] = Malefactor.Condition.AWAKEN.name();
        fields[6] = Malefactor.Weight.HEAVY.name();
        fields[7] = '\"'+Boolean.toString(false)+'\"';
        for(String s : strArr) {
            ArrayList<Integer> a = new ArrayList<>();
            if(s.indexOf(":") > -1) a.add(s.indexOf(":"));
            ArrayList<String> pair = Utils.splitStrByIndex(s, a);
            if(pair.size() < 2) throw new WrongFormatException();
            if(pair.get(0).length() < 2) throw new WrongFormatException();
            pair.set(0, pair.get(0).substring(1, pair.get(0).length()-1));
            switch (pair.get(0)) {
                case "name" :
					fields[0] = pair.get(1);
                    break;
                case "age" :
					fields[1] = pair.get(1);
					break;
                case "x" :
					fields[2] = pair.get(1);
					break;
                case "y" :
					fields[3] = pair.get(1);
					break;
                case "birthDate" :
					fields[4] = pair.get(1);
					break;
                case "condition" :
					fields[5] = pair.get(1);
					break;
                case "abilityToLift" :
                    fields[6] = pair.get(1);
                    break;
                case "canSleep" :
                    fields[7] = pair.get(1);
                    break;
                default:
                    throw new WrongFormatException();
            }
        }
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < fields.length; ++i) {
		    result.append(fields[i]);
		    if(i < fields.length - 1) result.append("; ");
        }

        return result.toString();
    }

    public static Malefactor elementFromString(String str) throws Exception {
        String[] arr = str.split(";");
        if (arr.length != 8) {
            throw new Exception("Неверный формат данных!");
        }
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = arr[i].trim();
        }
        Malefactor mf = new Malefactor();
        mf.setName(arr[0].substring(1, arr[0].length() - 1));
        mf.setAge(Integer.valueOf(arr[1]));
        mf.setX(Integer.valueOf(arr[2]));
        mf.setY(Integer.valueOf(arr[3]));
        mf.setBirthDate(LocalDateTime.ofEpochSecond(Long.valueOf(arr[4]),0, ZoneOffset.of("+03:00:00")));
        mf.setCondition(Malefactor.Condition.valueOf(arr[5]));
        mf.setAbilityToLift(Malefactor.Weight.valueOf(arr[6]));
        mf.setCanSleep(Boolean.valueOf(arr[7]));
        return mf;
    }
}
