package com.example.demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.demo.R;
import com.example.demo.ui.notifications.notification.NotificationFragment;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.notification_container, NotificationFragment.newInstance())
                    .commitNow();
        }
    }
}