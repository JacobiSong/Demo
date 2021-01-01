package com.example.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class CoreService extends Service {
    class SwapTask extends TimerTask {
        @Override
        public void run() {

        }
    }

    private Timer timer;
    public CoreService() {
        Timer timer = new Timer();
        timer.schedule(new SwapTask(), 0, 60000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void swapData() {

    }

    @Override
    public boolean stopService(Intent name) {
        timer.cancel();
        timer.purge();
        return super.stopService(name);
    }
}