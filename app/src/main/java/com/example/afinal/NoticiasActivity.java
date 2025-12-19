package com.example.afinal;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class NoticiasActivity extends AppCompatActivity {

    private TextView tvTitulo;
    private RecyclerView rvNoticias;
    private NoticiaAdapter adapter;
    private List<Noticia> listaNoticias;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        // 1. Recibir el "Extra" (Nombre del equipo)
        String equipoRecibido = getIntent().getStringExtra("equipo_extra");

        // 2. Configurar vistas
        tvTitulo = findViewById(R.id.tvTituloNoticias);
        rvNoticias = findViewById(R.id.rvTodasLasNoticias);
        db = FirebaseFirestore.getInstance();

        if (equipoRecibido != null) {
            tvTitulo.setText("Mundo " + equipoRecibido);
        }

        // 3. Configurar la lista
        rvNoticias.setLayoutManager(new LinearLayoutManager(this));
        listaNoticias = new ArrayList<>();
        adapter = new NoticiaAdapter(listaNoticias);
        rvNoticias.setAdapter(adapter);

        // 4. Cargar datos
        if (equipoRecibido != null) {
            cargarTodasLasNoticias(equipoRecibido);
        }
    }

    private void cargarTodasLasNoticias(String equipo) {
        db.collection("noticias")
                .whereEqualTo("equipo", equipo) // Filtramos por equipo
                // ¡OJO! Aquí NO ponemos .limit(3), queremos ver todas
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaNoticias.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Noticia n = doc.toObject(Noticia.class);
                            listaNoticias.add(n);
                        }
                        adapter.notifyDataSetChanged();

                        if (listaNoticias.isEmpty()) {
                            Toast.makeText(this, "No hay noticias recientes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}