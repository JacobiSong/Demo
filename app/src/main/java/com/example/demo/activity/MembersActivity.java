package com.example.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.MemberAdapter;
import com.example.demo.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class MembersActivity extends AppCompatActivity {

    private List<Student> studentList;
    private MemberAdapter adapter;
    private RecyclerView memberRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        initStudents();
        initMembers();
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

    public void toUserInfo(View view) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }

    private void initMembers() {
        memberRecyclerView = findViewById(R.id.recyclerViewInMenbersView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        memberRecyclerView.setLayoutManager(linearLayoutManager);
        this.adapter = new MemberAdapter(this.studentList);
        memberRecyclerView.setAdapter(this.adapter);
    }

    private void initStudents() {
        this.studentList = new ArrayList<>();
        this.studentList.add(new Student(
                "123456234",
                "小明",
                "13999999999",
                "xxx@gmail.com",
                0,
                "qwer1234",
                "计算机科学与技术学院",
                "软件工程",
                11111
        ));
    }
}