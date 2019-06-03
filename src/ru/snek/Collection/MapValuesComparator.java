package ru.snek.Collection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

public class MapValuesComparator implements Comparator<String>, Serializable {
    public enum Sorting {DEF, NAME, SIZE, LOC, DATE}

    private Map<String, Malefactor> map;
    private Sorting sorting;

    public MapValuesComparator(Map<String, Malefactor> map, Sorting sorting) {
        this.map = map;
        this.sorting = sorting;
    }

    public int compare(String first, String second) {
        Malefactor a = map.get(first);
        Malefactor b = map.get(second);
        switch(sorting) {
            case NAME:
                return a.getName().compareTo(b.getName()) > 0 ? 1 : -1;
            case SIZE:
                return a.getAge() > b.getAge() ? 1 : -1;
            case LOC:
                int aLoc = a.getX()*a.getX() + a.getY()*a.getY();
                int bLoc = b.getX()*b.getX() + b.getY()*b.getY();
                return (aLoc > bLoc ? 1 : -1);
            case DATE:
                return a.getBirthDate().compareTo(b.getBirthDate()) > 0 ? 1 : -1;
            case DEF:
            default:
                return first.compareTo(second) > 0 ? 1 : -1;
        }
    }
}