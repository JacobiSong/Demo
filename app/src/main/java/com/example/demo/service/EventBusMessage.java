package com.example.demo.service;

public class EventBusMessage {
    public int what;
    public Object obj;

    public EventBusMessage(Object obj) {
        this.what = 0;
        this.obj = obj;
    }

    public EventBusMessage(int what, Object obj) {
        this.what = what;
        this.obj = obj;
    }
}
