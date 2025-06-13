package application.usecases;

import domain.entities.Libro;
import domain.entities.Reserva;
import domain.services.ServicioPrestamos;

public class DevolverLibroCasoUso {
    private final ServicioPrestamos servicioPrestamos;
    public DevolverLibroCasoUso(
        ServicioPrestamos servicioPrestamos
    ) {
        this.servicioPrestamos = servicioPrestamos;
    }

    public Libro ejecutar(int idReserva) {
        // obtiene la reserva o lanza excepción ReservaNoEncontrada
        Reserva reserva = this.servicioPrestamos.buscarReservaPorId(idReserva);

        // Elimina la reserva
        this.servicioPrestamos.eliminarReserva(reserva);


        // retorna el libro ya disponible
        return reserva.getLibro();
    }
}
