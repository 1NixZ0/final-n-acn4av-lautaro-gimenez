package com.example.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {

    private List<Noticia> listaNoticias;

    public NoticiaAdapter(List<Noticia> listaNoticias) {
        this.listaNoticias = listaNoticias;
    }

    @NonNull
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noticia, parent, false);
        return new NoticiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
        Noticia noticia = listaNoticias.get(position);
        holder.tvTitulo.setText(noticia.getTitulo());
        holder.tvResumen.setText(noticia.getResumen());

        // Carga de imagen de noticia
        Glide.with(holder.itemView.getContext())
                .load(noticia.getImagenUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgNoticia);
    }

    @Override
    public int getItemCount() {
        return listaNoticias.size();
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvResumen;
        ImageView imgNoticia;

        public NoticiaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloNoticia);
            tvResumen = itemView.findViewById(R.id.tvResumenNoticia);
            imgNoticia = itemView.findViewById(R.id.imgNoticia);
        }
    }
}