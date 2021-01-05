package com.example.demo.ui.coursenotice;

import androidx.lifecycle.ViewModel;

import com.example.demo.entity.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseNoticeViewModel extends ViewModel {


    private final List<Notification> noticeList = new ArrayList<>();

    public CourseNoticeViewModel() {
        initData();
    }

    public List<Notification> getNoticeList() {
        return noticeList;
    }

    private void initData() {
        this.noticeList.add(new Notification(
                10001,
                "10101010101",
                "232342",
                "上课",
                "周末照常上课",
                LocalDateTime.now()
        ));
    }
}
