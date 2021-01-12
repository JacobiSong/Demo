package com.example.demo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.utils.DatabaseHelper;
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            login();
        }
    }

    private final MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void jumpToMenu(View view) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.demo.login");
        registerReceiver(broadcastReceiver, intentFilter);
        username = ((EditText)findViewById(R.id.editTextTextEmailAddress)).getText().toString();
        String password = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();
        int identity = ((RadioGroup)findViewById(R.id.radioGroup)).getCheckedRadioButtonId() == R.id.radioButtonTeacherMail ? 1 : 0;
        SharedPreferences userSp = getSharedPreferences("user_" + username, MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString("password", password);
        editor.putInt("identity", identity);
        editor.apply();
        MyApplication.getServer().login(username, password, identity, userSp.getLong("db_version", 0));
    }

    public void login() {
        unregisterReceiver(broadcastReceiver);
        SharedPreferences sp = getSharedPreferences("last_login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.apply();
        MyApplication.setUsername(username);
        MyApplication.setDatabase(new SqlBrite.Builder().build().wrapDatabaseHelper(new DatabaseHelper(username, 1).getSupportSQLiteOpenHelper(), Schedulers.io()));
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void jumpToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void jumpToSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}