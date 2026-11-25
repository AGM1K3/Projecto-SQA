
# Sistema de Autenticación de Dos Factores (2FA)

<p align="center">
  <strong>Proyecto de Aseguramiento de la Calidad de Software (SQA)</strong><br>
  Universidad Autónoma de la Ciudad de México — Plantel Cuautepec
</p>

---

## Descripción del proyecto

El proyecto consiste en el desarrollo de un **Sistema de Autenticación de Dos Factores (2FA)**, cuyo propósito es fortalecer la seguridad del proceso de inicio de sesión mediante dos etapas de verificación.

En la primera etapa, el usuario proporciona su identificador y contraseña. Una vez que las credenciales son validadas correctamente, el sistema solicita un segundo factor de autenticación mediante un código temporal de seis dígitos asociado al correo electrónico registrado.

El acceso al área protegida se concede únicamente cuando el código ingresado es correcto, se encuentra vigente, no ha sido utilizado y corresponde al intento de autenticación activo.

El proyecto contempla el análisis de requerimientos, diseño del sistema, implementación, documentación y pruebas de software.

## Objetivo general

Desarrollar un sistema de autenticación de dos factores que permita reforzar la seguridad del acceso de los usuarios, aplicando mecanismos de validación de credenciales y códigos temporales, así como técnicas de aseguramiento de la calidad mediante pruebas automatizadas.

## ⚙️ Tecnologías y herramientas

| Tecnología / Herramienta | Uso |
|---|---|
| Java 21 | Desarrollo del sistema |
| Apache Maven | Administración y construcción del proyecto |
| JUnit 5 | Ejecución de pruebas unitarias |
| Mockito 5 | Simulación de dependencias en pruebas |
| Selenium WebDriver 4 | Automatización de pruebas funcionales |
| Google Chrome | Navegador para pruebas funcionales |
| SHA-256 | Generación del hash de contraseñas |
| Git / GitHub | Control de versiones y almacenamiento del proyecto |

## Funcionalidades del sistema

- Autenticación mediante identificador y contraseña.
- Validación de credenciales del usuario.
- Generación de códigos OTP de seis dígitos.
- Envío del código mediante el servicio de correo definido por el sistema.
- Validación del segundo factor de autenticación.
- Vigencia del código de verificación de cinco minutos.
- Control de un máximo de tres intentos de validación.
- Rechazo de códigos incorrectos, expirados o reutilizados.
- Reenvío del código de verificación.
- Acceso al área protegida después de completar correctamente los dos factores.

## Arquitectura del sistema

El sistema utiliza una organización basada en el patrón **Modelo-Vista-Controlador (MVC)**, complementada con una capa de servicios y una capa de acceso a datos (DAO).

### Componentes principales

| Componente | Responsabilidad |
|---|---|
| Controller | Gestiona las solicitudes de autenticación y verificación 2FA. |
| Model | Representa los usuarios, códigos de verificación y resultados de validación. |
| Service | Contiene la lógica de autenticación y segundo factor. |
| DAO | Gestiona el acceso y almacenamiento de usuarios y códigos. |
| Web | Implementa el servidor web y las páginas de interacción. |

### Clases principales

- `AuthController`
- `TwoFactorController`
- `ServicioAutenticacion`
- `ServicioSegundoFactor`
- `ServicioCorreo`
- `Usuario`
- `CodigoVerificacion`
- `ResultadoValidacion`
- `UsuarioDAO`
- `CodigoVerificacionDAO`

## Aseguramiento de la calidad del software

El proyecto incorpora pruebas automatizadas para verificar el funcionamiento de los componentes y el flujo de autenticación.

### Pruebas unitarias

Se utiliza **JUnit 5 y Mockito** para comprobar el comportamiento de los servicios principales.

Clases de prueba:

- `ServicioAutenticacionTest`
- `ServicioSegundoFactorTest`

Se verifican escenarios como:

- Autenticación con credenciales válidas.
- Rechazo de credenciales incorrectas.
- Generación y almacenamiento del código.
- Validación de códigos correctos.
- Rechazo de códigos incorrectos.
- Expiración del código.
- Reutilización de códigos.
- Límite de intentos permitidos.
- Validación de campos vacíos.
- Rechazo de códigos correspondientes a otro intento.

### Pruebas funcionales

Se utiliza **Selenium WebDriver 4** para automatizar la interacción con la aplicación web.

Clase de prueba:

