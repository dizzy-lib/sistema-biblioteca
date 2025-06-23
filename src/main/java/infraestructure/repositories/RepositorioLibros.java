package infraestructure.repositories;

import domain.entities.Libro;
import interfaces.infraestructure.IRepositorioLibros;

import domain.valueObject.LibroCatalogoEntry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase que maneja el repositorio de libros dentro de memoria,
 * implementa la interfaz IRepositorioLibros que es la interfáz conectora
 * para el resto del sistema, esta implementación es concreta, y puede ser
 * cambiada
 * para uso de otros repositorios como BBDD, caché, etc.
 */
public class RepositorioLibros implements IRepositorioLibros {
  private final ArrayList<Libro> libros = new ArrayList<>();

  @Override
  public ArrayList<Libro> buscarLibros(String criterio) {
    Set<Libro> librosEncontradosSet = new HashSet<>(); // Usa Set para evitar duplicados

    // Si no hay criterio o está vacío retorna el array vacío
    if (criterio == null || criterio.trim().isEmpty()) {
      return new ArrayList<>(librosEncontradosSet);
    }

    String terminoBusqueda = criterio.trim().toLowerCase();

    for (Libro libro : libros) {
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

    for (int i = 0; i < libros.size(); i++) {
      if (libros.get(i).getUuid().equals(libro.getUuid())) {
        libros.set(i, libro);
        return;
      }
    }
    libros.add(libro);
  }

  @Override
  public void eliminarLibro(Libro libro) {
    if (libro == null || libro.getUuid() == null) {
      return;
    }

    // Eliminar usando el UUID como clave
    libros.removeIf(l -> l.getUuid().equals(libro.getUuid()));
  }

  @Override
  public Optional<Libro> buscarLibroPorId(String id) {
    if (id == null || id.trim().isEmpty()) {
      return Optional.empty();
    }

    String trimmedId = id.trim();
    return libros.stream()
        .filter(libro -> libro.getUuid().equals(trimmedId))
        .findFirst();
  }

  @Override
  public ArrayList<Libro> obtenerTodosLosLibros() {
    return new ArrayList<>(libros);
  }

  @Override
  public Set<LibroCatalogoEntry> obtenerCatalogoLibros() {
    return libros.stream()
        .map(libro -> new LibroCatalogoEntry(libro.getTitulo(), libro.getAutor()))
        .collect(Collectors.toSet());
  }
}
