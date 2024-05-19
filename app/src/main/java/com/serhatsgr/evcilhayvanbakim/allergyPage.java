package com.serhatsgr.evcilhayvanbakim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class allergyPage extends AppCompatActivity {

    private TextInputEditText allergyType;

    private Button addAllergy;

    private ListView lvAllergy;

    private List<String> allergyIds;

    private ArrayAdapter<String> allergyAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private HashMap<String, Object> mData;

    private FirebaseFirestore mFireStore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_page);

        allergyType=findViewById(R.id.editTextAllergyType);
        addAllergy=findViewById(R.id.buttonSaveAllergy);
        lvAllergy=findViewById(R.id.listViewAllergies);

        mAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();

        allergyIds=new ArrayList<>();

        loadAllergyHistory();

        addAllergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String allergy=allergyType.getText().toString().trim();

             if(TextUtils.isEmpty(allergy)){
                 Toast.makeText(allergyPage.this, "Lütfen alerji adını giriniz.",Toast.LENGTH_SHORT).show();

             }

             mUser=mAuth.getCurrentUser();

             if(mUser!=null){
                 String userId=mUser.getUid();

                 mFireStore.collection("allergy")
                         .whereEqualTo("userId",userId)
                         .get()
                         .addOnSuccessListener(querySnapshots -> {
                            if(querySnapshots.size()>=10){
                                Toast.makeText(allergyPage.this, "Lütfen yeni alerji eklemeden önceki kayıtlardan bazılarını sil!",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mData=new HashMap<>();
                                mData.put("allergyName",allergy);
                                mData.put("userId",userId);

                                mFireStore.collection("allergy")
                                        .add(mData)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(allergyPage.this, "Alerji kaydı başarıyla eklendi..",Toast.LENGTH_SHORT).show();
                                            allergyType.getText().clear();
                                            loadAllergyHistory();
                                        })
                                        .addOnFailureListener(e-> Toast.makeText(allergyPage.this, "Alerji kaydı eklenemedi.",Toast.LENGTH_SHORT).show());
                            }
                         })

                         .addOnFailureListener(e-> Toast.makeText(allergyPage.this, "Alerji bilgileri alınırken hata oluştu.",Toast.LENGTH_SHORT).show());
             } else{

                 Toast.makeText(allergyPage.this, "Kullanıcı oturumu açılmamış.",Toast.LENGTH_SHORT).show();

             }
            }
        });

    }

    private void loadAllergyHistory() {
        mFireStore.collection("allergy")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> allergyList = new ArrayList<>();
                    allergyIds.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String allergyId = documentSnapshot.getId();
                        allergyIds.add(allergyId);


                        String allergyInfo = documentSnapshot.getString("allergyName");
                        allergyList.add(allergyInfo);
                    }
                    updateAllergyAdapter(allergyList);
                })
                .addOnFailureListener(e -> showToast("Alerji geçmişi yüklenirken bir hata oluştu."));
    }

    private void updateAllergyAdapter(List<String> aList) {
        allergyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, aList);
        lvAllergy.setAdapter(allergyAdapter);

        if (aList.isEmpty()) {
            showEmptyListView();
        } else {
            lvAllergy.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyListView() {
        TextView textViewEmpty = new TextView(this);
        textViewEmpty.setText("ALERJİ GEÇMİŞİ YOK");
        textViewEmpty.setGravity(Gravity.CENTER);
        ((ViewGroup) lvAllergy.getParent()).addView(textViewEmpty);
        lvAllergy.setEmptyView(textViewEmpty);
        lvAllergy.setVisibility(View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