- `Flujo2FASeleniumTest`

Se comprueban los siguientes escenarios:

- Inicio de sesión con credenciales incorrectas.
- Rechazo de códigos de verificación incorrectos.
- Ejecución del flujo completo de autenticación.
- Acceso al área protegida después de superar las verificaciones.

## Estructura del proyecto

```text
Projecto-SQA-main/
│
├── src/
│   │
│   ├── main/
│   │   └── java/
│   │       └── mx/uacm/sqa/twofa/
│   │           │
│   │           ├── controller/
│   │           │   ├── AuthController.java
│   │           │   └── TwoFactorController.java
│   │           │
│   │           ├── dao/
│   │           │   ├── CodigoVerificacionDAO.java
│   │           │   ├── CodigoVerificacionDAOEnMemoria.java
│   │           │   ├── UsuarioDAO.java
│   │           │   └── UsuarioDAOEnMemoria.java
│   │           │
│   │           ├── model/
│   │           │   ├── CodigoVerificacion.java
│   │           │   ├── ResultadoValidacion.java
│   │           │   └── Usuario.java
│   │           │
│   │           ├── service/
│   │           │   ├── ServicioAutenticacion.java
│   │           │   ├── ServicioCorreo.java
│   │           │   └── ServicioSegundoFactor.java
│   │           │
│   │           └── web/
│   │               └── ServidorWeb2FA.java
│   │
│   └── test/
│       └── java/
│           └── mx/uacm/sqa/twofa/
│               │
│               ├── ui/
│               │   └── Flujo2FASeleniumTest.java
│               │
│               └── unit/
│                   ├── ServicioAutenticacionTest.java
│                   └── ServicioSegundoFactorTest.java
│
├── Proyecto_2FA SQA-E1.pdf
├── Proyecto_2FA SQA-E2.pdf
├── Proyecto_2FASAQ-E3.pdf
├── Proyecto_2FA SQA-EFpdf.pdf
│
├── pom.xml
├── .gitignore
└── README.md
```

## Requisitos del entorno

Para compilar y ejecutar el proyecto se requiere:

- Java JDK 21.
- Apache Maven 3.9 o superior.
- Eclipse IDE o Visual Studio Code.
- Google Chrome para las pruebas funcionales con Selenium WebDriver 4.

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

### 2. Entrar a la carpeta del proyecto

```bash
cd Projecto-SQA-main
```

### 3. Compilar y empaquetar

```bash
mvn clean package
```

### 4. Ejecutar la aplicación

```bash
java -jar target/proyecto-2fa-1.0.0.jar
```

### 5. Abrir el sistema

En el navegador, ingresar a la siguiente dirección:

```text
http://localhost:8080/2fa-app/login
```

## Ejecución de pruebas

### Ejecutar las pruebas unitarias

```bash
mvn test -Dtest=ServicioSegundoFactorTest,ServicioAutenticacionTest
```

### Ejecutar las pruebas funcionales con Selenium

```bash
mvn test -Dtest=Flujo2FASeleniumTest
```

**Nota:** Las pruebas funcionales requieren Google Chrome y un entorno compatible con Selenium WebDriver.

## Documentación del proyecto

El repositorio incluye los documentos correspondientes a las diferentes etapas de desarrollo:

| Documento | Contenido |
|---|---|
| Proyecto 2FA - SQA Entrega 1 | Organización del proyecto, modelo de desarrollo y estándares de calidad. |
| Proyecto 2FA - SQA Entrega 2 | Análisis y documentación de requerimientos. |
| Proyecto 2FA - SQA Entrega 3 | Diseño del sistema y modelado. |
| Proyecto 2FA - SQA Entrega Final | Implementación, ejecución y resultados del proyecto. |

Los documentos completos se encuentran disponibles en formato PDF en la raíz del repositorio.

## Integrantes del equipo

- **Ordaz Reyes Evelyn Julieta** — Scrum Master / QA / Documentación.
- **Aguirre López Miguel Ángel** — Product Owner / Desarrollo.




**Universidad Autónoma de la Ciudad de México (UACM)**

**Plantel:** Cuautepec

**Carrera:** Licenciatura en Ingeniería de Software

**Asignatura:** Aseguramiento de la Calidad de Software (SQA)

---

<p align="center">
  <strong>PROYECTO 2FA — UACM</strong><br>
  Sistema de Autenticación de Dos Factores
</p>
