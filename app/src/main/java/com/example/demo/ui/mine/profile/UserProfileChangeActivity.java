package com.example.demo.ui.mine.profile;

import android.content.Context;
import android.os.Bundle;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.R;

public class UserProfileChangeActivity extends AppCompatActivity {

    private String action;
    private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_change);
        editor = findViewById(R.id.user_profile_editor);
        UserProfileItem secret = findViewById(R.id.secret_button);
        UserProfileItem female = findViewById(R.id.female_button);
        UserProfileItem male = findViewById(R.id.male_button);
        action = getIntent().getAction();
        String text = getIntent().getStringExtra("text");
        if ("com.example.demo.user.change.gender".equals(action)) {
            setTitle("更改性别");
            secret.clearImage();
            female.clearImage();
            male.clearImage();
            findViewById(R.id.old_password_linear).setVisibility(View.GONE);
            findViewById(R.id.new_password_linear).setVisibility(View.GONE);
            editor.setVisibility(View.GONE);
            if ("保密".equals(text)) {
                secret.setImage(R.drawable.check_24);
            } else if ("女".equals(text)) {
                female.setImage(R.drawable.check_24);
            } else if ("男".equals(text)) {
                male.setImage(R.drawable.check_24);
            }
            secret.setOnClickListener(v -> {
                female.clearImage();
                male.clearImage();
                secret.setImage(R.drawable.check_24);
                editor.setText("保密");
            });
            female.setOnClickListener(v -> {
                secret.clearImage();
                male.clearImage();
                female.setImage(R.drawable.check_24);
                editor.setText("女");
            });
            male.setOnClickListener(v -> {
                secret.clearImage();
                female.clearImage();
                male.setImage(R.drawable.check_24);
                editor.setText("男");
            });
        } else if("com.example.demo.user.change.password".equals(action)) {
            secret.setVisibility(View.GONE);
            female.setVisibility(View.GONE);
            male.setVisibility(View.GONE);
            editor.setVisibility(View.GONE);
        } else {
            findViewById(R.id.old_password_linear).setVisibility(View.GONE);
            findViewById(R.id.new_password_linear).setVisibility(View.GONE);
            secret.setVisibility(View.GONE);
            female.setVisibility(View.GONE);
            male.setVisibility(View.GONE);
            editor.setText(text);
            if ("com.example.demo.user.change.phone".equals(action)) {
                setTitle("更改手机号");
            } else if ("com.example.demo.user.change.email".equals(action)) {
                setTitle("更改邮箱");
            } else if ("com.example.demo.user.change.department".equals(action)) {
                setTitle("更改院系");
            } else if ("com.example.demo.user.change.major".equals(action)) {
                setTitle("更改手机号");
            } else if ("com.example.demo.user.change.class_no".equals(action)) {
                setTitle("更改班级");
            }
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_change_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_change:
                if ("com.example.demo.user.change.password".equals(action)) {
                    if (getSharedPreferences("user_" + MyApplication.getUsername(), MODE_PRIVATE).getString("password", "").equals(
                            ((EditText) findViewById(R.id.old_password)).getText().toString()
                    )) {
                        MyApplication.getServer().changeUserProfile(action, ((EditText) findViewById(R.id.new_password)).getText().toString());
                    } else {
                        Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    MyApplication.getServer().changeUserProfile(action, editor.getText().toString());
                }
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}