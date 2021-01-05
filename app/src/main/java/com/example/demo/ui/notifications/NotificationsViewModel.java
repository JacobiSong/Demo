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

    private final MutableLiveData<DatagramProto.Notifications> notifications;
    List<DatagramProto.Notification> list = new ArrayList<>();
    QueryObservable coursesObservable;
    long time = 0;
    Map<QueryObservable, Long> observables = new HashMap<>();
    public NotificationsViewModel() {
        notifications = new MutableLiveData<>();
        coursesObservable = MyApplication.getDatabase().createQuery("course", "");
        coursesObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = MyApplication.getDatabase().query("select id, last_modified from course where last_modified > ? order by last_modified desc", time);
                if (cursor.moveToFirst()) {
                    time = cursor.getLong(1);
                    do {
                        String courseId = cursor.getString(0);
                        QueryObservable queryObservable = MyApplication.getDatabase().createQuery(courseId + "_n", "");
                        queryObservable.subscribe(new Consumer<SqlBrite.Query>() {
                            @Override
                            public void accept(SqlBrite.Query query) throws Exception {
                                Cursor cursor = MyApplication.getDatabase().query("select id, title, time from " + courseId + "_n where time > ? order by time desc", time);
                                if (cursor.moveToFirst()) {
                                    observables.put(queryObservable, cursor.getLong(2));
                                }
                                do {
                                    list.add(DatagramProto.Notification.newBuilder().setId(cursor.getLong(0)).setReceiverId(courseId)
                                            .setTitle(cursor.getString(1)).setTime(cursor.getLong(2)).build());
                                } while (cursor.moveToNext());
                                list.sort(new Comparator<DatagramProto.Notification>() {
                                    @Override
                                    public int compare(DatagramProto.Notification o1, DatagramProto.Notification o2) {
                                        long time1 = o1.getTime();
                                        long time2 = o2.getTime();
                                        if (o1 == o2) return 0;
                                        return time1 < time2 ? 1 : -1;
                                    }
                                });
                                notifications.setValue(DatagramProto.Notifications.newBuilder().addAllNotifications(list).build());
                            }
                        });
                    } while (cursor.moveToNext());
                }
            }
        });
    }
    public LiveData<DatagramProto.Notifications> getNotifications() {
        return notifications;
    }
}