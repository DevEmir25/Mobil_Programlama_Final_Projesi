package com.example.final_deneme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// BasliklarAdapter.java
// BasliklarAdapter.java
// BasliklarAdapter.java
public class BasliklarAdapter extends RecyclerView.Adapter<BasliklarAdapter.ViewHolder> {

    private List<String> baslikListesi;
    private List<Boolean> seciliDurumlar;

    public BasliklarAdapter(List<String> baslikListesi) {
        this.baslikListesi = baslikListesi;
        this.seciliDurumlar = new ArrayList<>(Collections.nCopies(baslikListesi.size(), false));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baslik, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String baslik = baslikListesi.get(position);
        boolean secili = seciliDurumlar.get(position);
        holder.bind(baslik, secili);
    }

    @Override
    public int getItemCount() {
        return baslikListesi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewBaslik;
        private ImageView imageViewSecili;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBaslik = itemView.findViewById(R.id.textViewBaslik);
            imageViewSecili = itemView.findViewById(R.id.imageViewSecili);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        toggleSecili(position);
                    }
                }
            });
        }

        public void bind(String baslik, boolean secili) {
            textViewBaslik.setText(baslik);

            if (secili) {
                imageViewSecili.setVisibility(View.VISIBLE);
            } else {
                imageViewSecili.setVisibility(View.INVISIBLE);
            }
        }
    }

    public boolean isSecili(int position) {
        return seciliDurumlar.get(position);
    }

    public String getBaslik(int position) {
        return baslikListesi.get(position);
    }

    public void toggleSecili(int position) {
        seciliDurumlar.set(position, !seciliDurumlar.get(position));
        notifyItemChanged(position);
    }
}


