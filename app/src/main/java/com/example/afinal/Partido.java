package com.example.afinal;

public class Partido {
    private String equipo1;
    private String equipo2;
    private String fecha;
    // ANTES: private String fotoUrl;
    // AHORA: Dos campos para las fotos
    private String fotoUrl1;
    private String fotoUrl2;

    public Partido() {
        // Constructor vacío necesario para Firebase
    }

    // Constructor actualizado
    public Partido(String equipo1, String equipo2, String fecha, String fotoUrl1, String fotoUrl2) {
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.fecha = fecha;
        this.fotoUrl1 = fotoUrl1;
        this.fotoUrl2 = fotoUrl2;
    }

    // Getters y Setters actualizados
    public String getEquipo1() { return equipo1; }
    public String getEquipo2() { return equipo2; }
    public String getFecha() { return fecha; }

    public String getFotoUrl1() { return fotoUrl1; } // Nuevo getter
    public String getFotoUrl2() { return fotoUrl2; } // Nuevo getter
}