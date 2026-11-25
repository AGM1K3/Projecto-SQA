package mx.uacm.sqa.twofa.controller;

import mx.uacm.sqa.twofa.service.ServicioAutenticacion;

public class AuthController {

    private final ServicioAutenticacion servicioAuth;

    public AuthController(ServicioAutenticacion servicioAuth) {
        this.servicioAuth = servicioAuth;
    }

    public String postLogin(String identificador, String password, String idIntento) {
        if (identificador == null || identificador.isBlank() || password == null || password.isBlank()) {
            return "ERROR: Complete los campos obligatorios.";
        }
        boolean iniciado = servicioAuth.autenticarYEnviar2FA(identificador, password, idIntento);
        return iniciado ? "VIEW: verificacion-2fa" : "ERROR: Credenciales inválidas.";
    }
}
