package com.example.demo.entity;

import com.example.demo.TimeConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.time.LocalDateTime;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.OrderBy;

@Entity
public class Notification {
    @Id
    private int id;

    @NotNull
    private String senderId;

    @NotNull
    private String receiverId;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    @Convert(converter = TimeConverter.class, columnType = Long.class)
    private LocalDateTime time;

    @Generated(hash = 71050509)
    public Notification(int id, @NotNull String senderId,
            @NotNull String receiverId, @NotNull String title,
            @NotNull String content, @NotNull LocalDateTime time) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    @Generated(hash = 1855225820)
    public Notification() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
