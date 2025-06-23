package domain.entities;

import domain.valueObject.DocumentoRut;

import java.util.Objects;

/**
 * Clase que modela un usuario dentro del sistema
 */
public class Usuario {
    private String nombre;
    private final DocumentoRut rut;

    public Usuario(String nombre, DocumentoRut rut) {
        this.rut = rut;
        this.nombre = nombre;
    }

    /**
     * Método que obtiene el RUT del cliente
     * @return rut del usuario
     */
    public DocumentoRut getRut() {
        return rut;
    }

    /**
     * Método que obtiene el nombre del usuario
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    // un usuario solo puede cambiar de nombre no de rut

    /**
     * Método que setea/actualiza el nombre del usuario
     * @param nombre nuevo nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return rut.equals(usuario.rut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut);
    }
}
