package com.example.demo;

import android.app.Application;

import com.example.demo.dao.DaoMaster;
import com.example.demo.dao.DaoSession;
import com.example.demo.entity.Course;
import com.example.demo.entity.Notification;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class MyApplication extends Application {

    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void InitialDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "we_course_db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
}
