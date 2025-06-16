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
import interfaces.infraestructure.IRepositorioLibros;
import interfaces.infraestructure.IRepositorioReservas;

import java.util.ArrayList;

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

  public Libro agrearLibro(String titulo, String autor, String genero, String editorial) {
    Libro libro = this.agregarLibroCasoUso.ejecutar(titulo, autor, genero, editorial);
    if (libro != null) {
      this.servicioPersistencia.guardarLibrosEnCSV(this.rutaLibrosCsv, this.repositorioLibros.obtenerTodosLosLibros());
    }
    return libro;
  }

  public ArrayList<Libro> buscarLibro(String criterio) {
    return this.buscarLibroCasoUso.ejecutar(criterio);
  }

  public Reserva prestarLibro(String uuid, DocumentoRut rut) {
    Reserva reserva = this.prestarLibroCasoUso.ejecutar(uuid, rut);
    if (reserva != null) {
      this.servicioPersistencia.guardarLibrosEnCSV(this.rutaLibrosCsv, this.repositorioLibros.obtenerTodosLosLibros());
      this.servicioPersistencia.guardarReservasEnCSV(this.rutaReservasCsv,
          this.repositorioReservas.obtenerTodasLasReservas());
    }
    return reserva;
  }

  public Libro devolverLibro(int idReserva) {
    Libro libro = this.devolverLibroCasoUso.ejecutar(idReserva);
    if (libro != null) {
      this.servicioPersistencia.guardarLibrosEnCSV(this.rutaLibrosCsv, this.repositorioLibros.obtenerTodosLosLibros());
      this.servicioPersistencia.guardarReservasEnCSV(this.rutaReservasCsv,
          this.repositorioReservas.obtenerTodasLasReservas());
    }
    return libro;
  }
}
