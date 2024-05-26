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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class register extends AppCompatActivity {

    TextInputEditText EditTextMail, EditTextPassword, EditTextRePassword, EditTextName;
    Button registerbtn;

    TextView sigIn;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private FirebaseAuth mAuth;

    private FirebaseUser mUsers;

    private HashMap<String, Object>  mData;

    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditTextName=findViewById(R.id.Username);
        EditTextMail = findViewById(R.id.mail);
        EditTextPassword = findViewById(R.id.password);
        EditTextRePassword = findViewById(R.id.repassword);
        sigIn = findViewById(R.id.others);
        registerbtn = findViewById(R.id.btnRegister);

        mAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();

        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  email, password, repassword,name;
                email = String.valueOf(EditTextMail.getText());
                password = String.valueOf(EditTextPassword.getText());
                repassword = String.valueOf(EditTextRePassword.getText());
                name=String.valueOf(EditTextName.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(register.this, "Mail Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(register.this, "Sifre Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repassword)) {
                    Toast.makeText(register.this, "Şifreler aynı değil, kontrol ediniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    mUsers=mAuth.getCurrentUser();

                                    mData=new HashMap<>();
                                    mData.put("UserName",name);
                                    mData.put("email",email);
                                    mData.put("password",password);
                                    mData.put("UserID",mUsers.getUid());

                                    mFireStore.collection("Users").document(mUsers.getUid())
                                            .set(mData)
                                            .addOnCompleteListener(register.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                        Toast.makeText(register.this, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Toast.makeText(register.this, "Kayıt Başarısız!", Toast.LENGTH_SHORT).show();
                                                }

                                            });


                                    Intent intent = new Intent(register.this, login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(register.this, "Kayıt Başarısız!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }


    }
