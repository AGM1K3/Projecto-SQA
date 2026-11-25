# Proyecto 2FA (SQA - UACM)

Sistema de autenticación de dos factores: credenciales + código temporal de un solo uso enviado por correo.

## Requisitos
- JDK 21 (en Eclipse: Window > Preferences > Java > Installed JREs)
- Maven 3.9 o superior (Eclipse trae m2e integrado)
- Google Chrome (solo para las pruebas de interfaz)

## Abrir en Eclipse
File > Import > Maven > Existing Maven Projects > seleccionar esta carpeta.

## Pruebas unitarias (JUnit 5 + Mockito)
- Eclipse: clic derecho en `src/test/java/.../unit` > Run As > JUnit Test.
- Terminal: `mvn test`

Las pruebas llevan etiqueta de técnica: `caja-negra`, `caja-blanca` o `caja-gris`.

## Pruebas de interfaz (Selenium, no unitarias)
Están excluidas de `mvn test` porque necesitan Chrome.
- Terminal: `mvn test -Pui` (agrega `-Dui.headed=true` para ver el navegador)
- Eclipse: Run As > JUnit Test sobre `Flujo2FASeleniumTest`.

## Ejecutar la aplicación de demostración
Ejecutar `mx.uacm.sqa.twofa.web.ServidorWeb2FA` y abrir http://localhost:8080/2fa-app/login
Usuario de prueba: `usuario.prueba@uacm.edu.mx` / `ClavePrueba#2026` (datos ficticios).
El correo está SIMULADO: el código aparece en la consola. Solo para la demo.

## Parámetros propuestos (pendientes de aprobación, RN-03 y RN-04)
- Vigencia del código: 5 minutos
- Intentos de código: 3
- Intentos de contraseña antes de bloqueo: 3, con bloqueo de 5 minutos

## Limitaciones conocidas
- Datos en memoria (sin base de datos real).
- Reenvío de código (CU-04 / CP-10) no implementado: la política de reenvío está pendiente (RN-04).
- Contraseñas con SHA-256 sin sal: suficiente para el curso; en producción usar bcrypt, Argon2 o PBKDF2.
