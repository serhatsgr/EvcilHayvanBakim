package com.serhatsgr.evcilhayvanbakim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.y = 200; // Y ekseninde kaydırma miktarı
        getWindow().setAttributes(params);
    }

    public void register(View view){
        Intent intent=new Intent(this,login.class);
        startActivity(intent);

    }
}