package com.serhatsgr.evcilhayvanbakim;

import android.os.Bundle;
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

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import java.util.ArrayList;


public class EgzersizFragment extends Fragment {

    private EditText etExerciseType;
    private EditText etExerciseDuration;
    private EditText etExerciseDate;
    private EditText etExerciseTime;

    private Calendar calendar;
    private int year, month, dayOfMonth, hourOfDay, minute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_egzersiz, container, false);


        PieChart pieChart = view.findViewById(R.id.pieChart);
        setupPieChart(pieChart);

        etExerciseType = view.findViewById(R.id.etExerciseType);
        etExerciseDuration = view.findViewById(R.id.etExerciseDuration);
        etExerciseDate = view.findViewById(R.id.etExerciseDate);
        etExerciseTime = view.findViewById(R.id.etExerciseTime);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

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
