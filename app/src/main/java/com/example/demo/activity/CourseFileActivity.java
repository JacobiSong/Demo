package com.example.demo.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.CourseFileAdapter;

import java.util.ArrayList;

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