package domain.services;

import domain.entities.Libro;

public class ServicioGenerador {
    public ServicioGenerador() {}

    public Libro generarLibro(String titulo, String autor, String genero, String editorial) {
        return new Libro(titulo, genero, autor, editorial);
    }
}
