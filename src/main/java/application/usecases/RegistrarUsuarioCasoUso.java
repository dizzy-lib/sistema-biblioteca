package application.usecases;

import domain.entities.Usuario;
import domain.services.ServicioUsuarios;
import domain.valueObject.DocumentoRut;

/**
 * Clase que ejecuta el caso de uso para registrar un usuario dentro del sistema
 */
public class RegistrarUsuarioCasoUso {
    private final ServicioUsuarios servicioUsuarios;

    public RegistrarUsuarioCasoUso(ServicioUsuarios servicioUsuarios) {
        this.servicioUsuarios = servicioUsuarios;
    }

    public Usuario ejecutar(String nombre, DocumentoRut rut) {
        return this.servicioUsuarios.registrarUsuario(nombre, rut);
    }
}
