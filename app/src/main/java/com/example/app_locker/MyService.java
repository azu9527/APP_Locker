package com.example.app_locker;

import android.app.ActivityManager;


import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;


import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;

import android.os.Build;

import android.os.IBinder;


import com.example.app_locker.Service.MysecondService;

import java.util.List;

public class MyService extends Service {
    public  ActivityManager am;
    Thread thread;
    Intent intent1;
    boolean f;
    static String packagen;
    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate() {
        super.onCreate();
        f=true;
        packagen=new String("");
        am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        sharedPreferences1 =getApplicationContext().getSharedPreferences("app",0);
        editor =sharedPreferences1.edit();
        intent1 = new Intent(MyService.this, locker.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread=new Thread(){
            @Override
            public void run(){
                super.run();
                while (f){
                    if((sharedPreferences1.getBoolean(getLauncherTopApp(MyService.this,am),false))){
                        packagen=getLauncherTopApp(MyService.this,am);
                       // intent1.putExtra("packagename",packagen);
                        startActivity(intent1);
                    }
                    if(getLauncherTopApp(MyService.this,am).equals(getLauncherPackageName(MyService.this))){

                        if(packagen.equals("")){

                        }else {
                            editor.putBoolean(packagen,true);
                            editor.commit();
                        }
                        continue;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
       // return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        f = false;
        //Intent sevice = new Intent(this, MyService.class);
       // this.startService(sevice);
        //startService(new Intent(this, MyService.class));
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String getLauncherTopApp(Context context, ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }

    public  String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }
}
