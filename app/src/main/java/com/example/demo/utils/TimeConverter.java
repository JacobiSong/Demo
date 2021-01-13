package com.example.demo.utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeConverter {

    public static String long2LocalDateTime(long time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        if (now.getYear() == localDateTime.getYear()) {
            if (now.getMonth() == localDateTime.getMonth()) {
                if (now.getDayOfMonth() - localDateTime.getDayOfMonth() < 7 && now.getDayOfWeek().getValue() >= localDateTime.getDayOfWeek().getValue()) {
                    if (now.getDayOfWeek() == localDateTime.getDayOfWeek()) {
                        return localDateTime.getHour() + ":" + localDateTime.getMinute();
                    } else {
                        return DayOfWeek2Chinese(localDateTime.getDayOfWeek()) + " " + localDateTime.getHour() + ":" + localDateTime.getMinute();
                    }
                } else {
                    return localDateTime.getDayOfMonth() + "日";
                }
            } else {
                return localDateTime.getMonth() + "-" + localDateTime.getDayOfMonth();
            }
        } else {
            return localDateTime.getYear() + "-" + localDateTime.getMonth() + "-" + localDateTime.getDayOfMonth();
        }
    }

    public static String DayOfWeek2Chinese(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "星期一";
            case TUESDAY:
                return "星期二";
            case WEDNESDAY:
                return "星期三";
            case THURSDAY:
                return "星期四";
            case FRIDAY:
                return "星期五";
            case SATURDAY:
                return "星期六";
            case SUNDAY:
                return "星期日";
            default:
                return "";
        }
    }
/*
    public static Long localDateTime2Long(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

 */
}