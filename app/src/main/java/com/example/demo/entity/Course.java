package com.example.demo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Course {
    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String classroom;

    @NotNull
    private String time;

    @NotNull
    private boolean hasGroup;

    private String remarks;

    @NotNull
    private int semester;

    @Generated(hash = 348460780)
    public Course(String id, @NotNull String name, @NotNull String classroom,
            @NotNull String time, boolean hasGroup, String remarks, int semester) {
        this.id = id;
        this.name = name;
        this.classroom = classroom;
        this.time = time;
        this.hasGroup = hasGroup;
        this.remarks = remarks;
        this.semester = semester;
    }

    @Generated(hash = 1355838961)
    public Course() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassroom() {
        return this.classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getHasGroup() {
        return this.hasGroup;
    }

    public void setHasGroup(boolean hasGroup) {
        this.hasGroup = hasGroup;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getSemester() {
        return this.semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
