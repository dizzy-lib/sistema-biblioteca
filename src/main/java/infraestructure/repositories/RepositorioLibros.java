package infraestructure.repositories;

import domain.entities.Libro;
import interfaces.infraestructure.IRepositorioLibros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class RepositorioLibros implements IRepositorioLibros {
    private final HashMap<String, Libro> libros = new HashMap<>();

    @Override
    public ArrayList<Libro> buscarLibroPorTitulo(String titulo) {
        ArrayList<Libro> librosEncontrados = new ArrayList<>();

        if (titulo == null || titulo.trim().isEmpty()) {
            return librosEncontrados;
        }

        String tituloBuscado = titulo.trim();

        for (Libro libro : libros.values()) {
            if (libro != null && libro.getTitulo() != null) {
                if (libro.getTitulo().equals(tituloBuscado)) {
                    librosEncontrados.add(libro);
                }
            }
        }

        return librosEncontrados;
    }

    @Override
    public void agregarLibro(Libro libro) {
        if (libro == null || libro.getUuid() == null || libro.getUuid().trim().isEmpty()) {
            throw new IllegalArgumentException("El libro y su UUID no pueden ser nulos o vacíos");
        }

        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del libro no puede ser nulo o vacío");
        }

        // Usar el UUID como clave en el HashMap
        libros.put(libro.getUuid(), libro);
    }

    @Override
    public void eliminarLibro(Libro libro) {
        if (libro == null || libro.getUuid() == null) {
            return;
        }

        // Eliminar usando el UUID como clave (CORREGIDO)
        libros.remove(libro.getUuid());
    }

    @Override
    public Optional<Libro> buscarLibroPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }

        // Buscar usando el UUID como clave
        return Optional.ofNullable(libros.get(id.trim()));
    }
}