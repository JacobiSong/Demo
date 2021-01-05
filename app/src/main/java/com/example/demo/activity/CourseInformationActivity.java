package com.example.demo.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.demo.R;
import com.example.demo.entity.Course;
import com.example.demo.ui.courseinfo.CourseInfoViewModel;

public class CourseInformationActivity extends AppCompatActivity {

    private CourseInfoViewModel courseInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_infomation);
        courseInfoViewModel = new ViewModelProvider(this).get(CourseInfoViewModel.class);
        initView();
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

    private void initView() {
        Course course = this.courseInfoViewModel.getCourse();
        ((TextView) findViewById(R.id.textCourseName)).setText(course.getName());
        ((TextView) findViewById(R.id.textTeacherName)).setText("XXXXXXX");
        ((TextView) findViewById(R.id.textClassRoom)).setText(course.getClassroom());
        ((TextView) findViewById(R.id.textCourseTime)).setText(course.getTime());
        ((TextView) findViewById(R.id.textCourseRemark)).setText(course.getRemarks());
    }
}