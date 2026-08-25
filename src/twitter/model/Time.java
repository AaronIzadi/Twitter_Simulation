package twitter.model;

import java.time.LocalDateTime;

public class Time implements Comparable<Time> {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public Time() {
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public static Time now() {
        LocalDateTime ld = LocalDateTime.now();
        Time time = new Time();
        time.setYear(ld.getYear());
        time.setMonth(ld.getMonthValue());
        time.setDay(ld.getDayOfMonth());
        time.setHour(ld.getHour());
        time.setMinute(ld.getMinute());
        time.setSecond(ld.getSecond());
        return time;
    }

    @Override
    public String toString() {
        return year + " " + month + " " + day + " " + hour + " " + minute + " " + second;
    }

    public static Time valueOf(String timeInString) {
        Time time = new Time();
        String[] all = timeInString.split(" ");
        time.setYear(Integer.parseInt(all[0]));
        time.setMonth(Integer.parseInt(all[1]));
        time.setDay(Integer.parseInt(all[2]));
        time.setHour(Integer.parseInt(all[3]));
        time.setMinute(Integer.parseInt(all[4]));
        time.setSecond(Integer.parseInt(all[5]));
        return time;
    }

    @Override
    public int compareTo(Time other) {
        if (this.getYear() != other.getYear()) {
            return Integer.compare(this.getYear(), other.getYear());
        }
        if (this.getMonth() != other.getMonth()) {
            return Integer.compare(this.getMonth(), other.getMonth());
        }
        if (this.getDay() != other.getDay()) {
            return Integer.compare(this.getDay(), other.getDay());
        }
        if (this.getHour() != other.getHour()) {
            return Integer.compare(this.getHour(), other.getHour());
        }
        if (this.getMinute() != other.getMinute()) {
            return Integer.compare(this.getMinute(), other.getMinute());
        }
        return Integer.compare(this.getSecond(), other.getSecond());
    }
}
