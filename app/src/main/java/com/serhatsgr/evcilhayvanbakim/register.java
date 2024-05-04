package com.serhatsgr.evcilhayvanbakim;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {

    TextInputEditText EditTextMail, EditTextPassword, EditTextRePassword;
    Button registerbtn;

    TextView sigIn;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditTextMail = findViewById(R.id.mail);
        EditTextPassword = findViewById(R.id.password);
        EditTextRePassword = findViewById(R.id.repassword); // EditTextRePassword'ı da başlatın
        sigIn = findViewById(R.id.others);
        registerbtn = findViewById(R.id.btnRegister);

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
                String email, password, repassword;

                email = String.valueOf(EditTextMail.getText());
                password = String.valueOf(EditTextPassword.getText());
                repassword = String.valueOf(EditTextRePassword.getText());

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
                                    Toast.makeText(register.this, "Kayıt başarılı. Giriş Yap!.", Toast.LENGTH_SHORT).show();
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

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.y = 200; // Y ekseninde kaydırma miktarı
        getWindow().setAttributes(params);
    }


    }
