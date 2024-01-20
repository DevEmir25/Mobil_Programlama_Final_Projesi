package com.example.final_deneme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FotografEkle extends AppCompatActivity {

    private static final String TAG = "FotografEkleActivity";
    private ImageView imageViewFoto;
    public RecyclerView recyclerViewBaslik;
    public static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int CAMERA_REQUEST = 1888;

    private Button btnKamera;
    private Button btnEkle;

    public BasliklarAdapter basliklarAdapter;

    private List<String> seciliBasliklar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotograf_ekle);

        imageViewFoto = findViewById(R.id.imageViewFoto);
        recyclerViewBaslik = findViewById(R.id.recyclerViewBaslik);
        btnKamera = findViewById(R.id.btnKamera);
        btnEkle = findViewById(R.id.btnEkle);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewBaslik.setLayoutManager(layoutManager);

        List<String> baslikListesi = getBaslikListesi();

        BasliklarAdapter basliklarAdapter = new BasliklarAdapter(baslikListesi);
        recyclerViewBaslik.setAdapter(basliklarAdapter);

        seciliBasliklar = new ArrayList<>();

        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitlariFirebaseKaydet();
            }
        });

        btnKamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermissionAndOpenCamera();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewFoto.setImageBitmap(imageBitmap);
        }
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Kamera izni reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<String> getBaslikListesi() {
        final List<String> baslikListesi = new ArrayList<>();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("basliklar");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String baslik = dataSnapshot.child("baslik").getValue(String.class);
                    baslikListesi.add(baslik);
                }

                basliklarAdapter = new BasliklarAdapter(baslikListesi);
                recyclerViewBaslik.setAdapter(basliklarAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Başlıkları alma işlemi başarısız oldu.", error.toException());
            }
        });

        return baslikListesi;
    }

    private void kayitlariFirebaseKaydet() {
        for (int i = 0; i < basliklarAdapter.getItemCount(); i++) {
            if (basliklarAdapter.isSecili(i)) {
                seciliBasliklar.add(basliklarAdapter.getBaslik(i));
            }
        }

        if (seciliBasliklar.size() > 0) {
            DatabaseReference fotografRef = FirebaseDatabase.getInstance().getReference().child("fotograflar");

            String fotografId = fotografRef.push().getKey();
            Fotograf fotograf = new Fotograf(fotografId, "", seciliBasliklar);

            fotografRef.child(fotografId).setValue(fotograf).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Fotoğraf başarıyla eklendi", Toast.LENGTH_SHORT).show();

                    uploadPhotoToStorage(fotografId);
                } else {
                    Toast.makeText(this, "Fotoğraf eklenirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "En az bir başlık seçmelisiniz", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPhotoToStorage(String fotografId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("fotograflar").child(fotografId + ".jpg");

        imageViewFoto.setDrawingCacheEnabled(true);
        imageViewFoto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewFoto.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String photoUrl = uri.toString();
                    DatabaseReference fotografRef = FirebaseDatabase.getInstance().getReference().child("fotograflar").child(fotografId);
                    fotografRef.child("fotografUrl").setValue(photoUrl);
                    finish();
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "URL alınamadı", e);
                });
            } else {
                Log.e(TAG, "Fotoğraf yükleme başarısız oldu", task.getException());
            }
        });
    }
}
