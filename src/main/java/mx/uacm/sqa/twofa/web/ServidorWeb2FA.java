package mx.uacm.sqa.twofa.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import mx.uacm.sqa.twofa.controller.AuthController;
import mx.uacm.sqa.twofa.controller.TwoFactorController;
import mx.uacm.sqa.twofa.dao.CodigoVerificacionDAOEnMemoria;
import mx.uacm.sqa.twofa.dao.UsuarioDAOEnMemoria;
import mx.uacm.sqa.twofa.model.CodigoVerificacion;
import mx.uacm.sqa.twofa.model.Usuario;
import mx.uacm.sqa.twofa.service.ServicioAutenticacion;
import mx.uacm.sqa.twofa.service.ServicioSegundoFactor;

public class ServidorWeb2FA {

    private HttpServer server;
    private final AuthController authController;
    private final TwoFactorController twoFactorController;
    private final CodigoVerificacionDAOEnMemoria codigoDAO;
    private final Usuario usuarioDemo;
    private static final String INTENTO_DEMO = "INTENTO-SESION-ACTIVA";

    public ServidorWeb2FA() {
        UsuarioDAOEnMemoria usuarioDAO = new UsuarioDAOEnMemoria();
        this.usuarioDemo = usuarioDAO.buscarPorIdentificador("usuario.prueba@uacm.edu.mx").orElseThrow();
        this.codigoDAO = new CodigoVerificacionDAOEnMemoria();
        ServicioSegundoFactor servicio2FA = new ServicioSegundoFactor(
                codigoDAO,
                (correo, otp) -> true,
                Clock.systemDefaultZone()
        );
        servicio2FA.setCodigoFijoPruebas("482913");
        ServicioAutenticacion servicioAuth = new ServicioAutenticacion(usuarioDAO, servicio2FA);

        this.authController = new AuthController(servicioAuth);
        this.twoFactorController = new TwoFactorController(servicio2FA);
    }

