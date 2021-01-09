package com.example.demo.ui.courses.chat.menu.notification;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.ui.courses.chat.menu.notification.CourseNoticeAdapter;
import com.example.demo.ui.courses.chat.menu.notification.CourseNoticeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseNoticeActivity extends AppCompatActivity {

    private CourseNoticeViewModel courseNoticeViewModel;
    private CourseNoticeAdapter adapter;
    private RecyclerView noticeRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("群通知");
        setContentView(R.layout.activity_course_notice);
        courseNoticeViewModel = new CourseNoticeViewModel(getIntent().getStringExtra("id"));
        adapter = new CourseNoticeAdapter(this, this.courseNoticeViewModel.getNotifications());
        courseNoticeViewModel.getNotifications().observe(this, new Observer<List<DatagramProto.Notification>>() {
            @Override
            public void onChanged(List<DatagramProto.Notification> notifications) {
                adapter.notifyDataSetChanged();
            }
        });
        noticeRecyclerView = findViewById(R.id.recyclerViewInMenbersView);
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