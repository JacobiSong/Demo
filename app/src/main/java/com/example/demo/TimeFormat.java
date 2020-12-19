package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 格式化日期
 */
public class TimeFormat {
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return formatter.format(localDateTime);
    }
}