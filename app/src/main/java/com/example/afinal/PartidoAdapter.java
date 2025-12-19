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

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder> {

    private List<Partido> listaPartidos;

    public PartidoAdapter(List<Partido> listaPartidos) {
        this.listaPartidos = listaPartidos;
    }

    @NonNull
    @Override
    public PartidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partido, parent, false);
        return new PartidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidoViewHolder holder, int position) {
        Partido partido = listaPartidos.get(position);

        holder.tvEquipo1.setText(partido.getEquipo1());
        holder.tvEquipo2.setText(partido.getEquipo2());
        holder.tvFecha.setText(partido.getFecha());

        // --- CARGAR ESCUDO LOCAL (Equipo 1) ---
        Glide.with(holder.itemView.getContext())
                .load(partido.getFotoUrl1()) // Usamos la URL del local
                .placeholder(R.mipmap.ic_launcher_round) // Muestra esto mientras carga
                .into(holder.imgEquipo1);

        // --- CARGAR ESCUDO VISITANTE (Equipo 2) ---
        Glide.with(holder.itemView.getContext())
                .load(partido.getFotoUrl2()) // Usamos la URL del visitante
                .placeholder(R.mipmap.ic_launcher_round) // Muestra esto mientras carga
                .into(holder.imgEquipo2);
    }

    @Override
    public int getItemCount() {
        return listaPartidos.size();
    }

    public static class PartidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvEquipo1, tvEquipo2, tvFecha;
        ImageView imgEquipo1, imgEquipo2;

        public PartidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEquipo1 = itemView.findViewById(R.id.tvEquipo1);
            tvEquipo2 = itemView.findViewById(R.id.tvEquipo2);
            imgEquipo1 = itemView.findViewById(R.id.imgEquipo1);
            imgEquipo2 = itemView.findViewById(R.id.imgEquipo2);
        }
    }
}