package com.example.demo.utils;

import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.example.demo.MyApplication;


public class DatabaseHelper {
    private static final String CREATE_COURSE = "create table course (" +
            "id varchar(10) primary key not null, " +
            "name varchar(20) not null, " +
            "classroom varchar(10) not null, " +
            "time varchar(50) not null, " +
            "semester varchar(10) not null, " +
            "remarks varchar(100), " +
            "last_modified bigint not null)";
    private static final String CREATE_USER = "create table user (" +
            "id varchar(10) primary key not null, " +
            "name varchar(20) not null, " +
            "identity int not null, " +
            "phone varchar(11), " +
            "email varchar(30), " +
            "gender int not null default 0, " +
            "last_modified bigint not null)";
    private static final String CREATE_TEACHER = "create table teacher (" +
            "id varchar(10) not null primary key, " +
            "department varchar(20))";
    private static final String CREATE_STUDENT = "create table student (" +
            "id varchar(10) not null primary key, " +
            "class_no varchar(10), " +
            "major varchar(20), " +
            "department varchar(20))";
    private static final String CREATE_T_JOIN = "create table t_join (" +
            "user_id varchar(10) not null, " +
            "course_id varchar(10) not null, " +
            "constraint un1 unique(user_id, course_id))";
    private static final String CREATE_T_PUSH = "create table t_push (" +
            "id integer not null primary key autoincrement, " +
            "type int not null, " +
            "id1 varchar(10) not null, " +
            "time bigint not null)";

    private final SupportSQLiteOpenHelper supportSQLiteOpenHelper;

    public DatabaseHelper(@Nullable String username, int version) {
        SupportSQLiteOpenHelper.Configuration configuration = SupportSQLiteOpenHelper.Configuration
                .builder(MyApplication.getInstance()).name(username + ".db").callback(new DatabaseCallback(version)).build();
        supportSQLiteOpenHelper = new FrameworkSQLiteOpenHelperFactory().create(configuration);
    }

    public SupportSQLiteOpenHelper getSupportSQLiteOpenHelper() {
        return supportSQLiteOpenHelper;
    }

    private static class DatabaseCallback extends SupportSQLiteOpenHelper.Callback {
        /**
         * Creates a new Callback to get database lifecycle events.
         *
         * @param version The version for the database instance. See {@link #version}.
         */
        public DatabaseCallback(int version) {
            super(version);
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            db.execSQL(CREATE_COURSE);
            db.execSQL(CREATE_USER);
            db.execSQL(CREATE_TEACHER);
            db.execSQL(CREATE_STUDENT);
            db.execSQL(CREATE_T_JOIN);
            db.execSQL(CREATE_T_PUSH);
        }

        @Override
        public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
