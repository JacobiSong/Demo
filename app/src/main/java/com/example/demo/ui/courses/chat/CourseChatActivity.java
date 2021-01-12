package com.example.demo.ui.courses.chat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.ui.courses.chat.menu.CourseMenuActivity;

public class CourseChatActivity extends AppCompatActivity {

    private MessageAdapter adapter;
    private String courseId;
    private CourseChatViewModel courseChatViewModel;
    private RecyclerView msgRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("name"));
        courseId = getIntent().getStringExtra("id");
        courseChatViewModel = new CourseChatViewModel(courseId);
        adapter = new MessageAdapter(this, courseChatViewModel.getMessages());

        courseChatViewModel.getMessages().observe(this, messages -> {
            adapter.notifyDataSetChanged();
            msgRecyclerView.scrollToPosition(courseChatViewModel.getMessages().getValue().size() - 1);
        });
        setContentView(R.layout.activity_course_chat);
        msgRecyclerView = findViewById(R.id.recyclerViewInCourseView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        msgRecyclerView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.courses_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_cart://监听菜单按钮
                startActivity(new Intent(this, CourseMenuActivity.class)
                        .putExtra("name", getIntent().getStringExtra("name"))
                        .putExtra("id", getIntent().getStringExtra("id")));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //点击发送按钮后发送消息
    public void sendMsg(View v) {
        TextView textView = findViewById(R.id.msgTextView);
        String content = textView.getText().toString(); // 获取消息
        if (!"".equals(content)) { // 消息不为空时发送
            ContentValues pushValues = new ContentValues();
            long time = System.currentTimeMillis();
            pushValues.put("type", 1);
            pushValues.put("id1", courseId);
            pushValues.put("time", time);
            ContentValues messageValues = new ContentValues();
            messageValues.put("sender_id", MyApplication.getUsername());
            messageValues.put("receiver_id", courseId);
            messageValues.put("content", content);
            messageValues.put("time", time);
            int temporary_id = (int) MyApplication.getDatabase().insert("t_push", SQLiteDatabase.CONFLICT_REPLACE, pushValues);
            messageValues.put("temporary_id", temporary_id);
            MyApplication.getDatabase().insert(courseId + "_m", SQLiteDatabase.CONFLICT_REPLACE, messageValues);
            textView.setText(""); // 清空输入框
            MyApplication.getServer().pushAll();
        } else {
            Toast.makeText(this, "消息不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}