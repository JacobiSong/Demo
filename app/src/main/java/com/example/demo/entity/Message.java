package com.example.demo.entity;

import com.example.demo.TimeConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.OrderBy;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    private int id;

    @NotNull
    private String senderId;

    @NotNull
    private String receiverId;

    @NotNull
    private String content;

    @NotNull
    @Convert(converter = TimeConverter.class, columnType = Long.class)
    private LocalDateTime time;

    @Generated(hash = 2026461602)
    public Message(int id, @NotNull String senderId, @NotNull String receiverId,
            @NotNull String content, @NotNull LocalDateTime time) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.time = time;
    }

    @Generated(hash = 637306882)
    public Message() {
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
