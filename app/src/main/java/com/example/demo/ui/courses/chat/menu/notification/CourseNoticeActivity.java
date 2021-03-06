package com.example.demo.ui.courses.chat.menu.notification;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

public class CourseNoticeActivity extends AppCompatActivity {
    private CourseNoticeAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("群通知");
        setContentView(R.layout.activity_course_notice);
        CourseNoticeViewModel courseNoticeViewModel = new CourseNoticeViewModel(getIntent().getStringExtra("id"));
        adapter = new CourseNoticeAdapter(this, courseNoticeViewModel.getNotifications());
        courseNoticeViewModel.getNotifications().observe(this, notifications -> adapter.notifyDataSetChanged());
        RecyclerView noticeRecyclerView = findViewById(R.id.recyclerViewInMenbersView);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noticeRecyclerView.setAdapter(adapter);
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