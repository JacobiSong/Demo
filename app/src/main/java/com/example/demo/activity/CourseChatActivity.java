package com.example.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.entity.Message;
import com.example.demo.ui.coursechat.CourseChatViewModel;
import com.example.demo.ui.coursechat.MessageAdapter;

import java.time.LocalDateTime;
import java.util.List;

public class CourseChatActivity extends AppCompatActivity {

    private CourseChatViewModel courseChatViewModel;
    private MessageAdapter adapter = null;
    private RecyclerView msgRecyclerView;

    private static int initIndex = 10000003; // 临时用于messageID自增，连上数据库后可删除该变量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_chat);

        courseChatViewModel = new ViewModelProvider(this).get(CourseChatViewModel.class);
        msgRecyclerView = findViewById(R.id.recyclerViewInCourseView);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new MessageAdapter(this.courseChatViewModel.getMessageList());
        msgRecyclerView.setAdapter(this.adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.courses_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_cart://监听菜单按钮
                Intent intent = new Intent(this, CourseMenuActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //点击头像后跳转到用户信息
    public void toUserInfo(View view) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }

    //点击发送按钮后发送消息
    public void sendMsg(View v) {
        TextView textView = findViewById(R.id.msgTextView);
        String content = textView.getText().toString(); // 获取消息
        System.out.println("消息内容为" + content);
        if (!"".equals(content)) { // 消息不为空时发送
            Message sendMessage = new Message(
                    initIndex++,
                    "1100000000",
                    "123456",
                    content,
                    LocalDateTime.now()
            );
            List<Message> messageList = this.courseChatViewModel.getMessageList();
            messageList.add(sendMessage);
            adapter.notifyItemInserted(messageList.size() - 1); // 刷新
            msgRecyclerView.scrollToPosition(messageList.size() - 1); // 将ListView定位到最后一行
            textView.setText(""); // 清空输入框
        }
    }
}