package com.example.demo.ui.courses.chat.menu.member;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MemberViewModel extends ViewModel {

    private MutableLiveData<List<DatagramProto.User>> users;
    QueryObservable queryObservable;
    public MemberViewModel(String courseId, int identity) {
        users = new MutableLiveData<>();
        users.setValue(new ArrayList<>());
        queryObservable = MyApplication.getDatabase().createQuery("user",
                "select id, name, last_modified from user inner join t_join on id = user_id where course_id = ? and identity = ?", courseId, identity);
        queryObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = query.run();
                List<DatagramProto.User> list = new ArrayList<>();
                while(cursor.moveToNext()) {
                    list.add(DatagramProto.User.newBuilder().setId(cursor.getString(0)).setLastModified(cursor.getLong(2))
                            .setName(cursor.getString(1)).build());
                }
                users.postValue(list);
            }
        });
    }

    public LiveData<List<DatagramProto.User>> getUsers() {
        return users;
    }

}
