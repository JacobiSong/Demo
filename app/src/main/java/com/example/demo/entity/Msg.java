package com.example.demo.entity;

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;

    private final int type; //消息类型
    private final String content; //消息内容
    private final String name; //发送人姓名

    public Msg(String content, int type) {
        this.type = type;
        this.content = content;
        this.name = "";
    }

    public Msg(String content, int type, String name) {
        this.type = type;
        this.content = content;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }
}
