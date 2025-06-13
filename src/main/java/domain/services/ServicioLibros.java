package domain.services;

import domain.entities.Libro;
import domain.enums.EstadoLibro;
import infraestructure.repositories.RepositorioLibros;
import interfaces.infraestructure.IRepositorioLibros;
import shared.exceptions.LibroNoEncontradoException;
import shared.exceptions.LibroYaPrestadoException;
import shared.utils.Validaciones;

import java.util.Optional;

public class ServicioLibros {
    private final IRepositorioLibros repositorioLibros;

    public ServicioLibros(IRepositorioLibros repositorioLibros) {
        this.repositorioLibros = repositorioLibros;
    }

    /**
     * Valída que la información del libro esté seteada correctamente
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

    public Libro obtenerLibroPorId(String uuid) {
        Optional<Libro> libroOptional = this.repositorioLibros.buscarLibroPorId(uuid);
        if (libroOptional.isEmpty()) {
            throw new LibroNoEncontradoException(String.format("No se encontró el libro con ID %s", uuid));
        }

        return libroOptional.get();
    }

    public void validarLibroLibre(Libro libro) {
        if (libro.getEstado() == EstadoLibro.RESERVADO) {
            throw new LibroYaPrestadoException(String.format("Libro %s ya esta reservado", libro.getTitulo()));
        }
    }

    public void disponibilizarLibro(String uuid) {
        // buscar el libro dentro del repositorio
        Libro libro = this.obtenerLibroPorId(uuid);

        // cambiar el estado del libro
        libro.marcarComoDiponible();
    }
}
