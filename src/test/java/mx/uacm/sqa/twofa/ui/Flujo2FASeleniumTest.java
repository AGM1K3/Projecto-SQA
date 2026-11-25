package mx.uacm.sqa.twofa.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.Duration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import mx.uacm.sqa.twofa.web.ServidorWeb2FA;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Flujo2FASeleniumTest {

    private static ServidorWeb2FA servidor;
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080/2fa-app";

    @BeforeAll
    static void levantarServidorLocal() throws IOException {
        servidor = new ServidorWeb2FA();
        servidor.iniciar(8080);
    }

    @AfterAll
    static void apagarServidorLocal() {
        if (servidor != null) {
            servidor.detener();
        }
    }

    @BeforeEach
    void iniciarNavegador() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void ui02_credencialesIncorrectasMuestranMensajeControlado() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("input-identificador")).sendKeys("usuario.prueba@uacm.edu.mx");
        driver.findElement(By.id("input-password")).sendKeys("ClaveIncorrecta");
        driver.findElement(By.id("btn-iniciar-sesion")).click();

        WebElement alerta = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("mensaje-error"))
        );
        assertEquals("Credenciales inválidas.", alerta.getText());
    }

    @Test
    @Order(2)
    void ui05_codigoIncorrectoRechazaAcceso() {
        driver.get(BASE_URL + "/verificacion-2fa");
        driver.findElement(By.id("input-codigo-2fa")).sendKeys("000000");
        driver.findElement(By.id("btn-validar-codigo")).click();

        WebElement alerta = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("mensaje-error"))
        );
        assertEquals("Código de verificación incorrecto.", alerta.getText());
    }

    @Test
    @Order(3)
    void ui01_ui03_ui04_ui10_flujoCompletoExitosoConcedeAccesoAreaProtegida() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("input-identificador")).sendKeys("usuario.prueba@uacm.edu.mx");
        driver.findElement(By.id("input-password")).sendKeys("ClavePrueba#2026");
        driver.findElement(By.id("btn-iniciar-sesion")).click();

        WebElement campoOtp = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("input-codigo-2fa"))
        );
        assertTrue(campoOtp.isDisplayed());

        campoOtp.sendKeys("482913");
        driver.findElement(By.id("btn-validar-codigo")).click();

        WebElement tituloArea = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("titulo-area-protegida"))
        );
        assertEquals("Acceso Concedido al Área Protegida", tituloArea.getText());
    }
}
