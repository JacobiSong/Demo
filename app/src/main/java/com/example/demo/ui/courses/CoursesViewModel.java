package com.example.demo.ui.courses;

import android.database.Cursor;
import android.util.Log;

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

public class CoursesViewModel extends ViewModel {

    private final MutableLiveData<List<DatagramProto.Course>> courses;
    private final QueryObservable queryObservable;

    public CoursesViewModel() {
        courses = new MutableLiveData<>();
        courses.setValue(new ArrayList<>());
        queryObservable = MyApplication.getDatabase().createQuery("course", "select id, name, time, semester from course");
        queryObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = query.run();
                List<DatagramProto.Course> list = new ArrayList<>();
                while(cursor.moveToNext()) {
                    list.add(DatagramProto.Course.newBuilder().setId(cursor.getString(0)).setName(cursor.getString(1))
                            .setTime(cursor.getString(2)).setSemester(cursor.getString(3)).build());
                }
                courses.postValue(list);
            }
        });
    }

    public LiveData<List<DatagramProto.Course>> getCourses() {
        return courses;
    }
}