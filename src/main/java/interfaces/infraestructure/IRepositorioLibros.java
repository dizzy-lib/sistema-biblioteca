package interfaces.infraestructure;

import domain.entities.Libro;

import java.util.ArrayList;
import java.util.Optional;

public interface IRepositorioLibros {
    /**
     * Método que busca libros dado un criterio dentro del repositorio de libros
     *
     * @param criterio criterio de búsqueda
     * @return Array de libros
     */
    ArrayList<Libro> buscarLibros(String criterio); // Renamed and purpose changed

    /**
     * Método que agrega un libro dentro del repositorio
     * @param libro libro a agregara
     */
    void agregarLibro(Libro libro);

    /**
     * Método que elimina un libro dentro del repositorio
     * @param libro libro a eliminar
     */
    void eliminarLibro(Libro libro);

    /**
     * Método que busca un libro por id dentro del repositorio
     * @param id id del libro a buscar
     * @return Opcional del libro encontrado
     */
    Optional<Libro> buscarLibroPorId(String id);

    /**
     * Método que obtiene todos los libros del repositorio
     * @return Arraylist de todos los libros del repositorio de libros
     */
    ArrayList<Libro> obtenerTodosLosLibros();
}
