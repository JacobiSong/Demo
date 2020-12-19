package com.example.demo;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeConverter implements PropertyConverter<LocalDateTime, Long> {

    @Override
    public LocalDateTime convertToEntityProperty(Long databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(databaseValue), ZoneId.systemDefault());
    }

    @Override
    public Long convertToDatabaseValue(LocalDateTime entityProperty) {
        return entityProperty.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}