package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView tvNombre, tvEquipo;
    private Button btnLogout, btnVerMas; // Agregamos el botón ver más
    private RecyclerView rvPartidos, rvNoticias; // Agregamos el segundo recycler

    // Variables de Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Variables de la Lista de Partidos
    private PartidoAdapter adapterPartidos;
    private List<Partido> listaDePartidos;

    // Variables de la Lista de Noticias (NUEVO)
    private NoticiaAdapter adapterNoticias;
    private List<Noticia> listaDeNoticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // --- EXTRAS CHECK ---
        String emailRecibido = getIntent().getStringExtra("extra_email");
        if (emailRecibido != null) {
            Log.d("EXTRAS_CHECK", "Email recibido: " + emailRecibido);
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 1. Vincular vistas
        tvNombre = findViewById(R.id.tvUserNombre);
        tvEquipo = findViewById(R.id.tvUserEquipo);
        btnLogout = findViewById(R.id.btnLogout);
        btnVerMas = findViewById(R.id.btnVerMasNoticias); // Nuevo botón

        rvPartidos = findViewById(R.id.rvPartidos);
        rvNoticias = findViewById(R.id.rvNoticiasHome); // Nuevo Recycler

        // 2. Configurar RecyclerView Partidos
        rvPartidos.setLayoutManager(new LinearLayoutManager(this));
        listaDePartidos = new ArrayList<>();
        adapterPartidos = new PartidoAdapter(listaDePartidos);
        rvPartidos.setAdapter(adapterPartidos);

        // 3. Configurar RecyclerView Noticias (NUEVO)
        rvNoticias.setLayoutManager(new LinearLayoutManager(this));
        listaDeNoticias = new ArrayList<>();
        adapterNoticias = new NoticiaAdapter(listaDeNoticias);
        rvNoticias.setAdapter(adapterNoticias);

        // 4. Iniciar carga de datos
        cargarDatosUsuario();

        // Botón Cerrar Sesión
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // 5. Botón Ver Más Noticias (NUEVO)
        btnVerMas.setOnClickListener(v -> {
            // Obtenemos el equipo actual del TextView para pasarlo a la otra pantalla
            String textoCompleto = tvEquipo.getText().toString();
            // Quitamos la parte de "Fanático de: " para quedarnos solo con el nombre del equipo
            String equipoLimpio = textoCompleto.replace(getString(R.string.fanatico_de) + " ", "");

            Intent intent = new Intent(HomeActivity.this, NoticiasActivity.class);
            intent.putExtra("equipo_extra", equipoLimpio); // Pasamos el equipo como Extra
            startActivity(intent);
        });
    }

    private void cargarDatosUsuario() {
        if (mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        String equipoFavorito = documentSnapshot.getString("equipo");

                        tvNombre.setText(nombre);
                        tvEquipo.setText(getString(R.string.fanatico_de) + " " + equipoFavorito);

                        // Personalizamos y cargamos ambas listas
                        personalizarColores(equipoFavorito);
                        cargarPartidosFiltrados(equipoFavorito);
                        cargarNoticiasFiltradas(equipoFavorito); // <--- Llamada nueva
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, getString(R.string.error_carga), Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarPartidosFiltrados(String miEquipo) {
        db.collection("partidos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaDePartidos.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Partido p = document.toObject(Partido.class);
                    if (p.getEquipo1().equalsIgnoreCase(miEquipo) || p.getEquipo2().equalsIgnoreCase(miEquipo)) {
                        listaDePartidos.add(p);
                    }
                }
                adapterPartidos.notifyDataSetChanged();
            }
        });
    }

    // --- NUEVO MÉTODO PARA CARGAR NOTICIAS ---
    private void cargarNoticiasFiltradas(String miEquipo) {
        db.collection("noticias")
                .whereEqualTo("equipo", miEquipo) // Filtro por equipo
                .limit(3) // Solo traemos las 3 primeras para el Home
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaDeNoticias.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Noticia n = document.toObject(Noticia.class);
                            listaDeNoticias.add(n);
                        }
                        adapterNoticias.notifyDataSetChanged();
                    }
                });
    }

    private void personalizarColores(String equipo) {
        View fondo = findViewById(android.R.id.content);
        if (equipo == null) return;

        int colorFondo = ContextCompat.getColor(this, R.color.background_app);
        int colorTexto = ContextCompat.getColor(this, R.color.text_title);

        if (equipo.equalsIgnoreCase("Boca Juniors")) {
            colorFondo = ContextCompat.getColor(this, R.color.boca_fondo);
            colorTexto = ContextCompat.getColor(this, R.color.boca_texto);
        } else if (equipo.equalsIgnoreCase("River Plate")) {
            colorFondo = ContextCompat.getColor(this, R.color.river_fondo);
            colorTexto = ContextCompat.getColor(this, R.color.river_texto);
        } else if (equipo.equalsIgnoreCase("Real Madrid")) {
            colorFondo = ContextCompat.getColor(this, R.color.madrid_fondo);
            colorTexto = ContextCompat.getColor(this, R.color.madrid_texto);
        } else if (equipo.equalsIgnoreCase("Barcelona")) {
            colorFondo = ContextCompat.getColor(this, R.color.barca_fondo);
            colorTexto = ContextCompat.getColor(this, R.color.barca_texto);
        }

        fondo.setBackgroundColor(colorFondo);
        tvEquipo.setTextColor(colorTexto);
    }
}