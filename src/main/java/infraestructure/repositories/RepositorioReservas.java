package infraestructure.repositories;

import domain.entities.Reserva;
import interfaces.infraestructure.IRepositorioReservas;

import java.util.HashMap;
import java.util.Optional;

public class RepositorioReservas implements IRepositorioReservas {
    private final HashMap<Integer, Reserva> reservas = new HashMap<>();

    @Override
    public Optional<Reserva> buscarReservaPorId(int id) {
        if (id <= 0) {
            return Optional.empty();
        }

        return Optional.ofNullable(reservas.get(id));
    }

    @Override
    public boolean eliminarReservaPorId(int id) {
        if (id <= 0) {
            return false; // No se pudo eliminar
        }

        // remove() retorna null si no existe la clave
        Reserva reservaEliminada = reservas.remove(id);
        return reservaEliminada != null; // true si se eliminó, false si no existía
    }

    @Override
    public void agregarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }

        if (reserva.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la reserva debe ser positivo");
        }

        reservas.put(reserva.getId(), reserva);
    }

    @Override
    public Optional<Reserva> obtenerUltimaReserva() {
        if (reservas.isEmpty()) {
            return Optional.empty();
        }

        // Encontrar el ID máximo
        int maxId = reservas.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(-1);

        return Optional.ofNullable(reservas.get(maxId));
    }

}
