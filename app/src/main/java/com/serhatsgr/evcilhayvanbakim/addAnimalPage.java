package com.serhatsgr.evcilhayvanbakim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class addAnimalPage extends AppCompatActivity {

   private TextInputEditText textName,textType,textRace,textGender,textWeight,textBirthDate, textAdoptionDate;

   private Button btnSave;

   private FirebaseAuth mAuth;
   private FirebaseUser mUser;

   private HashMap <String,Object> mData;
   private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evcil_hayvan_ekleme);

        textName=findViewById(R.id.name);
        textType=findViewById(R.id.type);
        textRace=findViewById(R.id.race);
        textGender=findViewById(R.id.gender);
        textWeight=findViewById(R.id.weight);
        textBirthDate=findViewById(R.id.birthDate);
        textAdoptionDate=findViewById(R.id.adoptionDate);
        btnSave=findViewById(R.id.buttonSavePet);
        mAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String name,type,race,gender,weight,birthDate,adoptionDate,buttonSavePet;
              name=String.valueOf(textName.getText());
              type=String.valueOf(textType.getText());
              race=String.valueOf(textRace.getText());
              gender=String.valueOf(textGender.getText());
              weight=String.valueOf(textWeight.getText());
              birthDate=String.valueOf(textBirthDate.getText());
              adoptionDate=String.valueOf(textAdoptionDate.getText());

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(addAnimalPage.this, "İsim Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(type)) {
                    Toast.makeText(addAnimalPage.this, " Tür Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(race)) {
                    Toast.makeText(addAnimalPage.this, "Irk Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(addAnimalPage.this, "Cinsiyet Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(weight)) {
                    Toast.makeText(addAnimalPage.this, "Kilo Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(birthDate)) {
                    Toast.makeText(addAnimalPage.this, "Doğum Tarihi Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(adoptionDate)) {
                    Toast.makeText(addAnimalPage.this, "Sahiplenme Tarihi Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mUser=mAuth.getCurrentUser();
                if(mUser!=null){
                    mFireStore.collection("ani")
                }

            }
        });


    }
}