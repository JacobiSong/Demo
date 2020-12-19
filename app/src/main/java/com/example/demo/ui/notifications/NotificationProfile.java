package com.example.demo.ui.notifications;

public class NotificationProfile {
    private final String course;
    private final String message;
    private final String time;
    public NotificationProfile(String course, String message, String time) {
        this.course = course;
        this.message = message;
        this.time = time;
    }

    public String getCourse() {
        return course;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
