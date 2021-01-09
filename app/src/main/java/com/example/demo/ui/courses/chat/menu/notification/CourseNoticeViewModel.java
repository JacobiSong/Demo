package com.example.demo.ui.courses.chat.menu.notification;

import android.database.Cursor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class CourseNoticeViewModel extends ViewModel {
    private final MutableLiveData<List<DatagramProto.Notification>> notifications;
    private long time = 0;
    QueryObservable queryObservable;
    public CourseNoticeViewModel(String courseId) {
        notifications = new MutableLiveData<>();
        notifications.setValue(new ArrayList<>());
        QueryObservable queryObservable = MyApplication.getDatabase().createQuery(courseId + "_n",
                "select id, title, time, receiver_id from " + courseId + "_n where time > ? order by time", time);
        queryObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = query.run();
                List<DatagramProto.Notification> list = notifications.getValue();
                while(cursor.moveToNext()) {
                    list.add(DatagramProto.Notification.newBuilder().setId(cursor.getLong(0)).setReceiverId(cursor.getString(3))
                            .setTitle(cursor.getString(1)).setTime(cursor.getLong(2)).build());
                }
                if (cursor.moveToLast()) {
                    time = cursor.getLong(2);
                }
                notifications.postValue(list);
            }
        });
    }

    public MutableLiveData<List<DatagramProto.Notification>> getNotifications() {
        return notifications;
    }
}
