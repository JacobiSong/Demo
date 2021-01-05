package com.example.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.demo.R;
import com.example.demo.tool.HorizontalListView;
import com.example.demo.ui.coursemenu.CourseMenuViewModel;
import com.example.demo.ui.coursemenu.MemberThumbAdapter;

public class CourseMenuActivity extends AppCompatActivity {

    private CourseMenuViewModel courseMenuViewModel;
    private HorizontalListView horizontalListViewForStudent;
    private HorizontalListView horizontalListViewForTeacher;
    MemberThumbAdapter hListViewAdapterForStudent;
    MemberThumbAdapter hListViewAdapterForTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_menu);
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
        this.courseMenuViewModel = new ViewModelProvider(this).get(CourseMenuViewModel.class);
        horizontalListViewForStudent = (HorizontalListView) findViewById(R.id.horizontalListViewForStudentMember);
        hListViewAdapterForStudent = new MemberThumbAdapter(getApplicationContext(), this.courseMenuViewModel.getStudentList());
        horizontalListViewForStudent.setAdapter(hListViewAdapterForStudent);
        horizontalListViewForTeacher = (HorizontalListView) findViewById(R.id.horizontalListViewForTeacherMember);
        hListViewAdapterForTeacher = new MemberThumbAdapter(getApplicationContext(), this.courseMenuViewModel.getStudentList());
        horizontalListViewForTeacher.setAdapter(hListViewAdapterForTeacher);
    }


}