package com.example.demo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.demo.MyApplication;
import com.example.demo.R;

public class RegisterActivity extends AppCompatActivity {

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            success();
        }
    }

    private final RegisterActivity.MyBroadcastReceiver broadcastReceiver = new RegisterActivity.MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void register(View view) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.demo.register");
        registerReceiver(broadcastReceiver, intentFilter);
        String username = ((EditText)findViewById(R.id.editTextTexEmailAddressRegister)).getText().toString();
        String password = ((EditText)findViewById(R.id.editTextTextPasswordRegister)).getText().toString();
        int identity = ((RadioGroup)findViewById(R.id.radioGroup)).getCheckedRadioButtonId() == R.id.radioButtonTeacherMail ? 1 : 0;
        MyApplication.getServer().register(username, password, identity);
    }

    private void success() {
        unregisterReceiver(broadcastReceiver);
        this.finish();
    }
}