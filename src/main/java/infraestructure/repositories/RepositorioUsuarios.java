package infraestructure.repositories;

import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioUsuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class RepositorioUsuarios implements IRepositorioUsuarios {
  private final HashMap<String, Usuario> usuarios = new HashMap<>();

  @Override
  public Optional<Usuario> obtenerUsuario(DocumentoRut rut) {
    if (rut == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(usuarios.get(rut.getSinFormato()));
  }

  @Override
  public void agregarUsuario(Usuario usuario) {
    if (usuario == null || usuario.getRut() == null) {
      throw new IllegalArgumentException("Usuario y su RUT no pueden ser nulos.");
    }
    usuarios.put(usuario.getRut().getSinFormato(), usuario);
  }

  @Override
  public boolean eliminarUsuario(DocumentoRut rut) {
    if (rut == null) {
      return false;
    }
    return usuarios.remove(rut.getSinFormato()) != null;
  }

  @Override
  public ArrayList<Usuario> obtenerTodosLosUsuarios() {
    return new ArrayList<>(usuarios.values());
  }
}
