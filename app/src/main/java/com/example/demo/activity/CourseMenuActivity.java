package com.example.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.R;
import com.example.demo.adapter.MemberThumbAdapter;
import com.example.demo.entity.Student;
import com.example.demo.tool.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class CourseMenuActivity extends AppCompatActivity {

    private HorizontalListView horizontalListViewForStudent;
    private HorizontalListView horizontalListViewForTeacher;
    MemberThumbAdapter hListViewAdapterForStudent;
    MemberThumbAdapter hListViewAdapterForTeacher;


    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_menu);
//        //隐藏系统自带的标题栏
//        ActionBar actionbar = getSupportActionBar();
//        if (actionbar != null) {
//            actionbar.hide();
//        }
        initThumbView();
    }

    public void toCourseFileView(View view) {
        Intent intent = new Intent(this, CourseFileActivity.class);
        startActivity(intent);
    }

    public void toCourseNoticeView(View view) {
        Intent intent = new Intent(this, CourseNoticeActivity.class);
        startActivity(intent);
    }

    public void toCourseInfoView(View view) {
        Intent intent = new Intent(this, CourseInformationActivity.class);
        startActivity(intent);
    }

    public void toMembersView(View view) {
        Intent intent = new Intent(this, MembersActivity.class);
        startActivity(intent);
    }

    public void back(View view) {
        ((Activity) view.getContext()).finish();
    }

    private void initThumbView() {
        //临时初始化数据
        this.initStudents();
        horizontalListViewForStudent = (HorizontalListView) findViewById(R.id.horizontalListViewForStudentMember);
        hListViewAdapterForStudent = new MemberThumbAdapter(getApplicationContext(), studentList);
        horizontalListViewForStudent.setAdapter(hListViewAdapterForStudent);
        horizontalListViewForTeacher = (HorizontalListView) findViewById(R.id.horizontalListViewForTeacherMember);
        hListViewAdapterForTeacher = new MemberThumbAdapter(getApplicationContext(), studentList);
        horizontalListViewForTeacher.setAdapter(hListViewAdapterForTeacher);
    }

    //临时初始化数据使用
    private void initStudents() {
        this.studentList = new ArrayList<>();
        this.studentList.add(new Student(
                "123456234",
                "小明",
                "13999999999",
                "xxx@gmail.com",
                0,
                "qwer1234",
                "计算机科学与技术学院",
                "软件工程",
                11111
        ));
    }

}