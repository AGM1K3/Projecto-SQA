package mx.uacm.sqa.twofa.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mx.uacm.sqa.twofa.dao.UsuarioDAO;
import mx.uacm.sqa.twofa.model.Usuario;
import mx.uacm.sqa.twofa.service.ServicioAutenticacion;
import mx.uacm.sqa.twofa.service.ServicioSegundoFactor;

class ServicioAutenticacionTest {

    private UsuarioDAO usuarioDAO;
    private ServicioSegundoFactor servicio2FA;
    private ServicioAutenticacion servicioAuth;

    @BeforeEach
    void setUp() {
        usuarioDAO = mock(UsuarioDAO.class);
        servicio2FA = mock(ServicioSegundoFactor.class);
        servicioAuth = new ServicioAutenticacion(usuarioDAO, servicio2FA);
    }

    @Test
    void cp01_y_cp03_credencialesValidasInicianSegundoFactor() {
        String pass = "ClavePrueba#2026";
        Usuario u = new Usuario(1, "usuario.prueba@uacm.edu.mx",
                ServicioAutenticacion.hashSha256(pass), "usuario.prueba@uacm.edu.mx");
        when(usuarioDAO.buscarPorIdentificador("usuario.prueba@uacm.edu.mx")).thenReturn(Optional.of(u));
        when(servicio2FA.generarYEnviar(u, "INT-100")).thenReturn(true);

        boolean resultado = servicioAuth.autenticarYEnviar2FA("usuario.prueba@uacm.edu.mx", pass, "INT-100");

        assertTrue(resultado);
        verify(servicio2FA, times(1)).generarYEnviar(u, "INT-100");
    }

    @Test
    void cp02_credencialesIncorrectasNoInicianSegundoFactor() {
        Usuario u = new Usuario(1, "usuario.prueba@uacm.edu.mx",
                ServicioAutenticacion.hashSha256("ClavePrueba#2026"), "usuario.prueba@uacm.edu.mx");
        when(usuarioDAO.buscarPorIdentificador("usuario.prueba@uacm.edu.mx")).thenReturn(Optional.of(u));

        boolean resultado = servicioAuth.autenticarYEnviar2FA("usuario.prueba@uacm.edu.mx", "Erronea", "INT-101");

        assertFalse(resultado);
        verify(servicio2FA, never()).generarYEnviar(any(), anyString());
    }

    @Test
    void cp14_hashNoExponePasswordEnTextoPlano() {
        String clave = "ClavePrueba#2026";
        String hash = ServicioAutenticacion.hashSha256(clave);
        assertNotEquals(clave, hash);
        assertEquals(64, hash.length());
    }
}
