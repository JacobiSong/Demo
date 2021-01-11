package com.example.demo.ui.courses.chat.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.ui.courses.chat.menu.notification.AddNotificationActivity;
import com.example.demo.ui.courses.chat.menu.notification.CourseNoticeActivity;
import com.example.demo.ui.courses.chat.menu.member.MembersActivity;
import com.example.demo.view.HorizontalListView;

public class CourseMenuActivity extends AppCompatActivity {

    private MemberThumbAdapter hListViewAdapterForStudent;
    private MemberThumbAdapter hListViewAdapterForTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("name"));
        CourseMenuViewModel courseMenuViewModel = new CourseMenuViewModel(getIntent().getStringExtra("id"));
        hListViewAdapterForStudent = new MemberThumbAdapter(this, courseMenuViewModel.getStudents());
        hListViewAdapterForTeacher = new MemberThumbAdapter(this, courseMenuViewModel.getTeachers());
        courseMenuViewModel.getStudents().observe(this, users -> hListViewAdapterForStudent.notifyDataSetChanged());
        courseMenuViewModel.getTeachers().observe(this, users -> hListViewAdapterForTeacher.notifyDataSetChanged());
        setContentView(R.layout.activity_course_menu);
        HorizontalListView horizontalListViewForStudent = findViewById(R.id.horizontalListViewForStudentMember);
        horizontalListViewForStudent.setAdapter(hListViewAdapterForStudent);
        HorizontalListView horizontalListViewForTeacher = findViewById(R.id.horizontalListViewForTeacherMember);
        horizontalListViewForTeacher.setAdapter(hListViewAdapterForTeacher);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE).getInt("identity", 0) == 1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_add_notification, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.notification_add:
                startActivity(new Intent(this, AddNotificationActivity.class).putExtra("id", getIntent().getStringExtra("id")));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toCourseNoticeView(View view) {
        startActivity(new Intent(this, CourseNoticeActivity.class).putExtra("id", getIntent().getStringExtra("id")));
    }

    public void toCourseInfoView(View view) {
        startActivity(new Intent(this, CourseInfoActivity.class).putExtra("id", getIntent().getStringExtra("id")));
    }

    public void toTeachers(View view) {
        startActivity(new Intent(this, MembersActivity.class).putExtra("identity", 1)
                .putExtra("id", getIntent().getStringExtra("id")));
    }

    public void toStudents(View view) {
        startActivity(new Intent(this, MembersActivity.class).putExtra("identity", 0)
                .putExtra("id", getIntent().getStringExtra("id")));
    }
}