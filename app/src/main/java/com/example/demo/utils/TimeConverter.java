package com.example.demo.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeConverter {

    public static LocalDateTime long2LocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }
/*
    public static Long localDateTime2Long(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

 */
}