package com.example.demo.ui.courses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    private MutableLiveData<DatagramProto.Courses> courses;

    QueryObservable queryObservable;

    public CoursesViewModel() {
        courses = new MutableLiveData<>();
        queryObservable = MyApplication.getDatabase().createQuery("course", "select id, name, time, semester from course");
        queryObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                DatagramProto.Courses.Builder builder = DatagramProto.Courses.newBuilder();
                Cursor cursor = query.run();
                while(cursor.moveToNext()) {
                    builder.addCourses(DatagramProto.Course.newBuilder().setId(cursor.getString(0)).setName(cursor.getString(1))
                            .setTime(cursor.getString(2)).setSemester(cursor.getString(3)).build());
                }
                courses.setValue(builder.build());
            }
        });
    }

    public LiveData<DatagramProto.Courses> getCourses() {
        return courses;
    }
}