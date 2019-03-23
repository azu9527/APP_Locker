package com.example.app_locker;


import android.app.ActivityManager;
import android.app.AppOpsManager;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import android.os.Build;

import android.provider.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public static SharedPreferences sharedPreferences;
    public static SharedPreferences sharedPreferences1;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!hasPermission()){
            startActionUsageAccessSettings(this);
        }
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("app", Context.MODE_PRIVATE);
        //Intent i = new Intent(this, MyService .class);
        //i.setAction(ACTION_START);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startService(i);
        startService(new Intent(this,MyService.class));
        //startForegroundService(i);
        if(sharedPreferences.getString("password","").equals("")){
            setContentView(R.layout.setpwd);
            putAppinfo();
        }else {
            setContentView(R.layout.activity_main);
        }
    }

    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public static void startActionUsageAccessSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
    }


    public void putAppinfo() {
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
            {
                editor.putBoolean(packageInfo.packageName,false);
            }
        }
        editor.commit();
    }

    public ArrayList<AppInfo> getAppinfo() {
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
       // SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo =new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            tmpInfo.packageName = packageInfo.packageName;
            //tmpInfo.versionName = packageInfo.versionName;
            //tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
           if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
           {
                appList.add(tmpInfo);
           }
        }
        return appList;
    }


    public void List(ArrayList<AppInfo> list){
        adapter = new Adapter(getBaseContext(), R.layout.list,list);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

    }

    public void buttonclick(View view){
        try {
            EditText editText2 =(EditText)findViewById(R.id.editText2);
            if(editText2.getText().toString().equals(sharedPreferences.getString("password",""))){
                ArrayList<AppInfo> list=getAppinfo();
                setContentView(R.layout.lock_selector);
                List(list);
            }else {
                Toast.makeText(this,"Wrong",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void button4click(View view){
        try{
            EditText editText = (EditText)findViewById(R.id.editText5);
            if(sharedPreferences.getString("answer","").equals(editText.getText().toString().trim())){
                setContentView(R.layout.changepwd);
            }else {
                Toast.makeText(this,"Wrong",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void button3click(View view){
        try{
            EditText editText = (EditText)findViewById(R.id.editText4);
            EditText editText1 =(EditText)findViewById(R.id.editText6);
            if(TextUtils.isEmpty(editText.getText())){
                Toast.makeText(this,"Wrong",Toast.LENGTH_LONG).show();
            }else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("question",editText1.getText().toString().trim());
                editor.putString("answer",editText.getText().toString().trim());
                editor.commit();
                setContentView(R.layout.seting);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void button2click(View view){
        try {
            EditText editText = (EditText)findViewById(R.id.editText);
            EditText editText3 = (EditText)findViewById(R.id.editText3);
            if(TextUtils.isEmpty(editText.getText())|TextUtils.isEmpty(editText3.getText())){
                Toast.makeText(this,"Wrong",Toast.LENGTH_LONG).show();
            }else if(!editText.getText().toString().equals(editText3.getText().toString())){
                Toast.makeText(this,"Wrong",Toast.LENGTH_LONG).show();
            }else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password",editText.getText().toString());
                editor.commit();
                setContentView(R.layout.setquestion);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void  button11click(View view){
        EditText editText =(EditText)findViewById(R.id.editText10);
        EditText editText1 =(EditText)findViewById(R.id.editText11);
        if(TextUtils.isEmpty(editText.getText())|TextUtils.isEmpty(editText1.getText())) {
            Toast.makeText(this, "Wrong", Toast.LENGTH_LONG).show();
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("question",editText.getText().toString().trim());
            editor.putString("answer",editText1.getText().toString().trim());
            editor.commit();
            setContentView(R.layout.lock_selector);
            ArrayList<AppInfo> list=getAppinfo();
            List(list);
        }
    }

    public void button8click(View view){
        ListView list= (ListView)findViewById(R.id.list);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        String name = ((AppInfo)adapter.getItem(list.getPositionForView(view))).packageName;
        if(sharedPreferences1.getBoolean(name,false)){
            editor.putBoolean(name,false);
            view.setBackgroundResource(R.drawable.button1);
        }else {
            editor.putBoolean(name,true);
            view.setBackgroundResource(R.drawable.button);
        }
        editor.commit();
    }

    public void button5click(View view){
        setContentView(R.layout.seting);
        Button button =(Button)findViewById(R.id.button14);
        if(sharedPreferences.getBoolean("switch",true)){
            button.setBackgroundResource(R.drawable.button);
        } else {
            button.setBackgroundResource(R.drawable.button1);
        }
    }
    public void button6click(View view){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
        finish();
    }
    public void button12click(View view){
        setContentView(R.layout.lock_selector);
        ArrayList<AppInfo> list=getAppinfo();
        List(list);
    }
    public void button14click(View view){
        Button button = (Button)findViewById(R.id.button14);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean("switch",true)){
            editor.putBoolean("switch",false);
            editor.commit();
            button.setBackgroundResource(R.drawable.button1);
            stopService(new Intent(this,MyService.class));
        }else {
            editor.putBoolean("switch",true);
            editor.commit();
            button.setBackgroundResource(R.drawable.button);
            startService(new Intent(this,MyService.class));
        }


    }
    public void  button13click(View view){
        setContentView(R.layout.changepwd);
    }
    public void  button15click(View view){
        setContentView(R.layout.changequestion);
    }
    public void  button10click(View view){
        EditText editText = (EditText)findViewById(R.id.editText8);
        EditText editText3 = (EditText)findViewById(R.id.editText9);
        if(TextUtils.isEmpty(editText.getText())|TextUtils.isEmpty(editText3.getText())){
            Toast.makeText(this,"Wrong",Toast.LENGTH_LONG).show();
        }else if(!editText.getText().toString().equals(editText3.getText().toString())){
            Toast.makeText(this,"Wrong",Toast.LENGTH_LONG).show();
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password",editText.getText().toString());
            editor.commit();
            setContentView(R.layout.seting);
        }
    }
    public void button16click(View view){
        setContentView(R.layout.forgetpwd);
        TextView textView = (TextView)findViewById(R.id.textView);
        String question = sharedPreferences.getString("question","");
        textView.setText(question);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

