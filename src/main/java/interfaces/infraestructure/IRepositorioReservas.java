package interfaces.infraestructure;

import domain.entities.Reserva;

import java.util.Optional;

public interface IRepositorioReservas {
    Optional<Reserva> buscarReservaPorId(int id);
    boolean eliminarReservaPorId(int id);
    void agregarReserva(Reserva reserva);
    /**
     * Obtiene la última reserva (la que tiene el ID más alto)
     * @return Optional con la última reserva o empty si no hay reservas
     */
    Optional<Reserva> obtenerUltimaReserva();
}
