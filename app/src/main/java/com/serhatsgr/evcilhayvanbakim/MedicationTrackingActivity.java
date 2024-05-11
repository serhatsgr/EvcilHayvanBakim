package com.serhatsgr.evcilhayvanbakim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class MedicationTrackingActivity extends AppCompatActivity {

    private TextInputEditText medicineName, usageReason, frequency, startDate, usageduration;
    private CheckBox continuation;
    private Button addMedication;

    private ListView medicationList;

    private List<String> medicationIds;
    private ArrayAdapter<String> medicationAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private HashMap<String, Object> mData;
    private FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_tracking);

        medicineName=findViewById(R.id.editTextMedicationName);
        usageReason=findViewById(R.id.editTextUsageReason);
        frequency=findViewById(R.id.editTextFrequency);
        startDate=findViewById(R.id.editTextStartDate);
        usageduration=findViewById(R.id.editTextDuration);
        continuation=findViewById(R.id.checkBoxContinuation);
        addMedication=findViewById(R.id.buttonAddMedication);
        medicationList=findViewById(R.id.listViewMedications);

        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();

        medicationIds=new ArrayList<>();

        loadMedicationHistory();

        addMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mName=medicineName.getText().toString().trim();
                String usR=usageReason.getText().toString().trim();
                String ffrequency=frequency.getText().toString().trim();
                String sDate=startDate.getText().toString().trim();
                String duration=usageduration.getText().toString().trim();
                String continuationn=continuation.getText().toString().trim();


                if (TextUtils.isEmpty(mName)) {
                    Toast.makeText(MedicationTrackingActivity.this, "Lütfen ilaç adını giriniz.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(usR)) {
                    Toast.makeText(MedicationTrackingActivity.this, "Lütfen kullanım nedenini giriniz.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(ffrequency)) {
                    Toast.makeText(MedicationTrackingActivity.this, "Lütfen günlük kullanım adedini giriniz.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(sDate)) {
                    Toast.makeText(MedicationTrackingActivity.this, "Lütfen kullanıma başlama tarihini giriniz.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(duration)) {
                    Toast.makeText(MedicationTrackingActivity.this, "Lütfen kullanım süresini giriniz.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(continuationn)) {
                    Toast.makeText(MedicationTrackingActivity.this, "Lütfen kullanıma devam edilip edilmediğini işaretleyiniz.",Toast.LENGTH_SHORT).show();
                    return;
                }

                mUser=mAuth.getCurrentUser();

                if(mUser!=null){
                    String userId=mUser.getUid();

                    mFirestore.collection("medication")
                            .whereEqualTo("userId",userId)
                            .get()
                            .addOnSuccessListener(querySnapshots -> {
                                if(querySnapshots.size() >=30){
                                    Toast.makeText(MedicationTrackingActivity.this, "Lütfen ekleme yapmadan önce bazı ilaç kayıtlarını siliniz.",Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    mData=new HashMap<>();
                                    mData.put("medicineName", mName);
                                    mData.put("usageReason", usR);
                                    mData.put("frequency", ffrequency);
                                    mData.put("startDate", sDate);
                                    mData.put("usageDuration", duration);
                                    mData.put("userId", userId);
                                    mData.put("continuation", continuation.isChecked() ? "Devam ediyor" : "Devam etmiyor");

                                    mFirestore.collection("medication")
                                            .add(mData)
                                            .addOnSuccessListener(documentReference -> {
                                                Toast.makeText(MedicationTrackingActivity.this, "İlaç kaydı başarıyla eklendi..",Toast.LENGTH_SHORT).show();
                                                medicineName.getText().clear();
                                                usageReason.getText().clear();
                                                frequency.getText().clear();
                                                startDate.getText().clear();
                                                usageduration.getText().clear();
                                                continuation.setChecked(false);
                                                loadMedicationHistory();

                                            })
                                            .addOnFailureListener(e-> Toast.makeText(MedicationTrackingActivity.this, "İlaç kaydı eklenemedi.",Toast.LENGTH_SHORT).show());

                                }
                            } )
                            .addOnFailureListener(e-> Toast.makeText(MedicationTrackingActivity.this, "Aşı bilgileri alınırken hata oluştu.",Toast.LENGTH_SHORT).show());


                } else{
                    Toast.makeText(MedicationTrackingActivity.this, "Kullanıcı oturumu açılmamış.",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void loadMedicationHistory() {
        mFirestore.collection("medication")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> medicationList = new ArrayList<>();
                    medicationIds.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String medicationId = documentSnapshot.getId();
                        medicationIds.add(medicationId);


                        String medicationInfo = documentSnapshot.getString("medicineName") + " - " +
                                documentSnapshot.getString("usageReason")+" - " +
                                documentSnapshot.getString("frequency")+" - " +
                                documentSnapshot.getString("startDate")+" - " +
                                documentSnapshot.getString("usageDuration")+" - " +
                                documentSnapshot.getString("continuation")+" - ";
                        medicationList.add(medicationInfo);
                    }
                    updateVaccineAdapter(medicationList);
                })
                .addOnFailureListener(e -> showToast("İlaç geçmişi yüklenirken bir hata oluştu."));
    }

    private void updateVaccineAdapter(List<String> vaccineList) {
        medicationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vaccineList);
        medicationList.setAdapter(medicationAdapter);

        if (vaccineList.isEmpty()) {
            showEmptyListView();
        } else {
            medicationList.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyListView() {
        TextView textViewEmpty = new TextView(this);
        textViewEmpty.setText("AŞI GEÇMİŞİ YOK");
        textViewEmpty.setGravity(Gravity.CENTER);
        ((ViewGroup) medicationList.getParent()).addView(textViewEmpty);
        medicationList.setEmptyView(textViewEmpty);
        medicationList.setVisibility(View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
