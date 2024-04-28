package com.serhatsgr.evcilhayvanbakim;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SaglikSayfa extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saglik, container, false);

        // Asi adlı butonu tanımlama
        Button asiButton = view.findViewById(R.id.asiBtn);

        // Asi butonuna onClick olayını ekleme
        asiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asi butonuna tıklandığında yapılacak işlemler
                // Örnek olarak bir Toast mesajı gösterelim
                Toast.makeText(getActivity(), "Asi button clicked!", Toast.LENGTH_SHORT).show();

                // İstediğiniz ek işlemleri buraya ekleyebilirsiniz, örneğin yeni bir aktiviteye geçiş yapabilirsiniz
                Intent intent = new Intent(getActivity(), asiSayfa.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
