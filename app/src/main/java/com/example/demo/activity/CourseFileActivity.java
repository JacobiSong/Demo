package com.example.demo.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.CourseFileAdapter;
import com.example.demo.entity.File;

import java.util.ArrayList;
import java.util.List;

public class CourseFileActivity extends AppCompatActivity {

    private final List<File> fileList = new ArrayList<>();
    private CourseFileAdapter adapter;
    private RecyclerView fileRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_file);
        initFile();
        fileRecyclerView = findViewById(R.id.recyclerViewInFileView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        fileRecyclerView.setLayoutManager(linearLayoutManager);
        this.adapter = new CourseFileAdapter(this.fileList);
        fileRecyclerView.setAdapter(this.adapter);
        //隐藏系统自带标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
    }

    public void back(View view) {
        this.finish();
    }

    private void initFile() {
        this.fileList.add(new File(
                "作业1.pdf",
                "张老师",
                78.6
        ));
        this.fileList.add(new File(
                "单元测评2.docx",
                "张老师",
                107.3
        ));
    }
}