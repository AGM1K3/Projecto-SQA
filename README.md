# Proyecto 2FA — Entrega Final SQA (UACM)

## Integrantes
- **Ordaz Reyes Evelyn Julieta** (230110233) — Scrum Master / QA / Documentación
- **Aguirre López Miguel Ángel** (230111111) — Product Owner / Desarrollo

## Requisitos del Entorno
- Java JDK 21 (`java -version`)
- Apache Maven 3.9+ (`mvn -version`)
- Eclipse IDE o Visual Studio Code
- Google Chrome (para pruebas funcionales con Selenium WebDriver 4)

## Compilación y Empaquetado Ejecutable
```bash
mvn clean package
java -jar target/proyecto-2fa-1.0.0.jar
```
URL de prueba: `http://localhost:8080/2fa-app/login`

## Ejecución de Pruebas
- **Pruebas unitarias (JUnit 5 + Mockito):**
  ```bash
  mvn test -Dtest=ServicioSegundoFactorTest,ServicioAutenticacionTest
  ```
- **Pruebas funcionales web (Selenium WebDriver 4):**
  ```bash
  mvn test -Dtest=Flujo2FASeleniumTest
  ```
