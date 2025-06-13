package domain.entities;

import domain.enums.EstadoLibro;

import java.util.UUID;

public class Libro {
    private final String uuid;
    private String titulo;
    private String autor;
    private String genero;
    private String editorial;
    private EstadoLibro estado;

    public Libro(String titulo, String autor, String genero, String editorial) {
        // propiedades autogeneradas
        this.uuid = UUID.randomUUID().toString();
        this.estado = EstadoLibro.LIBRE;

        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.editorial = editorial;
    }

    public String getUuid() {
        return uuid;
    }

    public EstadoLibro getEstado() {
        return estado;
    }

    public void marcarComoDiponible() {
        if (this.estado == EstadoLibro.LIBRE) return;

        this.estado = EstadoLibro.LIBRE;
    }

    public void marcarComoReservado() {
        if (this.estado == EstadoLibro.RESERVADO) return;

        this.estado = EstadoLibro.RESERVADO;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
}
