// Galeri.java
package com.example.final_deneme;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Galeri extends AppCompatActivity {

    private static final String TAG = "Galeri";
    private RecyclerView recyclerViewFotograflar;
    private TextView textViewGaleri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);

        recyclerViewFotograflar = findViewById(R.id.recyclerViewFotograflar);
        textViewGaleri = findViewById(R.id.textViewGaleri);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewFotograflar.setLayoutManager(layoutManager);

        getFotograflarFromFirebase();
    }

    private void getFotograflarFromFirebase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("fotograflar");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Fotograf> fotografListesi = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String fotografId = dataSnapshot.getKey();
                    String fotografUrl = dataSnapshot.child("fotografUrl").getValue(String.class);

                    List<String> basliklar = new ArrayList<>();
                    for (DataSnapshot baslikSnapshot : dataSnapshot.child("seciliBasliklar").getChildren()) {
                        basliklar.add(baslikSnapshot.getValue(String.class));
                    }

                    Fotograf fotograf = new Fotograf(fotografId, fotografUrl, basliklar);
                    fotografListesi.add(fotograf);
                }

                FotograflarAdapter fotograflarAdapter = new FotograflarAdapter(fotografListesi);
                recyclerViewFotograflar.setAdapter(fotograflarAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Fotoğrafları alma işlemi başarısız oldu.", error.toException());
            }
        });
    }
}
