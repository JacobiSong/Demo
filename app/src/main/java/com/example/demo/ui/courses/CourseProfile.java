package com.example.demo.ui.courses;

public class CourseProfile {
    private final String className;
    private final String classMessage;
    public CourseProfile(String className, String classMessage) {
        this.className = className;
        this.classMessage = classMessage;
    }

    public String getClassName() {
        return className;
    }

    public String getClassMessage() {
        return classMessage;
    }
}
