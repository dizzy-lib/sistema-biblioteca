package application.usecases;

import domain.entities.Libro;
import domain.entities.Reserva;
import domain.services.ServicioLibros;
import domain.services.ServicioPrestamos;
import domain.valueObject.DocumentoRut;

public class PrestarLibroCasoUso {
    private final ServicioLibros servicioLibros;
    private final ServicioPrestamos servicioPrestamos;

    public PrestarLibroCasoUso(
            ServicioLibros servicioLibros,
            ServicioPrestamos servicioPrestamos
    ) {
        this.servicioLibros = servicioLibros;
        this.servicioPrestamos = servicioPrestamos;
    }

    public Reserva ejecutar(String uuid, DocumentoRut rut) {
        // Obtiene el libro por el uuid, en caso de no encontrarlo
        // lanza un LibroNoEncontradoException
        Libro libro = this.servicioLibros.obtenerLibroPorId(uuid);

        // valída que el libro se encuentre disponible, en caso de estar prestado
        // lanza LibroYaPrestadoException
        this.servicioLibros.validarLibroLibre(libro);

        // Genera la reserva
        Reserva reserva = this.servicioPrestamos.generarReserva(uuid, rut);

        // cambiar estado del libro
        libro.marcarComoReservado();

        return reserva;
    }
}
