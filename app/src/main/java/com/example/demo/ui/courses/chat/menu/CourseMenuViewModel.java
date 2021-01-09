package com.example.demo.ui.courses.chat.menu;

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

public class CourseMenuViewModel extends ViewModel {
    private final MutableLiveData<List<DatagramProto.User>> students;
    private final MutableLiveData<List<DatagramProto.User>> teachers;
    private long time = 0;
    private final QueryObservable observable;

    public CourseMenuViewModel(String courseId) {
        students = new MutableLiveData<>();
        students.setValue(new ArrayList<>());
        teachers = new MutableLiveData<>();
        teachers.setValue(new ArrayList<>());
        observable = MyApplication.getDatabase().createQuery("user", "");
        observable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = MyApplication.getDatabase().query("select id, name, identity, last_modified from user inner join t_join on id = user_id where course_id = ? and last_modified > ? " +
                                "order by last_modified", courseId, time);
                List<DatagramProto.User> teacherList = teachers.getValue();
                List<DatagramProto.User> studentList = students.getValue();
                while(cursor.moveToNext()) {
                    switch (cursor.getInt(2)) {
                        case 0:
                            studentList.add(DatagramProto.User.newBuilder().setId(cursor.getString(0)).setIdentityValue(0).setName(cursor.getString(1)).build());
                            break;
                        case 1:
                            teacherList.add(DatagramProto.User.newBuilder().setId(cursor.getString(0)).setIdentityValue(1).setName(cursor.getString(1)).build());
                            break;
                        default:
                            break;
                    }
                }
                if (cursor.moveToLast()) {
                    time = cursor.getLong(3);
                }
                students.postValue(studentList);
                teachers.postValue(teacherList);
            }
        });
    }

    public LiveData<List<DatagramProto.User>> getStudents() {
        return students;
    }

    public LiveData<List<DatagramProto.User>> getTeachers() {
        return teachers;
    }
}
