package domain.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Reserva {
    public static final int DIAS_PRESTAMO = 4;

    private final int id;
    private final Usuario usuario;
    private final Libro libro;
    private final LocalDate fechaVencimiento;

    public Reserva(int id, Usuario usuario, Libro libro) {
        this.id = id;
        this.usuario = usuario;
        this.libro = libro;
        this.fechaVencimiento = LocalDate.now().plusDays(DIAS_PRESTAMO);
    }

    public Reserva(int id, Usuario usuario, Libro libro, LocalDate fechaVencimiento) {
        this.id = id;
        this.usuario = usuario;
        this.libro = libro;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return id == reserva.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
