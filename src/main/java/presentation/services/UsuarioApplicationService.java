package presentation.services;

import application.usecases.RegistrarUsuarioCasoUso;
import domain.entities.Usuario;
import domain.services.ServicioUsuarios;
import domain.valueObject.DocumentoRut;

public class UsuarioApplicationService {
    private final RegistrarUsuarioCasoUso registrarUsuarioCasoUso;

    public UsuarioApplicationService(ServicioUsuarios servicioUsuarios) {
        this.registrarUsuarioCasoUso = new RegistrarUsuarioCasoUso(servicioUsuarios);
    }

    public Usuario registrarUsuario(String nombre, DocumentoRut rut) {
        return this.registrarUsuarioCasoUso.ejecutar(nombre, rut);
    }
}
