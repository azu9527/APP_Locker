package com.example.app_locker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class locker extends Activity{

    public void onCreate(Bundle savedInstanceState) {
       // overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locker);

    }
    public void button7click(View view){
        String pass = ((EditText)findViewById(R.id.editText7)).getText().toString().trim();
        SharedPreferences.Editor editor =MainActivity.sharedPreferences1.edit();
        if(pass.equals(MainActivity.sharedPreferences.getString("password",""))){
            editor.putBoolean(MyService.packagen,false);
            editor.commit();
            finish();
        }else {
            Toast.makeText(this,"wrong",Toast.LENGTH_LONG).show();
        }
    }
    public void button9click(View view){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
