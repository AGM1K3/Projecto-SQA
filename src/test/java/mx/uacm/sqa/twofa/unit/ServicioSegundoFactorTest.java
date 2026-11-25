package mx.uacm.sqa.twofa.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mx.uacm.sqa.twofa.dao.CodigoVerificacionDAO;
import mx.uacm.sqa.twofa.model.CodigoVerificacion;
import mx.uacm.sqa.twofa.model.ResultadoValidacion;
import mx.uacm.sqa.twofa.model.Usuario;
import mx.uacm.sqa.twofa.service.ServicioCorreo;
import mx.uacm.sqa.twofa.service.ServicioSegundoFactor;

class ServicioSegundoFactorTest {

    private CodigoVerificacionDAO codigoDAO;
    private ServicioCorreo servicioCorreo;
    private Clock relojFijo;
    private ServicioSegundoFactor servicio;

    @BeforeEach
    void setUp() {
        codigoDAO = mock(CodigoVerificacionDAO.class);
        servicioCorreo = mock(ServicioCorreo.class);
        relojFijo = Clock.fixed(Instant.parse("2026-11-18T10:00:00Z"), ZoneOffset.UTC);
        servicio = new ServicioSegundoFactor(codigoDAO, servicioCorreo, relojFijo);
    }

    @Test
    void cp04_generacionYEnvioDeCodigoDebePersistirYNotificar() {
        Usuario u = new Usuario(1, "miguel.aguirre", "hash123", "miguel.prueba@uacm.edu.mx");
        when(servicioCorreo.enviarCodigo(eq("miguel.prueba@uacm.edu.mx"), anyString())).thenReturn(true);

        boolean enviado = servicio.generarYEnviar(u, "INT-01");

        assertTrue(enviado);
        verify(codigoDAO, times(1)).guardar(any(CodigoVerificacion.class));
    }

    @Test
    void cp05_codigoCorrectoYVigenteDebeValidarse() {
        LocalDateTime expira = LocalDateTime.now(relojFijo).plusMinutes(5);
        CodigoVerificacion cv = new CodigoVerificacion(1, "INT-01", "482913", expira, false, 0);
        when(codigoDAO.buscarPorIntento("INT-01")).thenReturn(Optional.of(cv));

        ResultadoValidacion resultado = servicio.validarCodigo("INT-01", "482913");

        assertEquals(ResultadoValidacion.VALIDO, resultado);
        verify(codigoDAO).marcarUtilizado(1);
    }

    @Test
    void cp06_codigoIncorrectoDebeRechazarse() {
        LocalDateTime expira = LocalDateTime.now(relojFijo).plusMinutes(5);
        CodigoVerificacion cv = new CodigoVerificacion(1, "INT-01", "482913", expira, false, 0);
        when(codigoDAO.buscarPorIntento("INT-01")).thenReturn(Optional.of(cv));

        ResultadoValidacion resultado = servicio.validarCodigo("INT-01", "000000");

        assertEquals(ResultadoValidacion.CODIGO_INCORRECTO, resultado);
        verify(codigoDAO).incrementarIntentos(1);
    }

    @Test
    void cp07_codigoExpiradoDebeRechazarse() {
        LocalDateTime expira = LocalDateTime.now(relojFijo).minusSeconds(1);
        CodigoVerificacion cv = new CodigoVerificacion(1, "INT-01", "482913", expira, false, 0);
        when(codigoDAO.buscarPorIntento("INT-01")).thenReturn(Optional.of(cv));

        ResultadoValidacion resultado = servicio.validarCodigo("INT-01", "482913");

        assertEquals(ResultadoValidacion.CODIGO_EXPIRADO, resultado);
        verify(codigoDAO, never()).marcarUtilizado(anyInt());
    }

    @Test
    void cp08_codigoReutilizadoDebeRechazarse() {
        LocalDateTime expira = LocalDateTime.now(relojFijo).plusMinutes(5);
        CodigoVerificacion cv = new CodigoVerificacion(1, "INT-01", "482913", expira, true, 0);
        when(codigoDAO.buscarPorIntento("INT-01")).thenReturn(Optional.of(cv));

        assertEquals(ResultadoValidacion.CODIGO_REUTILIZADO, servicio.validarCodigo("INT-01", "482913"));
    }

    @Test
    void cp09_limiteDeIntentosDebeBloquearValidacion() {
        LocalDateTime expira = LocalDateTime.now(relojFijo).plusMinutes(5);
        CodigoVerificacion cv = new CodigoVerificacion(1, "INT-01", "482913", expira, false, 2);
        when(codigoDAO.buscarPorIntento("INT-01")).thenReturn(Optional.of(cv));

        assertEquals(ResultadoValidacion.LIMITE_INTENTOS_EXCEDIDO, servicio.validarCodigo("INT-01", "999999"));
    }

    @Test
    void cp12_codigoVacioDebeRechazarse() {
        assertEquals(ResultadoValidacion.CAMPO_VACIO, servicio.validarCodigo("INT-01", "   "));
    }

    @Test
    void cp15_codigoDeOtroIntentoNoDebeAceptarse() {
        when(codigoDAO.buscarPorIntento("INT-02")).thenReturn(Optional.empty());
        assertEquals(ResultadoValidacion.INTENTO_NO_VALIDO, servicio.validarCodigo("INT-02", "482913"));
    }
}
