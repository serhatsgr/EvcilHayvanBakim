package com.serhatsgr.evcilhayvanbakim;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class healthPage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);


        Button asiButton = view.findViewById(R.id.asiBtn);
        Button medicationButton=view.findViewById(R.id.addMedication);
        Button allergyButton=view.findViewById(R.id.btnAllergy);


        asiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), vaccinesPage.class);
                startActivity(intent);
            }
        });

        medicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),  MedicationTrackingActivity.class);
                startActivity(intent);

            }
        });

        allergyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),  allergyPage.class);
                startActivity(intent);

            }
        });



        return view;
    }
}
