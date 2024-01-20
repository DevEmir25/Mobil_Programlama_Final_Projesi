package com.example.final_deneme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BaslikEkle extends AppCompatActivity {

    private EditText editTextBaslik;
    private EditText editTextAciklama;
    private Button baslikEkleButton;
    private LinearLayout linearLayoutBasliklar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baslik_ekle);

        databaseReference = FirebaseDatabase.getInstance().getReference("basliklar");

        editTextBaslik = findViewById(R.id.editTextBaslik);
        editTextAciklama = findViewById(R.id.editTextAciklama);
        baslikEkleButton = findViewById(R.id.baslikEkleButton);
        linearLayoutBasliklar = findViewById(R.id.linearLayoutBasliklar);

        baslikEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baslikEkle();
            }
        });

        dinlemeBaslat();
    }

    private void baslikEkle() {
        String baslikText = editTextBaslik.getText().toString().trim();
        String aciklamaText = editTextAciklama.getText().toString().trim();

        if (TextUtils.isEmpty(baslikText) || TextUtils.isEmpty(aciklamaText)) {
            Toast.makeText(this, "Başlık ve açıklama boş olamaz", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = databaseReference.push().getKey();
        Baslik baslik = new Baslik(baslikText, aciklamaText);
        databaseReference.child(key).setValue(baslik);

        editTextBaslik.setText("");
        editTextAciklama.setText("");
    }


    private void dinlemeBaslat() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayoutBasliklar.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Baslik baslik = snapshot.getValue(Baslik.class);
                    if (baslik != null) {
                        TextView textViewBaslik = new TextView(BaslikEkle.this);
                        textViewBaslik.setText("Başlık: " + baslik.getBaslik() + "\nAçıklama: " + baslik.getAciklama());
                        textViewBaslik.setTextSize(16);

                        linearLayoutBasliklar.addView(textViewBaslik);

                        View dividerView = new View(BaslikEkle.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                (int) getResources().getDimension(R.dimen.divider_height)
                        );
                        params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.divider_margin_bottom));
                        dividerView.setLayoutParams(params);
                        dividerView.setBackgroundColor(getResources().getColor(R.color.divider_color));
                        linearLayoutBasliklar.addView(dividerView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }


}
