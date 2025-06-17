package application.usecases;

import domain.entities.Libro;
import domain.entities.Reserva;
import domain.services.ServicioPrestamos;

/**
 * Clase que se encarga de ejecutar el caso de uso para devolver un libro
 * reservado
 */
public class DevolverLibroCasoUso {
    private final ServicioPrestamos servicioPrestamos;

    public DevolverLibroCasoUso(ServicioPrestamos servicioPrestamos) {
        this.servicioPrestamos = servicioPrestamos;
    }

    /**
     * Ejecuta el caso de devolver un libro
     * @param idReserva id de la reserva
     * @return libro de la reserva
     */
    public Libro ejecutar(int idReserva) {
        // obtiene la reserva o lanza excepción ReservaNoEncontrada
        Reserva reserva = this.servicioPrestamos.buscarReservaPorId(idReserva);

        // Elimina la reserva
        this.servicioPrestamos.eliminarReserva(reserva);

        // retorna el libro ya disponible
        return reserva.libro();
    }
}
