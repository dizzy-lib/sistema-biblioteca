package presentation.services;

import application.usecases.AgregarLibroCasoUso;
import application.usecases.BuscarLibroCasoUso;
import application.usecases.DevolverLibroCasoUso;
import application.usecases.PrestarLibroCasoUso;
import domain.entities.Libro;
import domain.entities.Reserva;
import domain.services.ServicioGenerador;
import domain.services.ServicioLibros;
import domain.services.ServicioPersistencia;
import domain.services.ServicioPrestamos;
import domain.valueObject.DocumentoRut;
import domain.valueObject.LibroCatalogoEntry;
import interfaces.infraestructure.IRepositorioLibros;
import interfaces.infraestructure.IRepositorioReservas;

import java.util.ArrayList;
import java.util.Set;

/**
 * Clase de servicio de terminal que maneja los casos
 * de la biblioteca por terminal
 */
public class BibliotecaApplicationService {
  private final AgregarLibroCasoUso agregarLibroCasoUso;
  private final BuscarLibroCasoUso buscarLibroCasoUso;
  private final PrestarLibroCasoUso prestarLibroCasoUso;
  private final DevolverLibroCasoUso devolverLibroCasoUso;
  private final ServicioPersistencia servicioPersistencia;
  private final IRepositorioLibros repositorioLibros;
  private final IRepositorioReservas repositorioReservas;
  private final String rutaLibrosCsv;
  private final String rutaReservasCsv;

  public BibliotecaApplicationService(
      IRepositorioLibros repositorioLibros,
      ServicioLibros servicioLibros,
      ServicioPrestamos servicioPrestamos,
      ServicioGenerador servicioGenerador,
      ServicioPersistencia servicioPersistencia,
      IRepositorioReservas repositorioReservas,
      String rutaLibrosCsv,
      String rutaReservasCsv) {
    this.agregarLibroCasoUso = new AgregarLibroCasoUso(repositorioLibros, servicioLibros, servicioGenerador);
    this.buscarLibroCasoUso = new BuscarLibroCasoUso(repositorioLibros);
    this.prestarLibroCasoUso = new PrestarLibroCasoUso(servicioLibros, servicioPrestamos);
    this.devolverLibroCasoUso = new DevolverLibroCasoUso(servicioPrestamos);
    this.servicioPersistencia = servicioPersistencia;
    this.repositorioLibros = repositorioLibros;
    this.repositorioReservas = repositorioReservas;
    this.rutaLibrosCsv = rutaLibrosCsv;
    this.rutaReservasCsv = rutaReservasCsv;
  }

  /**
   * Método que ejecuta el caso de uso de registrar un libro dentro del sistema
   * @param titulo título del libro
   * @param autor autor del libro
   * @param genero género del libro
   * @param editorial editorial del libro
   * @return libro agregado al sistema
   */
  public Libro agrearLibro(String titulo, String autor, String genero, String editorial) {
    // Ejecuta el caso de uso para agregar un libro al sistema
    Libro libro = this.agregarLibroCasoUso.ejecutar(titulo, autor, genero, editorial);

    // Almacena el libro generado dentro del CSV
    if (libro != null) {
      this.servicioPersistencia.guardarLibrosEnCSV(this.rutaLibrosCsv, this.repositorioLibros.obtenerTodosLosLibros());
    }

    // retorna el libro agregado
    return libro;
  }

  /**
   * Método que ejecuta el caso de uso para buscar un libro dentro del repo
   * @param criterio criterio de búsqueda
   * @return ArrayList de libros encontrados dentro del sistema
   */
  public ArrayList<Libro> buscarLibro(String criterio) {
    return this.buscarLibroCasoUso.ejecutar(criterio);
  }

  /**
   * Metodo que ejecuta el caso de uso de prestar/reservar un libro para un usuario
   * @param uuid uuid del libro a reservar
   * @param rut rut del usuario que reserva
   * @return reserva generada
   */
  public Reserva prestarLibro(String uuid, DocumentoRut rut) {
    // Ejecuta el caso de uso de prestar un libro
    Reserva reserva = this.prestarLibroCasoUso.ejecutar(uuid, rut);

    // Agrega la persistencia de datos dentro de los CSV
    if (reserva != null) {
      this.servicioPersistencia.guardarLibrosEnCSV(this.rutaLibrosCsv, this.repositorioLibros.obtenerTodosLosLibros());
      this.servicioPersistencia.guardarReservasEnCSV(this.rutaReservasCsv,
          this.repositorioReservas.obtenerTodasLasReservas());
    }

    // retorna la reserva generada
    return reserva;
  }

  /**
   * Método que ejecuta el caso de uso de devolución de un libro/reserva
   * @param idReserva id de la reserva a gestionar
   * @return Libro devuelto
   */
  public Libro devolverLibro(int idReserva) {
    // Ejecuta el caso de uso que devuelve el libro
    Libro libro = this.devolverLibroCasoUso.ejecutar(idReserva);

    // Agrega la persistencia dentro de la aplicación
    if (libro != null) {
      this.servicioPersistencia.guardarLibrosEnCSV(this.rutaLibrosCsv, this.repositorioLibros.obtenerTodosLosLibros());
      this.servicioPersistencia.guardarReservasEnCSV(this.rutaReservasCsv,
          this.repositorioReservas.obtenerTodasLasReservas());
    }

    // retorna el libro de la reserva
    return libro;
  }

  /**
   * Método que muestra todas las reservas activas dentro del sistema
   * @return Arraylist de todas las reservas dentro del repositorio de reservas
   */
  public ArrayList<Reserva> verReservasActivas() {
    return this.repositorioReservas.obtenerTodasLasReservas();
  }

  /**
   * Método que muestra todos los libros dentro del sistema
   * @return Arraylist de los libros registrados del sistema
   */
  public ArrayList<Libro> obtenerTodosLosLibros() {
    return this.repositorioLibros.obtenerTodosLosLibros();
  }

  /**
   * Método que obtiene un catálogo único de libros (título y autor)
   * @return Set de libros únicos
   */
  public Set<LibroCatalogoEntry> obtenerCatalogoDeLibros() {
    return this.repositorioLibros.obtenerCatalogoLibros();
  }
}
