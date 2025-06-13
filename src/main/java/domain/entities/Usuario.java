package domain.entities;

import domain.valueObject.DocumentoRut;

public class Usuario {
    private String nombre;
    private final DocumentoRut rut;

    public Usuario(String nombre, DocumentoRut rut) {
        this.rut = rut;
        this.nombre = nombre;
    }

    public DocumentoRut getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    // un usuario solo puede cambiar de nombre no de rut

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
