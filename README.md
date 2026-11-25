
# Sistema de Autenticación de Dos Factores (2FA)

### Proyecto de Aseguramiento de la Calidad de Software (SQA) — UACM

---

## Descripción del proyecto

El presente proyecto consiste en el desarrollo de un **Sistema de Autenticación de Dos Factores (2FA)**, cuyo objetivo es reforzar la seguridad durante el proceso de inicio de sesión mediante dos etapas de verificación.

El sistema permite autenticar a un usuario mediante sus credenciales y posteriormente validar un código de verificación de seis dígitos como segundo factor de autenticación.

El proyecto contempla el desarrollo del software, la documentación y la aplicación de pruebas para verificar el correcto funcionamiento de sus funcionalidades.

## Objetivo

Implementar y evaluar un sistema de autenticación de dos factores que permita fortalecer el proceso de acceso, validar las credenciales del usuario y verificar códigos temporales, considerando diferentes escenarios de éxito y error.

## ⚙️ Tecnologías utilizadas

| Tecnología | Aplicación |
|---|---|
| Java 21 | Lenguaje de programación |
| Apache Maven | Gestión y construcción del proyecto |
| JUnit 5 | Pruebas unitarias |
| Mockito | Simulación de dependencias |
| Selenium WebDriver 4 | Pruebas funcionales de interfaz |
| Google Chrome | Navegador para pruebas |
| SHA-256 | Hash de contraseñas |

## Funcionalidades principales

- Inicio de sesión mediante identificador y contraseña.
- Validación de credenciales del usuario.
- Generación de códigos de verificación de seis dígitos.
- Validación del segundo factor de autenticación.
- Vigencia del código de verificación de cinco minutos.
- Control de intentos de validación.
- Rechazo de códigos incorrectos, expirados o reutilizados.
- Acceso al área protegida cuando la autenticación es exitosa.

## Pruebas de software

El proyecto incorpora pruebas unitarias y funcionales para comprobar el comportamiento de los principales componentes del sistema.

### Pruebas unitarias

Se utiliza JUnit 5 junto con Mockito para evaluar los servicios de autenticación y segundo factor.

**Clases de prueba:**
- `ServicioAutenticacionTest`
- `ServicioSegundoFactorTest`

### Pruebas funcionales

Se utiliza Selenium WebDriver 4 para verificar el flujo de autenticación desde la interfaz web.

**Clase de prueba:**
- `Flujo2FASeleniumTest`

Se contemplan escenarios como:
- Inicio de sesión con credenciales incorrectas.
- Rechazo de códigos de verificación incorrectos.
- Validación del código correcto.
- Acceso concedido al área protegida.

## Estructura del proyecto

```text
Projecto-SQA-main/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── mx/uacm/sqa/twofa/
│   │           ├── controller/
│   │           ├── dao/
│   │           ├── model/
│   │           ├── service/
│   │           └── web/
│   │
│   └── test/
│       └── java/
│           └── mx/uacm/sqa/twofa/
│               ├── ui/
│               └── unit/
│
├── Proyecto_2FA SQA-E1.pdf
├── Proyecto_2FA SQA-E2.pdf
├── Proyecto_2FA SQA-EFpdf.pdf
├── Proyecto_2FASAQ-E3.pdf
│
├── pom.xml
├── .gitignore
└── README.md
```

## Requisitos del entorno

Para ejecutar el proyecto se requiere:

- Java JDK 21.
- Apache Maven 3.9 o superior.
- Eclipse IDE o Visual Studio Code.
- Google Chrome para las pruebas funcionales con Selenium WebDriver.

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

### 2. Entrar a la carpeta del proyecto

```bash
cd Projecto-SQA-main
```

### 3. Compilar y generar el ejecutable

```bash
mvn clean package
```

### 4. Ejecutar la aplicación

```bash
java -jar target/proyecto-2fa-1.0.0.jar
```

### 5. Acceder al sistema

Abrir el navegador y entrar a:

```text
http://localhost:8080/2fa-app/login
```

## Ejecución de pruebas

### Ejecutar pruebas unitarias

```bash
mvn test -Dtest=ServicioSegundoFactorTest,ServicioAutenticacionTest
```

### Ejecutar pruebas funcionales con Selenium

```bash
mvn test -Dtest=Flujo2FASeleniumTest
```

**Nota:** Para las pruebas funcionales se requiere Google Chrome y un entorno compatible con Selenium WebDriver.

## Documentación del proyecto

El repositorio incluye los documentos correspondientes a las diferentes entregas del proyecto:

- **SQA-E1:** Primera entrega del proyecto.
- **SQA-E2:** Segunda entrega del proyecto.
- **SQA-E3:** Tercera entrega del proyecto.
- **SQA-Entrega Final:** Documentación de la entrega final.

Los archivos PDF se encuentran disponibles en la raíz del repositorio.

## Integrantes

- **Ordaz Reyes Evelyn Julieta** — Scrum Master / QA / Documentación.
- **Aguirre López Miguel Ángel** — Product Owner / Desarrollo.


**Universidad Autónoma de la Ciudad de México (UACM)**

Proyecto de Aseguramiento de la Calidad de Software (SQA).

---

<p align="center">
  <strong>Proyecto 2FA — UACM</strong><br>
  Sistema de Autenticación de Dos Factores
</p>
