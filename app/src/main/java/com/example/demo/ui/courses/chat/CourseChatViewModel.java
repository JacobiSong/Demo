package com.example.demo.ui.courses.chat;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;
import com.squareup.sqlbrite3.QueryObservable;

import java.util.ArrayList;
import java.util.List;

public class CourseChatViewModel extends ViewModel {

    private final MutableLiveData<List<DatagramProto.Message>> messages;

    public CourseChatViewModel(String courseId) {
        messages = new MutableLiveData<>();
        messages.setValue(new ArrayList<>());
        QueryObservable observable = MyApplication.getDatabase().createQuery(courseId + "_m", "select content, time, sender_id from " + courseId + "_m order by time");
        observable.subscribe(query -> {
            Cursor cursor = query.run();
            List<DatagramProto.Message> list = new ArrayList<>();
            assert cursor != null;
            while(cursor.moveToNext()) {
                list.add(DatagramProto.Message.newBuilder().setContent(cursor.getString(0))
                        .setSenderId(cursor.getString(2))
                        .setTime(cursor.getLong(1)).build());
            }
            messages.postValue(list);
        });
    }

    public LiveData<List<DatagramProto.Message>> getMessages() {
        return messages;
    }
}
