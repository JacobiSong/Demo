package com.example.demo;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.example.demo.service.ConnectService;
import com.example.demo.ui.LoginActivity;
import com.example.demo.ui.MainActivity;
import com.example.demo.utils.DatabaseHelper;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.schedulers.Schedulers;

public class MyApplication extends Application {
    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            logout();
        }
    }

    private final MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();

    private void logout() {
        Configuration.INSTANCE.clear();
        SharedPreferences.Editor editor = getSharedPreferences("last_login", MODE_PRIVATE).edit();
        editor.putString("username", "");
        editor.apply();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Configuration.INSTANCE.server = (ConnectService.MyBinder) service;
            String username = getSharedPreferences("last_login", MODE_PRIVATE).getString("username", "");
            if (!username.isEmpty()) {
                Configuration.INSTANCE.username = username;
                Configuration.INSTANCE.database = new SqlBrite.Builder().build().wrapDatabaseHelper(new DatabaseHelper(username, 1).getSupportSQLiteOpenHelper(), Schedulers.io());
                SharedPreferences userSp = getSharedPreferences("user_" + username, MODE_PRIVATE);
                MyApplication.getServer().login(userSp.getString("ip", "140.143.6.64"), userSp.getInt("port", 8888), username,
                        userSp.getString("password", ""), userSp.getInt("identity", 0), userSp.getLong("db_version", 0));
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.demo.login");
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
}
