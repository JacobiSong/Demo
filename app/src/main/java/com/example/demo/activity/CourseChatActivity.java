package com.example.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.MessageAdapter;
import com.example.demo.entity.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseChatActivity extends AppCompatActivity {

    private final List<Message> messageList = new ArrayList<>();
    private MessageAdapter adapter = null;
    private RecyclerView msgRecyclerView;

    private static int initIndex = 10000003; // 临时用于messageID自增，连上数据库后可删除该变量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_chat);
        initMsg();
        msgRecyclerView = findViewById(R.id.recyclerViewInCourseView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        this.adapter = new MessageAdapter(this.messageList);
        msgRecyclerView.setAdapter(this.adapter);
        //隐藏系统自带的标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
    }

    //点击发送按钮后发送消息
    public void sendMsg(View v) {
        TextView textView = findViewById(R.id.msgTextView);
        String content = textView.getText().toString(); // 获取消息
        if (!"".equals(content)) { // 消息不为空时发送
            Message sendMessage = new Message(
                    initIndex++,
                    "1100000000",
                    "123456",
                    "你好",
                    LocalDateTime.now()
            );
            this.messageList.add(sendMessage);
            adapter.notifyItemInserted(this.messageList.size() - 1); // 刷新
            msgRecyclerView.scrollToPosition(this.messageList.size() - 1); // 将ListView定位到最后一行
            textView.setText(""); // 清空输入框
        }
    }

    //点击返回按钮，关闭当前Activity
    public void back(View view) {
        this.finish();
    }

    //点击菜单，打开课程菜单
    public void toCourseMenu(View view) {
        Intent intent = new Intent(this, CourseMenuActivity.class);
        startActivity(intent);
    }

    private void initMsg() {
        this.messageList.add(new Message(
                10000001,
                "1188888888",
                "123456",
                "你好",
                LocalDateTime.now()
        ));
        this.messageList.add(new Message(
                10000002,
                "1100000000",
                "123456",
                "你好",
                LocalDateTime.now()
        ));
    }
}