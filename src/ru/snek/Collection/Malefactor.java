package ru.snek.Collection;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Malefactor implements Comparable<Malefactor>, Serializable {

    public static final String JULIO_NAME = "Жулио";
    public static final String SPROOTS_NAME = "Спрутс";

    public enum Condition implements Serializable{
        AWAKEN,
        ASLEEP,
        AWAKEN_AND_GROANING,
        AWAKEN_AND_FALLEN,
    }

    public enum Weight implements Serializable {
        VERY_LIGHT,
        LIGHT,
        MEDIUM,
        HEAVY,
        VERY_HEAVY,
    }


    private String name;
    private int age;
    private int x;
    private int y;
    private LocalDateTime birthDate;
    private Condition condition;
    private Weight abilityToLift;
    private boolean canSleep;

    public Malefactor() {
        birthDate = LocalDateTime.now();
    }

    public Malefactor(String name, int age, int x, int y, LocalDateTime birthDate, Condition condition, Weight abilityToLift, boolean canSleep) {
        setName(name);
        setAge(age);
        setX(x);
        setY(y);
        setBirthDate(birthDate);
        setCondition(condition);
        setAbilityToLift(abilityToLift);
        setCanSleep(canSleep);
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public int getX() { return x; }
    public int getY() { return y; }
    public LocalDateTime getBirthDate() { return birthDate; }
    public Condition getCondition() { return condition; }
    public Weight getAbilityToLift() { return abilityToLift; }
    public boolean isCanSleep() { return canSleep; }

    public void setName(String name) {this.name = name;}
    public void setAge(int age) {this.age = age;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setBirthDate(LocalDateTime birthDate) {this.birthDate = birthDate;}
    public void setCondition(Condition condition) {this.condition = condition;}
    public void setAbilityToLift(Weight abilityToLift) {this.abilityToLift = abilityToLift;}
    public void setCanSleep(boolean canSleep) {this.canSleep = canSleep;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Malefactor that = (Malefactor) o;
        return age == that.age &&
                x == that.x &&
                y == that.y &&
                canSleep == that.canSleep &&
                name.equals(that.name) &&
                birthDate.equals(that.birthDate) &&
                condition == that.condition &&
                abilityToLift == that.abilityToLift;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, x, y, birthDate, condition, abilityToLift, canSleep);
    }

    @Override
    public String toString() {
        return "Malefactor{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", x=" + x +
                ", y=" + y +
                ", birthDate=" + birthDate +
                ", condition=" + condition +
                ", abilityToLift=" + abilityToLift +
                ", canSleep=" + canSleep +
                '}';
    }

    @Override
    public int compareTo(Malefactor o) {
        int result;
        result = name.compareTo(o.name);
        if(result == 0) result += abilityToLift.compareTo(o.abilityToLift);
        return result;
    }
}
