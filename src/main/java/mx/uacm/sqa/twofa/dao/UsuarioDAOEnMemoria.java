package mx.uacm.sqa.twofa.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mx.uacm.sqa.twofa.model.Usuario;
import mx.uacm.sqa.twofa.service.ServicioAutenticacion;

public class UsuarioDAOEnMemoria implements UsuarioDAO {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    public UsuarioDAOEnMemoria() {
        String hash = ServicioAutenticacion.hashSha256("ClavePrueba#2026");
        usuarios.put("usuario.prueba@uacm.edu.mx",
                new Usuario(1, "usuario.prueba@uacm.edu.mx", hash, "usuario.prueba@uacm.edu.mx"));
    }

    @Override
    public Optional<Usuario> buscarPorIdentificador(String identificador) {
        if (identificador == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(usuarios.get(identificador.trim()));
    }

    @Override
    public void actualizarBloqueo(int idUsuario, int intentos, LocalDateTime bloqueadoHasta) {
        usuarios.values().stream()
                .filter(u -> u.getId() == idUsuario)
                .findFirst()
                .ifPresent(u -> u.setBloqueadoHasta(bloqueadoHasta));
    }
}
