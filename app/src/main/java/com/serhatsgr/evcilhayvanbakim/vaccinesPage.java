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

public class vaccinesPage extends AppCompatActivity {

    private TextInputEditText etVaccineName, etVaccineDate;
    private Button btnAddVaccine;
    private ListView lvVaccines;
    private List<String> vaccineIds;
    private ArrayAdapter<String> vaccineAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private HashMap<String, Object> mData;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines_page);

        etVaccineName = findViewById(R.id.etVaccineName);
        etVaccineDate = findViewById(R.id.etVaccineDate);
        btnAddVaccine = findViewById(R.id.btnAddVaccine);
        lvVaccines = findViewById(R.id.ListViewVaccines);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        vaccineIds = new ArrayList<>();
        loadVaccineHistory();

        btnAddVaccine.setOnClickListener(v -> addVaccine());
    }

    private void addVaccine() {
        String vaccineName = etVaccineName.getText().toString().trim();
        String date = etVaccineDate.getText().toString().trim();

        if (TextUtils.isEmpty(vaccineName)) {
            showToast("Aşı Türü Giriniz.");
            return;
        }
        if (TextUtils.isEmpty(date)) {
            showToast("Aşı olma tarihini giriniz!");
            return;
        }

        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String userId = mUser.getUid();

            mFirestore.collection("vaccines")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener(querySnapshots -> {
                        if (querySnapshots.size() >= 30) {
                            showToast("Ekleme yapmadan önce bazı eski aşı kayıtlarını sil!");
                        } else {
                            mData = new HashMap<>();
                            mData.put("type", vaccineName);
                            mData.put("date", date);
                            mData.put("userId", userId);

                            mFirestore.collection("vaccines")
                                    .add(mData)
                                    .addOnSuccessListener(documentReference -> {
                                        showToast("Aşı başarıyla kaydedildi.");
                                        etVaccineName.getText().clear();
                                        etVaccineDate.getText().clear();
                                        loadVaccineHistory();
                                    })
                                    .addOnFailureListener(e -> showToast("Aşı kaydedilemedi."));
                        }
                    })
                    .addOnFailureListener(e -> showToast("Aşı bilgileri alınırken hata oluştu."));
        } else {
            showToast("Kullanıcı oturumu açılmamış.");
        }
    }

    private void loadVaccineHistory() {
        mFirestore.collection("vaccines")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> vaccineList = new ArrayList<>();
                    vaccineIds.clear(); // Clear before adding new data
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String vaccineId = documentSnapshot.getId();
                        vaccineIds.add(vaccineId);

                        String vaccineInfo = documentSnapshot.getString("date") + " - " +
                                documentSnapshot.getString("type");
                        vaccineList.add(vaccineInfo);
                    }
                    updateVaccineAdapter(vaccineList);
                })
                .addOnFailureListener(e -> showToast("Aşı geçmişi yüklenirken bir hata oluştu."));
    }

    private void updateVaccineAdapter(List<String> vaccineList) {
        vaccineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vaccineList);
        lvVaccines.setAdapter(vaccineAdapter);

        if (vaccineList.isEmpty()) {
            showEmptyListView();
        } else {
            lvVaccines.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyListView() {
        TextView textViewEmpty = new TextView(this);
        textViewEmpty.setText("AŞI GEÇMİŞİ YOK");
        textViewEmpty.setGravity(Gravity.CENTER);
        ((ViewGroup) lvVaccines.getParent()).addView(textViewEmpty);
        lvVaccines.setEmptyView(textViewEmpty);
        lvVaccines.setVisibility(View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
