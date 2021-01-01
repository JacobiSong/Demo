package com.example.demo.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.CourseNoticeAdapter;
import com.example.demo.entity.Notification;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseNoticeActivity extends AppCompatActivity {

    private final List<Notification> noticeList = new ArrayList<>();
    private CourseNoticeAdapter adapter;
    private RecyclerView noticeRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notice);
        initNotice();
        this.noticeRecyclerView = findViewById(R.id.recyclerViewInMenbersView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.noticeRecyclerView.setLayoutManager(linearLayoutManager);
        this.adapter = new CourseNoticeAdapter(this.noticeList);
        this.noticeRecyclerView.setAdapter(this.adapter);
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

    public void back(@NotNull View view) {
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNotice() {
        this.noticeList.add(new Notification(
                10001,
                "10101010101",
                "232342",
                "上课",
                "周末照常上课",
                LocalDateTime.now()
        ));
    }
}