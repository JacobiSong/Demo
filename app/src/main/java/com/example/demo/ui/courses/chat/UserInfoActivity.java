package com.example.demo.ui.courses.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.service.ClientHandler;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.functions.Consumer;

public class UserInfoActivity extends AppCompatActivity {
    private QueryObservable identityObservable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("群成员信息");
        setContentView(R.layout.activity_user_info);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        QueryObservable userObservable = MyApplication.getDatabase().createQuery("user", "select name, phone, email, gender, identity from user where id = ?", id);
        userObservable.subscribe(query -> {
            Cursor cursor = query.run();
            if (cursor.moveToFirst()) {
                int gender = cursor.getInt(3);
                int type = cursor.getInt(4);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.text_userId)).setText(id);
                        ((TextView) findViewById(R.id.text_username)).setText(cursor.getString(0));
                        ((TextView) findViewById(R.id.text_phone)).setText(cursor.getString(1));
                        ((TextView) findViewById(R.id.text_email)).setText(cursor.getString(2));
                        ((TextView) findViewById(R.id.text_gender)).setText(gender == 0 ? "保密" : gender == 1 ? "女" : "男");
                        ((TextView) findViewById(R.id.text_identity)).setText(type == 0 ? "学生" : "教师");
                    }
                });
                if (type == 0) {
                    identityObservable = MyApplication.getDatabase().createQuery("student", "select department, major, class_no from student where id = ?", id);
                    identityObservable.subscribe(new Consumer<SqlBrite.Query>() {
                        @Override
                        public void accept(SqlBrite.Query query) throws Exception {
                            Cursor c = query.run();
                            if (c.moveToFirst()) {
                                runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              ((TextView) findViewById(R.id.text_department)).setText(c.getString(0));
                                              ((TextView) findViewById(R.id.text_major)).setText(c.getString(1));
                                              ((TextView) findViewById(R.id.text_classNo)).setText(c.getString(2));
                                          }
                                    }
                                );
                            }
                        }
                    });
                } else if (type == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) findViewById(R.id.text_title_major)).setVisibility(View.GONE);
                            ((TextView) findViewById(R.id.text_title_classNo)).setVisibility(View.GONE);
                            ((TextView) findViewById(R.id.text_major)).setVisibility(View.GONE);
                            ((TextView) findViewById(R.id.text_classNo)).setVisibility(View.GONE);
                            identityObservable = MyApplication.getDatabase().createQuery("teacher", "select department from teacher where id = ?", id);
                            identityObservable.subscribe(new Consumer<SqlBrite.Query>() {
                                @Override
                                public void accept(SqlBrite.Query query) throws Exception {
                                    Cursor c = query.run();
                                    if (c.moveToFirst()) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((TextView) findViewById(R.id.text_department)).setText(c.getString(0));
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        MyApplication.getServer().getUserInfo(id);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}