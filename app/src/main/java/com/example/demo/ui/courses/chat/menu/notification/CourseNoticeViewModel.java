package com.example.demo.ui.courses.chat.menu.notification;

import android.database.Cursor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;
import com.squareup.sqlbrite3.QueryObservable;

import java.util.ArrayList;
import java.util.List;

public class CourseNoticeViewModel extends ViewModel {
    private final MutableLiveData<List<DatagramProto.Notification>> notifications;

    public CourseNoticeViewModel(String courseId) {
        notifications = new MutableLiveData<>();
        notifications.setValue(new ArrayList<>());
        QueryObservable queryObservable = MyApplication.getDatabase().createQuery(courseId + "_n",
                "select id, title, time, receiver_id, sender_id from " + courseId + "_n order by time");
        queryObservable.subscribe(query -> {
            Cursor cursor = query.run();
            List<DatagramProto.Notification> list = new ArrayList<>();
            assert cursor != null;
            while(cursor.moveToNext()) {
                list.add(DatagramProto.Notification.newBuilder().setId(cursor.getLong(0)).setReceiverId(cursor.getString(3))
                        .setSenderId(cursor.getString(4)).setTitle(cursor.getString(1)).setTime(cursor.getLong(2)).build());
            }
            notifications.postValue(list);
        });
    }

    public MutableLiveData<List<DatagramProto.Notification>> getNotifications() {
        return notifications;
    }
}
