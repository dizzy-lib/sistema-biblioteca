package domain.entities;

import domain.enums.EstadoLibro;

import java.util.UUID;

/**
 * Clase que modela un libro dentro del sistema
 */
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

    /**
     * Obtiene el uuid del libro
     * @return uuid del libro
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Método que obtiene el estado del libro
     * @return estado del libro
     */
    public EstadoLibro getEstado() {
        return estado;
    }

    /**
     * Método que marca el libro como disponible dentro del sistema
     */
    public void marcarComoDiponible() {
        if (this.estado == EstadoLibro.LIBRE) return;

        this.estado = EstadoLibro.LIBRE;
    }

    /**
     * Método que marca el libro como reservado
     */
    public void marcarComoReservado() {
        if (this.estado == EstadoLibro.RESERVADO) return;

        this.estado = EstadoLibro.RESERVADO;
    }

    /**
     * Método que obtiene el título del libro
     * @return titulo del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Método que setea/actualiza el título del libro
     * @param titulo título actualizado del libro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Método que obtiene el autor del libro
     * @return autor del libro
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Método que setea/actualiza el autor del libro
     * @param autor autor actualizado del libro
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Método que obtiene el género del libro
     * @return género del libro
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Método que setea/actualiza el género del libro
     * @param genero nuevo género del libro
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Método que obtiene la editorial del libro
     * @return editorial del libro
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Método que setea/actualiza la editorial del libro
     * @param editorial nueva editorial del libro
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
}
