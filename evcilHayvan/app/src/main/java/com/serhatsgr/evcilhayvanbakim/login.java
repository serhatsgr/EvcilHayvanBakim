package com.serhatsgr.evcilhayvanbakim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void giris(View view){
        Intent intent=new Intent(this,evcilHayvanSecim.class);
        startActivity(intent);
    }

    public void tikla(View view){
        Intent intent=new Intent(this,register.class);
        startActivity(intent);
    }
}