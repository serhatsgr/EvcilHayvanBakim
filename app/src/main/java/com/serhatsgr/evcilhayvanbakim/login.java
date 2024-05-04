package com.serhatsgr.evcilhayvanbakim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    TextInputEditText EditTextMail, EditTextPassword;
    Button loginbtn;

    TextView others;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditTextMail=findViewById(R.id.mail);
        EditTextPassword=findViewById(R.id.password);
        others=findViewById(R.id.others);
        loginbtn=findViewById(R.id.loginbtn);

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this, register.class);
                startActivity(intent);
                finish();
            }
        });

       loginbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email,password;
               email=String.valueOf(EditTextMail.getText());
               password=String.valueOf(EditTextPassword.getText());

               if(TextUtils.isEmpty(email)) {
                   Toast.makeText(login.this, "Mail Giriniz.", Toast.LENGTH_SHORT).show();
                   return;
               }

               if(TextUtils.isEmpty(password)){
                   Toast.makeText(login.this, "Sifre Giriniz.", Toast.LENGTH_SHORT).show();
                   return;
               }

               firebaseAuth.signInWithEmailAndPassword(email,password)
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   Intent intent=new Intent(login.this, evcilHayvanSecim.class);
                                   startActivity(intent);
                                   finish();
                               }
                               else{
                                   Toast.makeText(login.this, "Mail veya şifre hatalı!.", Toast.LENGTH_SHORT).show();
                               }




                           }
                       });


           }
       });

    }

}