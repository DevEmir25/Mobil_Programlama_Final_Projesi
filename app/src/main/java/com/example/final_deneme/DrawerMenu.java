package com.example.final_deneme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DrawerMenu extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);

        Button baslikEkleButton = findViewById(R.id.baslikEkleButton);
        Button fotografEkleButton = findViewById(R.id.fotografEkleButton);
        Button galeriButton = findViewById(R.id.galeriButton);
        Button hakkindaButton = findViewById(R.id.hakkindaButton);
        Button cikisButton = findViewById(R.id.cikisButton);

        baslikEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawerMenu.this,BaslikEkle.class);
                startActivity(intent);
            }
        });

        fotografEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawerMenu.this,FotografEkle.class);
                startActivity(intent);
            }
        });

        galeriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawerMenu.this,Galeri.class);
                startActivity(intent);
            }
        });

        hakkindaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawerMenu.this,Hakkinda.class);
                startActivity(intent);
            }
        });

        cikisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawerMenu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
