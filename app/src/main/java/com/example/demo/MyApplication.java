package com.example.demo;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.demo.service.ConnectService;
import com.squareup.sqlbrite3.BriteDatabase;

public class MyApplication extends Application {
    private enum Configuration {
        INSTANCE;
        private MyApplication mContext = null;
        private BriteDatabase database = null;
        private String username = "";
        private ConnectService.MyBinder server = null;
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Configuration.INSTANCE.server = (ConnectService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.INSTANCE.mContext = this;
        bindService(new Intent(this, ConnectService.class), serviceConnection, BIND_AUTO_CREATE);
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
}
