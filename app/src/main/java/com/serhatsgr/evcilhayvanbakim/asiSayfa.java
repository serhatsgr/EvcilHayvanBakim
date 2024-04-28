package com.serhatsgr.evcilhayvanbakim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class asiSayfa extends AppCompatActivity {

    private EditText etVaccineName, etVaccineDate;
    private Button btnAddVaccine;
    private ListView lvVaccines;
    private List<String> vaccineList;
    private ArrayAdapter<String> vaccineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etVaccineName = findViewById(R.id.etVaccineName);
        etVaccineDate = findViewById(R.id.etVaccineDate);
        btnAddVaccine = findViewById(R.id.btnAddVaccine);
        lvVaccines = findViewById(R.id.lvVaccines);

        vaccineList = new ArrayList<>();
        vaccineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vaccineList);
        lvVaccines.setAdapter(vaccineAdapter);

        btnAddVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaccineName = etVaccineName.getText().toString();
                String vaccineDate = etVaccineDate.getText().toString();
                String vaccineInfo = vaccineName + " - " + vaccineDate;
                vaccineList.add(vaccineInfo);
                vaccineAdapter.notifyDataSetChanged();
                etVaccineName.setText("");
                etVaccineDate.setText("");
            }
        });
    }
}
