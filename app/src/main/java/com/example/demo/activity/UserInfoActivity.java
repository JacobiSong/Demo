package com.example.demo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.functions.Consumer;

public class UserInfoActivity extends AppCompatActivity {
    private QueryObservable identityObservable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        QueryObservable userObservable = MyApplication.getDatabase().createQuery("user", "select name, phone, email, gender, identity from user where id = ?", id);
        userObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = query.run();
                if (cursor.moveToFirst()) {
                    ((TextView) findViewById(R.id.text_username)).setText(cursor.getString(0));
                    ((TextView) findViewById(R.id.text_phone)).setText(cursor.getString(1));
                    ((TextView) findViewById(R.id.text_email)).setText(cursor.getString(2));
                    int gender = cursor.getInt(3);
                    ((TextView) findViewById(R.id.text_gender)).setText(gender == 0 ? "保密" : gender == 1 ? "女" : "男");
                    int type = cursor.getInt(4);
                    if (type == 0) {
                        identityObservable = MyApplication.getDatabase().createQuery("student", "select department, major, class_no from student where id = ?", id);
                        identityObservable.subscribe(new Consumer<SqlBrite.Query>() {
                            @Override
                            public void accept(SqlBrite.Query query) throws Exception {
                                Cursor c = query.run();
                                if (c.moveToFirst()) {

                                }
                            }
                        });
                    } else if (type == 1) {
                        identityObservable = MyApplication.getDatabase().createQuery("teacher", "select department from teacher where id = ?", id);
                        identityObservable.subscribe(new Consumer<SqlBrite.Query>() {
                            @Override
                            public void accept(SqlBrite.Query query) throws Exception {
                                Cursor c = query.run();
                                if (c.moveToFirst()) {

                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}