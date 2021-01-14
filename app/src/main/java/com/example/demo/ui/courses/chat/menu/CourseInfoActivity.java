package com.example.demo.ui.courses.chat.menu;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.squareup.sqlbrite3.QueryObservable;

public class CourseInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("课程信息");
        setContentView(R.layout.activity_course_infomation);
        QueryObservable queryObservable = MyApplication.getDatabase().createQuery("course", "select name, classroom, time, remarks, semester from course where id = ?",
                getIntent().getStringExtra("id"));
        queryObservable.subscribe(query -> {
            Cursor cursor = query.run();
            assert cursor != null;
            if (cursor.moveToFirst()) {
                Cursor c = MyApplication.getDatabase().query("select name from user inner join t_join on id = user_id where course_id = ? and identity = 1", getIntent().getStringExtra("id"));
                StringBuilder teacher = new StringBuilder();
                while (c.moveToNext()) {
                    teacher.append(c.getString(0)).append(" ");
                }
                ((TextView) findViewById(R.id.textSemester)).setText(cursor.getString(4));
                ((TextView) findViewById(R.id.textCourseName)).setText(cursor.getString(0));
                ((TextView) findViewById(R.id.textClassRoom)).setText(cursor.getString(1));
                ((TextView) findViewById(R.id.textCourseTime)).setText(cursor.getString(2));
                ((TextView) findViewById(R.id.textCourseRemark)).setText(cursor.getString(3));
                ((TextView) findViewById(R.id.textTeacherName)).setText(teacher.toString());
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}