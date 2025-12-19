package com.example.afinal;

public class Noticia {
    private String titulo;
    private String resumen;
    private String imagenUrl;
    private String equipo; // <--- ESTO ES LA CLAVE PARA EL FILTRO

    public Noticia() { } // Constructor vacío para Firebase

    public Noticia(String titulo, String resumen, String imagenUrl, String equipo) {
        this.titulo = titulo;
        this.resumen = resumen;
        this.imagenUrl = imagenUrl;
        this.equipo = equipo;
    }

    public String getTitulo() { return titulo; }
    public String getResumen() { return resumen; }
    public String getImagenUrl() { return imagenUrl; }
    public String getEquipo() { return equipo; }
}