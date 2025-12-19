package com.example.afinal;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter; // Importante para la lista
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner; // Importante para el menú
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    // Variables actualizadas
    private EditText etNombre, etEmail, etPass;
    private Spinner spEquipo; // ¡AQUÍ ESTÁ EL CAMBIO! (Antes era EditText)
    private Button btnRegistrar;
    private TextView tvVolver;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Vinculamos las vistas
        etNombre = findViewById(R.id.etNombreReg);
        etEmail = findViewById(R.id.etEmailReg);
        etPass = findViewById(R.id.etPassReg);
        btnRegistrar = findViewById(R.id.btnDoRegister);
        tvVolver = findViewById(R.id.tvGoLogin);

        // Vinculamos el Spinner (Menú)
        spEquipo = findViewById(R.id.spEquipoReg);

        // 1. CARGAR LAS OPCIONES DEL MENÚ
        String[] equipos = {"Boca Juniors", "River Plate", "Real Madrid", "Barcelona"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, equipos);
        spEquipo.setAdapter(adapter);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etPass.getText().toString().trim();

                // 2. OBTENER EL EQUIPO SELECCIONADO DEL MENÚ
                String equipo = spEquipo.getSelectedItem().toString();

                if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(RegistroActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                registrarUsuario(email, pass, nombre, equipo);
            }
        });

        tvVolver.setOnClickListener(v -> finish());
    }

    private void registrarUsuario(String email, String pass, String nombre, String equipo) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        guardarDatosFirestore(user.getUid(), nombre, email, equipo);
                    } else {
                        Toast.makeText(RegistroActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarDatosFirestore(String uid, String nombre, String email, String equipo) {
        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("nombre", nombre);
        usuarioMap.put("email", email);
        usuarioMap.put("equipo", equipo);

        db.collection("usuarios").document(uid)
                .set(usuarioMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegistroActivity.this, "¡Registro Exitoso!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegistroActivity.this, "Error guardando datos", Toast.LENGTH_SHORT).show();
                });
    }
}