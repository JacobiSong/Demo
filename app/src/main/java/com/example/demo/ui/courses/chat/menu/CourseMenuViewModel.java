package com.example.demo.ui.courses.chat.menu;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;
import com.squareup.sqlbrite3.QueryObservable;

import java.util.ArrayList;
import java.util.List;

public class CourseMenuViewModel extends ViewModel {
    private final MutableLiveData<List<DatagramProto.User>> students;
    private final MutableLiveData<List<DatagramProto.User>> teachers;

    public CourseMenuViewModel(String courseId) {
        students = new MutableLiveData<>();
        students.setValue(new ArrayList<>());
        teachers = new MutableLiveData<>();
        teachers.setValue(new ArrayList<>());
        QueryObservable observable = MyApplication.getDatabase().createQuery("user", "");
        observable.subscribe(query -> {
            Cursor cursor = MyApplication.getDatabase().query("select id, name, identity, last_modified from user inner join t_join on id = user_id where course_id = ? " +
                            "order by last_modified", courseId);
            List<DatagramProto.User> teacherList = new ArrayList<>();
            List<DatagramProto.User> studentList = new ArrayList<>();
            assert cursor != null;
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
            students.postValue(studentList);
            teachers.postValue(teacherList);
        });
    }

    public LiveData<List<DatagramProto.User>> getStudents() {
        return students;
    }

    public LiveData<List<DatagramProto.User>> getTeachers() {
        return teachers;
    }
}
