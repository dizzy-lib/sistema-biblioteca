package infraestructure.repositories;

import domain.entities.Libro;
import interfaces.infraestructure.IRepositorioLibros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import java.util.HashSet;
import java.util.Set;

public class RepositorioLibros implements IRepositorioLibros {
  private final HashMap<String, Libro> libros = new HashMap<>();

  @Override
  public ArrayList<Libro> buscarLibros(String criterio) {
    Set<Libro> librosEncontradosSet = new HashSet<>(); // Use Set to avoid duplicates

    if (criterio == null || criterio.trim().isEmpty()) {
      return new ArrayList<>(librosEncontradosSet);
    }

    String terminoBusqueda = criterio.trim().toLowerCase();

    for (Libro libro : libros.values()) {
      if (libro != null) {
        boolean matched = false;
        if (libro.getTitulo() != null && libro.getTitulo().toLowerCase().contains(terminoBusqueda)) {
          matched = true;
        }
        if (!matched && libro.getAutor() != null && libro.getAutor().toLowerCase().contains(terminoBusqueda)) {
          matched = true;
        }
        if (!matched && libro.getEditorial() != null && libro.getEditorial().toLowerCase().contains(terminoBusqueda)) {
          matched = true;
        }
        if (matched) {
          librosEncontradosSet.add(libro);
        }
      }
    }
    return new ArrayList<>(librosEncontradosSet);
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

    // Eliminar usando el UUID como clave
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

  @Override
  public ArrayList<Libro> obtenerTodosLosLibros() {
    return new ArrayList<>(libros.values());
  }
}
