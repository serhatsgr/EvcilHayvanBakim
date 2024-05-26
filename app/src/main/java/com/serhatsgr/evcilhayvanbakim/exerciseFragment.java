package com.serhatsgr.evcilhayvanbakim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import java.util.ArrayList;
import java.util.HashMap;


public class exerciseFragment extends Fragment {

     TextInputEditText etExerciseType,etExerciseDuration,etExerciseDate,etExerciseTime;

     Button btn,btnH;



    //firebase
    private FirebaseAuth mAuth;

    private FirebaseUser mUsers;

    private HashMap<String, Object> mData;

    private FirebaseFirestore mFireStore;

    //chart
    private Calendar calendar;
    private int year, month, dayOfMonth, hourOfDay, minute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_exercise, container, false);


        PieChart pieChart = view.findViewById(R.id.pieChart);
        setupPieChart(pieChart);

        etExerciseType = view.findViewById(R.id.etExerciseType);
        etExerciseDuration = view.findViewById(R.id.etExerciseDuration);
        etExerciseDate = view.findViewById(R.id.etExerciseDate);
        etExerciseTime = view.findViewById(R.id.etExerciseTime);
        btn= view.findViewById(R.id.btnadd);
        btnH=view.findViewById(R.id.btnHistory);

        mAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type, duration, date, time;

                type = String.valueOf(etExerciseType.getText());
                duration = String.valueOf(etExerciseDuration.getText());
                date = String.valueOf(etExerciseDate.getText());
                time = String.valueOf(etExerciseTime.getText());

                if (TextUtils.isEmpty(type)) {
                    Toast.makeText(getActivity(), "Egzersiz Türü Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(duration)) {
                    Toast.makeText(getActivity(), "Egzersiz Süresi Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(date)) {
                    Toast.makeText(getActivity(), "Egzersiz Tarihi Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(time)) {
                    Toast.makeText(getActivity(), "Egzersiz Zamanı Giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mUsers = mAuth.getCurrentUser();
                if (mUsers != null) {
                    String userId = mUsers.getUid();

                    mFireStore.collection("exercise")
                            .whereEqualTo("userId", userId)
                            .get()
                            .addOnSuccessListener(querySnapshot -> {
                                if (querySnapshot.size() >= 20  ) {
                                    Toast.makeText(getActivity(), "Egzersiz Eklenemedi. Egzersiz Geçmişini Temizle!.", Toast.LENGTH_SHORT).show();
                                } else {
                                    mData = new HashMap<>();
                                    mData.put("type", type);
                                    mData.put("duration", duration);
                                    mData.put("date", date);
                                    mData.put("time", time);
                                    mData.put("userId", userId);

                                    mFireStore.collection("exercise")
                                            .add(mData)
                                            .addOnSuccessListener(documentReference -> {
                                                Toast.makeText(getActivity(), "Egzersiz bilgisi kaydedildi.", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(getActivity(), "Egzersiz bilgisi kaydedilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                            });

                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Egzersiz bilgileri alınırken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(getActivity(), "Kullanıcı oturumu açılmamış.", Toast.LENGTH_SHORT).show();
                }
            }

        });



        btnH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), exerciseHistory.class);
                startActivity(intent);
            }
        });


        etExerciseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etExerciseDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        etExerciseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etExerciseTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hourOfDay, minute, true);
                timePickerDialog.show();
            }
        });

        return view;

    }

    private void setupPieChart(PieChart pieChart) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Pazartesi"));
        entries.add(new PieEntry(45f, "Salı"));
        entries.add(new PieEntry(20f, "Çarşamba"));
        entries.add(new PieEntry(15f, "Perşembe"));
        entries.add(new PieEntry(5f, "Cuma"));
        entries.add(new PieEntry(20f, "Cumartesi"));
        entries.add(new PieEntry(10f, "Pazar"));

        PieDataSet dataSet = new PieDataSet(entries, "Egzersiz Süresi");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setCenterText("Haftanın Günleri");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}
