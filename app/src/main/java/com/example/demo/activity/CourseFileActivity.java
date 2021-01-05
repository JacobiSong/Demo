package com.example.demo.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.ui.coursefile.CourseFileAdapter;
import com.example.demo.ui.coursefile.CourseFileViewModel;

public class CourseFileActivity extends AppCompatActivity {

    private CourseFileViewModel courseFileViewModel;
    private CourseFileAdapter adapter;
    private RecyclerView fileRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_file);

        this.courseFileViewModel = new ViewModelProvider(this).get(CourseFileViewModel.class);
        fileRecyclerView = findViewById(R.id.recyclerViewInFileView);
        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new CourseFileAdapter(this.courseFileViewModel.getFiles());
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

}