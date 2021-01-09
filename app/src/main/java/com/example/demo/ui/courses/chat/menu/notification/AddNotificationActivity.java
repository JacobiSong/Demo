package com.example.demo.ui.courses.chat.menu.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.MyApplication;
import com.example.demo.R;

public class AddNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notification);
    }

    public void sendNotification(View view) {
        final String title = ((EditText) findViewById(R.id.notification_title)).getText().toString();
        final String content = ((EditText) findViewById(R.id.notification_content)).getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
        } else if (content.isEmpty()) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues pushValues = new ContentValues();
            String courseId = getIntent().getStringExtra("id");
            long time = System.currentTimeMillis();
            pushValues.put("type", 2);
            pushValues.put("id1", courseId);
            pushValues.put("time", time);
            ContentValues notificationValues = new ContentValues();
            notificationValues.put("sender_id", MyApplication.getUsername());
            notificationValues.put("receiver_id", courseId);
            notificationValues.put("title", title);
            notificationValues.put("content", content);
            notificationValues.put("time", time);
            int temporary_id = (int) MyApplication.getDatabase().insert("t_push", SQLiteDatabase.CONFLICT_REPLACE, pushValues);
            notificationValues.put("temporary_id", temporary_id);
            MyApplication.getDatabase().insert(courseId + "_n", SQLiteDatabase.CONFLICT_REPLACE, notificationValues);
            MyApplication.getServer().pushAll();
            finish();
        }
    }
}