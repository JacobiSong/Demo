package com.example.demo.ui.notifications;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<List<DatagramProto.Notification>> notifications;

    public NotificationsViewModel() {
        notifications = new MutableLiveData<>();
        notifications.setValue(new ArrayList<>());
        QueryObservable coursesObservable = MyApplication.getDatabase().createQuery("course", "select id from course");
        coursesObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = query.run();
                List<DatagramProto.Notification> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String courseId = cursor.getString(0);
                    Cursor c = MyApplication.getDatabase().query("select id, title, time from " + courseId + "_n");
                    while (c.moveToNext()) {
                        list.add(DatagramProto.Notification.newBuilder().setId(c.getLong(0)).setReceiverId(courseId)
                                .setTitle(c.getString(1)).setTime(c.getLong(2)).build());
                    }
                }
                list.sort(new Comparator<DatagramProto.Notification>() {
                    @Override
                    public int compare(DatagramProto.Notification o1, DatagramProto.Notification o2) {
                        long time1 = o1.getTime();
                        long time2 = o2.getTime();
                        if (o1 == o2) return 0;
                        return time1 < time2 ? 1 : -1;
                    }
                });
                notifications.postValue(list);
            }
        });
    }
    public LiveData<List<DatagramProto.Notification>> getNotifications() {
        return notifications;
    }
}