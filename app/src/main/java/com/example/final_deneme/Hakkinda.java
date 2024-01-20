package com.example.final_deneme;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Hakkinda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakkinda);

        ImageView imageViewProfil = findViewById(R.id.imageViewProfil);
        TextView textViewBilgi = findViewById(R.id.textViewBilgi);

        imageViewProfil.setImageResource(R.drawable.my_photo);
    }
}
