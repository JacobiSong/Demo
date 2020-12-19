package com.example.demo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinCoursesWithTeachers {
    @Id
    private int id;

    @NotNull
    private String userId;

    @NotNull
    private String courseId;

    @Generated(hash = 2041855107)
    public JoinCoursesWithTeachers(int id, @NotNull String userId,
            @NotNull String courseId) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
    }

    @Generated(hash = 1428193010)
    public JoinCoursesWithTeachers() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
