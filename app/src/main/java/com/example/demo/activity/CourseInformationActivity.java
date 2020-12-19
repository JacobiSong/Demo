package com.example.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
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
        ((TextView) findViewById(R.id.textCourseName)).setText(this.course.getCourseName());
        ((TextView) findViewById(R.id.textTeacherName)).setText(this.course.getTeacherName());
        ((TextView) findViewById(R.id.textClassRoom)).setText(this.course.getClassroom());
        ((TextView) findViewById(R.id.textCourseTime)).setText(this.course.getCourseTime());
        ((TextView) findViewById(R.id.textCourseRemark)).setText(this.course.getRemark());

        //隐藏系统自带的标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
    }

    public void back(View view) {
        this.finish();
    }

    private void initInfo() {
        this.course = new Course(
                "数据结构",
                "XXXXXX",
                "[2-15周]周一12节、周三12节",
                "正心716",
                "先导课程：C语言程序设计"
        );
    }
}