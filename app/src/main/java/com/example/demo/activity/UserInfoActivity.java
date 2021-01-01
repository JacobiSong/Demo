package com.example.demo.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.R;
import com.example.demo.entity.Student;

public class UserInfoActivity extends AppCompatActivity {

    //临时数据
    private Student student;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initStudent();
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

    private void initView() {
        ((TextView) findViewById(R.id.text_username)).setText(this.student.getName());
        ((TextView) findViewById(R.id.text_gender))
                .setText(this.student.getGender() == 0 ? "男" : "女");
        ((TextView) findViewById(R.id.text_phone)).setText(this.student.getPhone());
        ((TextView) findViewById(R.id.text_email)).setText(this.student.getEmail());
        ((TextView) findViewById(R.id.text_department)).setText(this.student.getDepartment());
    }

    private void initStudent() {
        this.student = new Student(
                "123456234",
                "小明",
                "13999999999",
                "xxx@gmail.com",
                0,
                "qwer1234",
                "计算机科学与技术学院",
                "软件工程",
                11111
        );
    }
}