package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // 1. Declarar variables
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private Button btnAdmin; // Variable para el botón secreto

    // Variable para Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();

        // 2. Vincular con el diseño (IDs del XML)
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        btnAdmin = findViewById(R.id.btnAdminLoad); // Vinculamos el botón admin

        // 3. Configurar Botón INGRESAR
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, pass);
            }
        });

        // 4. Configurar Botón REGISTRO
        tvRegister.setOnClickListener(v -> {
            // Navegar a la pantalla de Registro
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // 5. Configurar BOTÓN SECRETO (ADMIN LOAD)
        // Aquí es donde ocurre la magia de la carga de datos
        btnAdmin.setOnClickListener(v -> {
            // Cargar Partidos
            CargaDatos.cargarPartidosReales();

            // Cargar Noticias (¡NUEVA LÍNEA!) ✅
            CargaDatos.cargarNoticias();

            Toast.makeText(MainActivity.this, "¡Partidos y Noticias cargados!", Toast.LENGTH_SHORT).show();
            v.setEnabled(false); // Desactivar para evitar clicks dobles
        });
    }

    // Método para iniciar sesión con Firebase
    private void loginUser(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login correcto
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, getString(R.string.login_title), Toast.LENGTH_SHORT).show();

                        // Preparar el salto al HOME
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                        // --- REQUISITO DE RÚBRICA: ENVÍO DE EXTRAS ---
                        // "Empaquetamos" el email para enviarlo a la otra actividad.
                        if (user != null) {
                            intent.putExtra("extra_email", user.getEmail());
                        }
                        // ---------------------------------------------

                        startActivity(intent);
                        finish(); // Cierra esta actividad para que no se pueda volver atrás

                    } else {
                        // Login fallido
                        Toast.makeText(MainActivity.this, "Error: Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}