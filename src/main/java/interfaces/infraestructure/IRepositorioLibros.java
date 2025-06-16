package interfaces.infraestructure;

import domain.entities.Libro;

import java.util.ArrayList;
import java.util.Optional;

public interface IRepositorioLibros {
    ArrayList<Libro> buscarLibros(String criterio); // Renamed and purpose changed
    void agregarLibro(Libro libro);
    void eliminarLibro(Libro libro);
    Optional<Libro> buscarLibroPorId(String id);
    ArrayList<Libro> obtenerTodosLosLibros();
}
