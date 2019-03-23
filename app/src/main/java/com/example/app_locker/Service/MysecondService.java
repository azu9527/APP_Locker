package com.example.app_locker.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.app_locker.MyService;

public class MysecondService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService(new Intent(this, MyService.class));
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
