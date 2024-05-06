package com.serhatsgr.evcilhayvanbakim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class vaccinesPage extends AppCompatActivity {

    private TextInputEditText etVaccineName, etVaccineDate;
    private Button btnAddVaccine;
    private ListView lvVaccines;
    private List<String> vaccineList;
    private ArrayAdapter<String> vaccineAdapter;

    private FirebaseAuth mAuth;

    private FirebaseUser mUsers;

    private HashMap<String, Object> mData;

    private FirebaseFirestore mFireStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines_page);

        etVaccineName=findViewById(R.id.etVaccineName);
        etVaccineDate=findViewById(R.id.etVaccineDate);
        btnAddVaccine=findViewById(R.id.etVaccineDate);

        mAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();

        btnAddVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String vaccineName,date;

               vaccineName=String.valueOf(etVaccineDate.getText());
               date=String.valueOf(etVaccineDate.getText());

               if(TextUtils.isEmpty(vaccineName)){
                   Toast.makeText(vaccinesPage.this, "Egzersiz Türü Giriniz.", Toast.LENGTH_SHORT).show();
               }
               if(TextUtils.isEmpty(date)){
                   Toast.makeText(vaccinesPage.this,"Aşı olma tarihini giriniz!",Toast.LENGTH_SHORT).show();
               }

               mUsers=mAuth.getCurrentUser();
               if(mUsers!=null){
                 String userId=mUsers.getUid();

                 mFireStore.collection("vaccines")
                         .whereEqualTo("userId",userId)
                         .get()
                         .addOnSuccessListener(querySnapshots -> {
                           if (querySnapshots.size()>=30){
                                Toast.makeText(vaccinesPage.this,"Ekleme yapmadan önce bazı eski aşı kayıtlarını sil!",Toast.LENGTH_SHORT).show();
                           }
                           else{
                               mData=new HashMap<>();
                               mData.put("type",vaccineName);
                               mData.put("date",date);

                               mFireStore.collection("vaccines")
                                       .add(mData)
                                       .addOnSuccessListener(documentReference -> {
                                           Toast.makeText(vaccinesPage.this,"Aşı başarıyla kaydedildi.",Toast.LENGTH_SHORT).show();

                                       })

                                       .addOnFailureListener(e -> {
                                           Toast.makeText(vaccinesPage.this,"Aşı kaydedilemedi.",Toast.LENGTH_SHORT).show();
                                       });
                           }

                          })
                         .addOnFailureListener(e -> {
                             Toast.makeText(vaccinesPage.this,"Aşı bilgileri alınırken hata oluştu.",Toast.LENGTH_SHORT).show();
                         });

               }

               else {
                   Toast.makeText(vaccinesPage.this,"Kullanıcı oturumu açılmamış.",Toast.LENGTH_SHORT).show();
               }
            }
        });


    }
}
