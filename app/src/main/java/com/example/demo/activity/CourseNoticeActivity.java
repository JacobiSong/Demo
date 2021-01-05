package com.example.demo.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.ui.coursenotice.CourseNoticeAdapter;
import com.example.demo.ui.coursenotice.CourseNoticeViewModel;

import org.jetbrains.annotations.NotNull;

public class CourseNoticeActivity extends AppCompatActivity {

    private CourseNoticeViewModel courseNoticeViewModel;
    private CourseNoticeAdapter adapter;
    private RecyclerView noticeRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notice);
        this.courseNoticeViewModel = new ViewModelProvider(this).get(CourseNoticeViewModel.class);
        this.noticeRecyclerView = findViewById(R.id.recyclerViewInMenbersView);
        this.noticeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new CourseNoticeAdapter(this.courseNoticeViewModel.getNoticeList());
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
}