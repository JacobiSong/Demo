package com.example.demo.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.R;
import com.example.demo.entity.Course;

public class CourseInformationActivity extends AppCompatActivity {

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_infomation);

        initInfo();
        //设置显示内容
        ((TextView) findViewById(R.id.textCourseName)).setText(this.course.getName());
        ((TextView) findViewById(R.id.textTeacherName)).setText("XXXXXXX");
        ((TextView) findViewById(R.id.textClassRoom)).setText(this.course.getClassroom());
        ((TextView) findViewById(R.id.textCourseTime)).setText(this.course.getTime());
        ((TextView) findViewById(R.id.textCourseRemark)).setText(this.course.getRemarks());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void back(View view) {
        this.finish();
    }

    private void initInfo() {
        this.course = new Course(
                "cs_10001",
                "数据结构",
                "正心716",
                "[2-15周]周一12节、周三12节",
                true,
                "先导课程：C语言程序设计",
                3
        );
    }
}