package ua.opnu.Task1;


public class TimeSpan {
    private int hours;
    private int minutes;


    public TimeSpan() {
        this.hours = 0;
        this.minutes = 0;
    }

    public TimeSpan(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Хвилини не можуть бути від'ємними");
        convert(minutes);
    }

    public TimeSpan(int hours, int minutes) {
        check(hours, minutes);
        this.hours = hours;
        this.minutes = minutes;
    }

    public TimeSpan(TimeSpan ts) {
        this.hours = ts.getHours();
        this.minutes = ts.getMinutes();
    }

    private void check(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes >= 60)
            throw new IllegalArgumentException("В аргументах не можуть бути відʼємні значення, або значення хвилин вийшло за діапазон 0..59");
    }

    public int getTotalMinutes() {
        return this.minutes + this.hours * 60;
    }

    public double getTotalHours() {
        return hours + minutes / 60.0;
    }

    public void add(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Аргумент хвилин повинен бути більшим за нуль");
        convert(minutes + getTotalMinutes());
    }

    public void add(int hours, int minutes) {
        if (minutes < 0 || hours < 0 || minutes > 59) throw new IllegalArgumentException();
        int total = getTotalMinutes() + hours * 60 + minutes;
        this.convert(total);
    }
    public void add(TimeSpan ts) {
        add(ts.getHours(), ts.getMinutes());
    }

    public void subtract(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Аргумент хвилин повинен бути більшим за нуль");
        int total = getTotalMinutes() - minutes;
        if (total < 0) throw new IllegalArgumentException("Хвилини в аргументі перевищують кількість хвилин в обʼєкті");
        convert(total);
    }

    public void subtract(int hours, int minutes) {
        if (minutes < 0 || hours < 0 || minutes > 59) throw new IllegalArgumentException("Аргументи хвилин та годин повинні бути більшим за нуль");
        int total = getTotalMinutes() - minutes - hours * 60;
        if (total < 0) throw new IllegalArgumentException("Хвилини в аргументі перевищують кількість хвилин в обʼєкті");
        convert(total);
    }

    public void subtract(TimeSpan ts) {
        subtract(ts.getHours(), ts.getMinutes());
    }

    public void scale(int factor) {
        if (factor <= 0) throw new IllegalArgumentException("factor повинен бути більшим за 0");
        convert(getTotalMinutes() * factor);
    }

    private void convert(int totalminutes) {
        this.hours = totalminutes / 60;
        this.minutes = totalminutes % 60;
    }


    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }



    @Override
    public String toString() {
        return hours + " год " + minutes + " хв";
    }
}