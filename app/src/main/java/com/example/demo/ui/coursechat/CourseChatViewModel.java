package com.example.demo.ui.coursechat;

import androidx.lifecycle.ViewModel;

import com.example.demo.entity.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseChatViewModel extends ViewModel {

    private final List<Message> messageList = new ArrayList<>();

    private static int initIndex = 10000003; // 临时用于messageID自增，连上数据库后可删除该变量

    public CourseChatViewModel() {
        initMsg();
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    private void initMsg() {
        this.messageList.add(new Message(
                10000001,
                "1188888888",
                "123456",
                "你好",
                LocalDateTime.now()
        ));
        this.messageList.add(new Message(
                10000002,
                "1100000000",
                "123456",
                "你好",
                LocalDateTime.now()
        ));
    }
}
