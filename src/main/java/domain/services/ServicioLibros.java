package domain.services;

import domain.entities.Libro;
import domain.enums.EstadoLibro;
import interfaces.infraestructure.IRepositorioLibros;
import shared.exceptions.LibroNoEncontradoException;
import shared.exceptions.LibroYaPrestadoException;
import shared.utils.Validaciones;

import java.util.Optional;

/**
 * Clase de servicio que maneja la lógica de negocio respecto a los libros
 * dentro de la biblioteca, dentro de este servicio se ven métodos que validan,
 * obtienen datos, disponibilizan, etc.
 */
public class ServicioLibros {
  private final IRepositorioLibros repositorioLibros;

  public ServicioLibros(IRepositorioLibros repositorioLibros) {
    this.repositorioLibros = repositorioLibros;
  }

  /**
   * Valída que la información del libro esté seteada correctamente
   * 
   * @param libro libro a validar
   */
  public void validarLibro(Libro libro) {
    // valída que el título sea alfanumérico, en caso de que estuviera incorrecto
    // lanza excepción
    Validaciones.esAlfanumericoFlexible(libro.getTitulo());
    Validaciones.esAlfanumericoFlexible(libro.getAutor());
    Validaciones.esAlfanumericoFlexible(libro.getEditorial());
    Validaciones.esAlfanumericoFlexible(libro.getGenero());
  }

  /**
   * Método que obtiene un libro por ID registrado dentro del sistema, en caso
   * de no haber libro, lanza una excepción LibroNoEncontradoException
   * @param uuid uuid del libro
   * @return Libro encontrado
   */
  public Libro obtenerLibroPorId(String uuid) {
    // busca el libro dentro del repositorio
    Optional<Libro> libroOptional = this.repositorioLibros.buscarLibroPorId(uuid);

    if (libroOptional.isEmpty()) {
      throw new LibroNoEncontradoException(String.format("No se encontró el libro con ID %s", uuid));
    }

    return libroOptional.get();
  }

  /**
   * Método que valída si un libro está libre para reservar
   * @param libro libro a consultar
   */
  public void validarLibroLibre(Libro libro) {
    if (libro.getEstado() == EstadoLibro.RESERVADO) {
      throw new LibroYaPrestadoException(String.format("Libro %s ya esta reservado", libro.getTitulo()));
    }
  }

  /**
   * Método que disponibiliza un libro reservado dentro del sistema
   * @param uuid uuid del libro
   */
  public void disponibilizarLibro(String uuid) {
    // buscar el libro dentro del repositorio
    Libro libro = this.obtenerLibroPorId(uuid);

    // cambiar el estado del libro
    libro.marcarComoDiponible();
  }
}
