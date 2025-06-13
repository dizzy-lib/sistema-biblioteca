package infraestructure.repositories;

import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioUsuarios;

import java.util.ArrayList;
import java.util.Optional;

public class RepositorioUsuarios implements IRepositorioUsuarios {
    private final ArrayList<Usuario> usuarios = new ArrayList<>();

    @Override
    public Optional<Usuario> obtenerUsuario(DocumentoRut rut) {
        for (Usuario usuario : usuarios) {
            if (usuario != null && usuario.getRut() != null) {
                // Comparar usando el formato sin puntos ni guión
                if (usuario.getRut().getSinFormato().equals(rut.getSinFormato())) {
                    return Optional.of(usuario);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public boolean eliminarUsuario(DocumentoRut rut) {
        return usuarios.removeIf(usuario ->
                usuario != null &&
                        usuario.getRut() != null &&
                        usuario.getRut().getSinFormato().equals(rut.getSinFormato())
        );
    }

    public ArrayList<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarios);
    }
}