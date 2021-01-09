package com.example.demo.ui.courses.chat.menu;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.functions.Consumer;

public class CourseInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("课程信息");
        setContentView(R.layout.activity_course_infomation);
        QueryObservable queryObservable = MyApplication.getDatabase().createQuery("course", "select name, classroom, time, remarks from course where id = ?",
                getIntent().getStringExtra("id"));
        queryObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = query.run();
                if (cursor.moveToFirst()) {
                    Cursor c = MyApplication.getDatabase().query("select name from user inner join t_join on id = user_id where course_id = ? and identity = 1", getIntent().getStringExtra("id"));
                    String teacher = "";
                    while (c.moveToNext()) {
                        teacher += c.getString(0) + " ";
                    }
                    ((TextView) findViewById(R.id.textCourseName)).setText(cursor.getString(0));
                    ((TextView) findViewById(R.id.textClassRoom)).setText(cursor.getString(1));
                    ((TextView) findViewById(R.id.textCourseTime)).setText(cursor.getString(2));
                    ((TextView) findViewById(R.id.textCourseRemark)).setText(cursor.getString(3));
                    ((TextView) findViewById(R.id.textTeacherName)).setText(teacher);
                }
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