    public void iniciar(int puerto) throws IOException {
        server = HttpServer.create(new InetSocketAddress(puerto), 0);

        server.createContext("/2fa-app/login", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviarHtml(exchange, paginaLogin(""));
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> params = leerFormulario(exchange);
                String res = authController.postLogin(
                        params.get("identificador"),
                        params.get("password"),
                        INTENTO_DEMO
                );
                if (res.startsWith("VIEW:")) {
                    redirigir(exchange, "/2fa-app/verificacion-2fa");
                } else {
                    String msg = res.replace("ERROR: ", "");
                    enviarHtml(exchange, paginaLogin(msg));
                }
            }
        });

        server.createContext("/2fa-app/verificacion-2fa", exchange -> {
            if (codigoDAO.buscarPorIntento(INTENTO_DEMO).isEmpty()) {
                codigoDAO.guardar(new CodigoVerificacion(
                        INTENTO_DEMO, "482913", LocalDateTime.now().plusMinutes(5), false, 0
                ));
            }

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviarHtml(exchange, pagina2FA("", ""));
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> params = leerFormulario(exchange);
                String res = twoFactorController.postVerificarCodigo(INTENTO_DEMO, params.get("codigo"));
                if (res.startsWith("VIEW:")) {
                    redirigir(exchange, "/2fa-app/area-protegida");
                } else {
                    String msg = traducirError2FA(res.replace("ERROR: ", ""));
                    enviarHtml(exchange, pagina2FA(msg, ""));
                }
            }
        });

        server.createContext("/2fa-app/reenviar-2fa", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String res = twoFactorController.postReenviarCodigo(usuarioDemo, INTENTO_DEMO);
                if (res.startsWith("INFO:")) {
                    enviarHtml(exchange, pagina2FA("", res.replace("INFO: ", "")));
                } else {
                    enviarHtml(exchange, pagina2FA("No fue posible reenviar el código.", ""));
                }
            }
        });

        server.createContext("/2fa-app/area-protegida", exchange -> {
            enviarHtml(exchange, paginaAreaProtegida());
        });

        server.setExecutor(null);
        server.start();
    }

    public void detener() {
        if (server != null) {
            server.stop(0);
        }
    }

    private String traducirError2FA(String codigoEnum) {
        return switch (codigoEnum) {
            case "CAMPO_VACIO" -> "Ingrese el código de verificación.";
            case "FORMATO_INVALIDO" -> "El código debe contener 6 dígitos.";
            case "CODIGO_INCORRECTO" -> "Código de verificación incorrecto.";
            case "CODIGO_EXPIRADO" -> "El código ha expirado. Solicite uno nuevo.";
            case "CODIGO_REUTILIZADO" -> "Este código ya fue utilizado.";
            case "LIMITE_INTENTOS_EXCEDIDO" -> "Se superó el límite de intentos permitidos.";
            default -> "No fue posible validar el código.";
        };
    }

    private Map<String, String> leerFormulario(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    private void redirigir(HttpExchange exchange, String destino) throws IOException {
        exchange.getResponseHeaders().add("Location", destino);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    private void enviarHtml(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String paginaLogin(String error) {
        String alerta = error.isEmpty() ? "" :
                "<p id='mensaje-error' style='color:#b91c1c;font-weight:bold;'>" + error + "</p>";
        return "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>Inicio de Sesión - Proyecto 2FA</title></head>" +
               "<body style='font-family:sans-serif;max-width:420px;margin:40px auto;'>" +
               "<h2>Sistema de Autenticación 2FA - UACM</h2>" + alerta +
               "<form method='POST' action='/2fa-app/login'>" +
               "<label for='input-identificador'>Identificador / Correo:</label><br>" +
               "<input id='input-identificador' name='identificador' type='text' style='width:100%;margin-bottom:10px;'><br>" +
               "<label for='input-password'>Contraseña:</label><br>" +
               "<input id='input-password' name='password' type='password' style='width:100%;margin-bottom:15px;'><br>" +
               "<button id='btn-iniciar-sesion' type='submit'>Iniciar sesión</button>" +
               "</form></body></html>";
    }

    private String pagina2FA(String error, String info) {
        String alerta = error.isEmpty() ? "" :
                "<p id='mensaje-error' style='color:#b91c1c;font-weight:bold;'>" + error + "</p>";
        String aviso = info.isEmpty() ? "" :
                "<p id='mensaje-info' style='color:#1d4ed8;font-weight:bold;'>" + info + "</p>";
        return "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>Verificación 2FA</title></head>" +
               "<body style='font-family:sans-serif;max-width:420px;margin:40px auto;'>" +
               "<h2>Verificación en Dos Pasos</h2>" +
               "<p>Se ha enviado un código temporal de 6 dígitos a su correo registrado.</p>" + alerta + aviso +
               "<form method='POST' action='/2fa-app/verificacion-2fa'>" +
               "<label for='input-codigo-2fa'>Código temporal (6 dígitos):</label><br>" +
               "<input id='input-codigo-2fa' name='codigo' type='text' maxlength='6' style='width:100%;margin-bottom:15px;'><br>" +
               "<button id='btn-validar-codigo' type='submit'>Verificar código</button>" +
               "</form>" +
               "<form method='POST' action='/2fa-app/reenviar-2fa' style='margin-top:10px;'>" +
               "<button id='btn-reenviar-codigo' type='submit'>Reenviar código</button>" +
               "</form></body></html>";
    }

    private String paginaAreaProtegida() {
        return "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>Área Protegida</title></head>" +
               "<body style='font-family:sans-serif;max-width:520px;margin:40px auto;'>" +
               "<h1 id='titulo-area-protegida' style='color:#15803d;'>Acceso Concedido al Área Protegida</h1>" +
               "<p>Autenticación de dos factores completada exitosamente.</p>" +
               "</body></html>";
    }

    public static void main(String[] args) throws IOException {
        ServidorWeb2FA app = new ServidorWeb2FA();
        app.iniciar(8080);
        System.out.println("Servidor 2FA activo en http://localhost:8080/2fa-app/login");
    }
}
