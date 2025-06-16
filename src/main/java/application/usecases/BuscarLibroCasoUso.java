package application.usecases;

import domain.entities.Libro;
import interfaces.infraestructure.IRepositorioLibros;
import shared.utils.Validaciones;

import java.util.ArrayList;

/**
 * Clase que delega la responsabilidad de buscar un libro por un criterio
 * (título, autor, editorial) dentro del repositorio de libros.
 */
public class BuscarLibroCasoUso {
  private final IRepositorioLibros repositorioLibros;

  public BuscarLibroCasoUso(IRepositorioLibros repositorioLibros) {
    this.repositorioLibros = repositorioLibros;
  }

  /**
   * Ejecuta la búsqueda de libros dentro del repositorio inyectado
   * en el constructor, basado en un criterio de búsqueda.
   *
   * @param criterio término de búsqueda para título, autor o editorial.
   * @return Lista de libros encontrados.
   */
  public ArrayList<Libro> ejecutar(String criterio) {
    // Validar que el campo introducido sea alfanumérico o lanza excepción
    Validaciones.esAlfanumericoFlexible(criterio);

    // Prepara el criterio para la búsqueda (trim y lowercase)
    String criterioProcesado = criterio.trim().toLowerCase();

    // retorna los libros encontrados
    return this.repositorioLibros.buscarLibros(criterioProcesado);
  }
}
