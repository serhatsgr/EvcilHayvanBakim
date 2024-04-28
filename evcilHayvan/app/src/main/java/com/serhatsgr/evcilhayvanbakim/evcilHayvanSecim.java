package com.serhatsgr.evcilhayvanbakim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class evcilHayvanSecim extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evcil_hayvan_secim);

    }


    public void hayvan(View view){
        Intent intent=new Intent(this,bakimAna.class);
        startActivity(intent);
    }
}