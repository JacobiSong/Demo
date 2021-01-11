package com.example.demo;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.demo.service.ConnectService;
import com.example.demo.ui.LaunchActivity;
import com.squareup.sqlbrite3.BriteDatabase;

public class MyApplication extends Application {
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.example.demo.logout".equals(action)) {
                logout();
            }
            else if ("com.example.demo.toast".equals(action)) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra("text"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();

    private void logout() {
        Configuration.INSTANCE.clear();
        SharedPreferences.Editor editor = getSharedPreferences("last_login", MODE_PRIVATE).edit();
        editor.putString("username", "");
        editor.apply();
        startActivity(new Intent(getApplicationContext(), LaunchActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private enum Configuration {
        INSTANCE;
        private MyApplication mContext = null;
        private BriteDatabase database = null;
        private String username = "";
        private ConnectService.MyBinder server = null;

        public void clear() {
            database.close();
            database = null;
            username = "";
            server = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.INSTANCE.mContext = this;
        startService(new Intent(this, ConnectService.class));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.demo.logout");
        intentFilter.addAction("com.example.demo.toast");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public static MyApplication getInstance() {
        if (Configuration.INSTANCE.mContext == null) {
            return new MyApplication();
        }
        return Configuration.INSTANCE.mContext;
    }

    public static void setDatabase(BriteDatabase database) {
        Configuration.INSTANCE.database = database;
    }

    public static ConnectService.MyBinder getServer() {
        return Configuration.INSTANCE.server;
    }

    public static void setUsername(String username) {
        Configuration.INSTANCE.username = username;
    }

    public static BriteDatabase getDatabase() {
        return Configuration.INSTANCE.database;
    }

    public static String getUsername() {
        return Configuration.INSTANCE.username;
    }

    public static void setServer(ConnectService.MyBinder binder) {
        Configuration.INSTANCE.server = binder;
    }
}
