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
import com.example.demo.adapter.MsgAdapter;
import com.example.demo.entity.Msg;

import java.util.ArrayList;
import java.util.List;

public class CourseChatActivity extends AppCompatActivity {

    private final List<Msg> msgList = new ArrayList<>();
    private MsgAdapter adapter = null;
    private RecyclerView msgRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_chat);
        initMsg();
        msgRecyclerView = findViewById(R.id.recyclerViewInCourseView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        this.adapter = new MsgAdapter(this.msgList);
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
            Msg sendMsg = new Msg(content, Msg.TYPE_SEND);
            this.msgList.add(sendMsg);
            adapter.notifyItemInserted(this.msgList.size() - 1); // 刷新
            msgRecyclerView.scrollToPosition(this.msgList.size() - 1); // 将ListView定位到最后一行
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
        this.msgList.add(new Msg(
                "你好",
                Msg.TYPE_RECEIVED,
                "张三"
        ));
        this.msgList.add(new Msg(
                "你好lskdf我佩服你我鞥覅沃尔夫我鞥覅级危房为覅围殴覅沃恩发我而烦恼为谔谔谔谔谔谔呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃",
                Msg.TYPE_SEND
        ));
    }
}