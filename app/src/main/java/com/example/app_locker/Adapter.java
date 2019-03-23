package com.example.app_locker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class Adapter extends ArrayAdapter {
    private final int resourceId;

    public Adapter(Context context, int textViewResourceId, ArrayList<AppInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppInfo appInfo = (AppInfo) getItem(position);
         View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
         ImageView Appicon = (ImageView) view.findViewById(R.id.imageView4);
         TextView Appname = (TextView) view.findViewById(R.id.textView5);
         Button Switch = (Button)view.findViewById(R.id.button8);
         Appicon.setBackground(appInfo.appIcon);
         Appname.setText(appInfo.appName);

         if(MainActivity.sharedPreferences1.getBoolean(appInfo.packageName,false)){
             Switch.setBackgroundResource(R.drawable.button);
         }
         else {
             Switch.setBackgroundResource(R.drawable.button1);
         }
        return view;
    }
}
