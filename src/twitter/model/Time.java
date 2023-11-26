package twitter.model;

import java.time.LocalDateTime;

public class Time implements Comparable {
    private static int year;
    private static int month;
    private static int day;
    private static int hour;
    private static int minute;
    private static int second;

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
        Time.year = year;
    }

    public void setMonth(int month) {
        Time.month = month;
    }

    public void setDay(int day) {
        Time.day = day;
    }

    public void setHour(int hour) {
        Time.hour = hour;
    }

    public void setMinute(int minute) {
        Time.minute = minute;
    }

    public void setSecond(int second) {
        Time.second = second;
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

    public String toString() {
        String time = "";
        time = year + "-" + month + "-" + day + "-" + hour + "-" + minute + "-" + second;
        return time;
    }

    public static Time valueOf(String timeInString) {
        Time time = new Time();
        String[] all = timeInString.split("-");
        time.setYear(Integer.parseInt(all[0]));
        time.setMonth(Integer.parseInt(all[1]));
        time.setDay(Integer.parseInt(all[2]));
        time.setHour(Integer.parseInt(all[3]));
        time.setMinute(Integer.parseInt(all[4]));
        time.setSecond(Integer.parseInt(all[5]));
        return time;
    }


    @Override
    public int compareTo(Object o) {
        Time time = (Time) o;
        if (this.getYear() > time.getYear()) {
            return 1;
        }
        if (this.getYear() < time.getYear()) {
            return -1;
        } else {
            if (this.getMonth() > time.getMonth()) {
                return 1;
            }
            if (this.getMonth() < time.getMonth()) {
                return -1;
            } else {
                if (this.getDay() > time.getDay()) {
                    return 1;
                }
                if (this.getDay() < time.getDay()) {
                    return -1;
                } else {
                    if (this.getHour() > time.getHour()) {
                        return 1;
                    }
                    if (this.getHour() < time.getHour()) {
                        return -1;
                    } else {
                        if (this.getMinute() > time.getMinute()) {
                            return 1;
                        }
                        if (this.getMinute() < time.getMinute()) {
                            return -1;
                        } else {
                            return Integer.compare(this.getSecond(), time.getSecond());
                        }
                    }
                }
            }
        }
    }
}
