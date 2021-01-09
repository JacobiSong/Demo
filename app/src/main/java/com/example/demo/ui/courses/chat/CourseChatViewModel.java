package com.example.demo.ui.courses.chat;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class CourseChatViewModel extends ViewModel {

    private final MutableLiveData<List<DatagramProto.Message>> messages;
    private final QueryObservable observable;
    long time = 0;
    public CourseChatViewModel(String courseId) {
        messages = new MutableLiveData<>();
        messages.setValue(new ArrayList<>());
        observable = MyApplication.getDatabase().createQuery(courseId + "_m", "select content, time, sender_id from " + courseId + "_m where time > ? order by time", time);
        observable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = query.run();
                List<DatagramProto.Message> list = messages.getValue();
                while(cursor.moveToFirst()) {
                    list.add(DatagramProto.Message.newBuilder().setContent(cursor.getString(0))
                            .setSenderId(cursor.getString(2))
                            .setTime(cursor.getLong(1)).build());
                }
                if (cursor.moveToLast()) {
                    time = cursor.getLong(1);
                }
                messages.postValue(list);
            }
        });
    }

    public LiveData<List<DatagramProto.Message>> getMessages() {
        return messages;
    }
}
