package com.serhatsgr.evcilhayvanbakim;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class exerciseHistory extends AppCompatActivity {

    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    private ListView listViewExercise;
    private List<String> exerciseIds;

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history);



        // Firestore ve Auth nesnelerini başlat
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        listViewExercise = findViewById(R.id.listViewExerciseHistory);
        exerciseIds = new ArrayList<>();

        txt=findViewById(R.id.text1);


        // Egzersiz geçmişi listesini yükle
        loadExerciseHistory();

        // Egzersiz geçmişinden bir öğe seçildiğinde gerçekleşecek olayları belirle
        listViewExercise.setOnItemClickListener((parent, view, position, id) -> {
            String exerciseId = exerciseIds.get(position);
            showDeleteConfirmationDialog(exerciseId);
        });


    }

    private void loadExerciseHistory() {
        mFireStore.collection("exercise")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> exerciseList = new ArrayList<>();
                    exerciseIds.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String exerciseId = documentSnapshot.getId();
                        exerciseIds.add(exerciseId);

                        String exercise = documentSnapshot.getString("type") + " - " +
                                documentSnapshot.getString("duration") + " - " +
                                documentSnapshot.getString("date") + " - " +
                                documentSnapshot.getString("time");
                        exerciseList.add(exercise);
                    }
                    // Egzersiz geçmişini göstermek için bir adapter kullanın
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseList);
                    listViewExercise.setAdapter(adapter);

                    if (exerciseList.isEmpty()) {
                        TextView textViewEmpty = new TextView(this);
                        textViewEmpty.setText("GEÇMİŞ BOŞ GİBİ GÖZÜKÜYOR :)");
                        textViewEmpty.setGravity(Gravity.CENTER);
                        ((ViewGroup) listViewExercise.getParent()).addView(textViewEmpty);
                        listViewExercise.setEmptyView(textViewEmpty);
                    } else {
                        // Egzersiz geçmişi doluysa, listeyi göster
                        listViewExercise.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Egzersiz geçmişi yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteConfirmationDialog(String exerciseId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Egzersizi Sil");
        builder.setMessage("Bu egzersizi silmek istediğinizden emin misiniz?");
        builder.setPositiveButton("Evet", (dialog, which) -> {
            deleteExercise(exerciseId);
        });
        builder.setNegativeButton("Hayır", null);
        builder.show();
    }

    private void deleteExercise(String exerciseId) {
        mFireStore.collection("exercise")
                .document(exerciseId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Egzersiz başarıyla silindi.", Toast.LENGTH_SHORT).show();
                    loadExerciseHistory(); // Egzersiz geçmişini yeniden yükle
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Egzersiz silinirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                });
    }
}