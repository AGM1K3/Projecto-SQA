package mx.uacm.sqa.twofa.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;
import mx.uacm.sqa.twofa.dao.UsuarioDAO;
import mx.uacm.sqa.twofa.model.Usuario;

public class ServicioAutenticacion {

    private final UsuarioDAO usuarioDAO;
    private final ServicioSegundoFactor servicio2FA;

    public ServicioAutenticacion(UsuarioDAO usuarioDAO, ServicioSegundoFactor servicio2FA) {
        this.usuarioDAO = usuarioDAO;
        this.servicio2FA = servicio2FA;
    }

    public boolean autenticarYEnviar2FA(String identificador, String passwordPlano, String idIntento) {
        if (identificador == null || identificador.isBlank() || passwordPlano == null || passwordPlano.isBlank()) {
            return false;
        }
        Optional<Usuario> optUsuario = usuarioDAO.buscarPorIdentificador(identificador.trim());
        if (optUsuario.isEmpty()) {
            return false;
        }
        Usuario usuario = optUsuario.get();
        String hashIngresado = hashSha256(passwordPlano);
        if (!usuario.getPasswordHash().equals(hashIngresado)) {
            return false;
        }
        return servicio2FA.generarYEnviar(usuario, idIntento);
    }

    public static String hashSha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible", e);
        }
    }
}
