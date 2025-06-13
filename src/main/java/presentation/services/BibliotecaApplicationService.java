package presentation.services;

import application.usecases.AgregarLibroCasoUso;
import application.usecases.BuscarLibroCasoUso;
import application.usecases.DevolverLibroCasoUso;
import application.usecases.PrestarLibroCasoUso;
import domain.entities.Libro;
import domain.entities.Reserva;
import domain.services.ServicioGenerador;
import domain.services.ServicioLibros;
import domain.services.ServicioPrestamos;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioLibros;

import java.util.ArrayList;

public class BibliotecaApplicationService {
    private final AgregarLibroCasoUso agregarLibroCasoUso;
    private final BuscarLibroCasoUso buscarLibroCasoUso;
    private final PrestarLibroCasoUso prestarLibroCasoUso;
    private final DevolverLibroCasoUso devolverLibroCasoUso;

    public BibliotecaApplicationService(
            IRepositorioLibros repositorioLibros,
            ServicioLibros servicioLibros,
            ServicioPrestamos servicioPrestamos,
            ServicioGenerador servicioGenerador
    ) {
        this.agregarLibroCasoUso = new AgregarLibroCasoUso(repositorioLibros, servicioLibros, servicioGenerador);
        this.buscarLibroCasoUso = new BuscarLibroCasoUso(repositorioLibros);
        this.prestarLibroCasoUso = new PrestarLibroCasoUso(servicioLibros, servicioPrestamos);
        this.devolverLibroCasoUso = new DevolverLibroCasoUso(servicioPrestamos);
    }

    public Libro agrearLibro(String titulo, String autor, String genero, String editorial) {
        return this.agregarLibroCasoUso.ejecutar(titulo, autor, genero, editorial);
    }

    public ArrayList<Libro> buscarLibro(String titulo) {
        return this.buscarLibroCasoUso.ejecutar(titulo);
    }

    public Reserva prestarLibro(String uuid, DocumentoRut rut) {
        return this.prestarLibroCasoUso.ejecutar(uuid, rut);
    }

    public Libro devolverLibro(int idReserva) {
        return this.devolverLibroCasoUso.ejecutar(idReserva);
    }
}
