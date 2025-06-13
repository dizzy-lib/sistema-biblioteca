package application.usecases;

import domain.entities.Libro;
import interfaces.infraestructure.IRepositorioLibros;
import shared.utils.Formateador;
import shared.utils.Validaciones;

import java.util.ArrayList;

/**
 * Clase que delega la responsabilidad de buscar un libro por título
 * dentro del repositorio de libros
 */
public class BuscarLibroCasoUso {
    private final IRepositorioLibros repositorioLibros;

    public BuscarLibroCasoUso(IRepositorioLibros repositorioLibros) {
        this.repositorioLibros = repositorioLibros;
    }

    /**
     * Ejecuta la búsqueda de un libro dentro del repositorio inyectado
     * en el constructor
     *
     * @param titulo título del libro
     * @return libro encontrado o null
     */
    public ArrayList<Libro> ejecutar(String titulo) {
        // Validar que el campo introducido sea alfanumérico o lanza excepción
        Validaciones.esAlfanumericoFlexible(titulo);

        // formatea el título de búsqueda
        String busquedaFormateada = Formateador.normalizarString(titulo);

        // retorna el libro encontrado en caso contrario retorna null
        return this.repositorioLibros.buscarLibroPorTitulo(busquedaFormateada);
    }
}
