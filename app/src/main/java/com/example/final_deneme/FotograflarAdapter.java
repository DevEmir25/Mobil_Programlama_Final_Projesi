// FotograflarAdapter.java
package com.example.final_deneme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FotograflarAdapter extends RecyclerView.Adapter<FotograflarAdapter.ViewHolder> {

    private List<Fotograf> fotografListesi;

    public FotograflarAdapter(List<Fotograf> fotografListesi) {
        this.fotografListesi = fotografListesi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fotograf, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fotograf fotograf = fotografListesi.get(position);

        Glide.with(holder.itemView.getContext())
                .load(fotograf.getFotografUrl())
                .into(holder.imageViewFoto);

        String basliklar = String.join(", ", fotograf.getSeciliBasliklar());
        holder.textViewBasliklar.setText(basliklar);
    }

    @Override
    public int getItemCount() {
        return fotografListesi.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewFoto;
        private TextView textViewBasliklar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewFoto = itemView.findViewById(R.id.imageViewFoto);
            textViewBasliklar = itemView.findViewById(R.id.textViewBasliklar);
        }
    }
}
