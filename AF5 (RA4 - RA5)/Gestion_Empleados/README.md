# 🏢 API REST — Gestión de Empleados (AF5 - PSP)

> **Asignatura:** Programación de Servicios y Procesos (PSP)  
> **Actividad:** AF5 — Resultados de Aprendizaje RA4 y RA5  
> **Tecnologías:** Java 21 · Spring Boot 4 · Spring Security · Spring Data JPA · H2 Database · Jakarta Validation  

---

## 📋 Índice

1. [Funcionamiento General y Pruebas (RA4)](#1-funcionamiento-general-y-pruebas-ra4)
2. [Justificación de Criterios RA4](#2-justificación-de-criterios-ra4)
3. [Medidas de Seguridad Aplicadas (RA5)](#3-medidas-de-seguridad-aplicadas-ra5)
4. [Guía de Pruebas con Postman](#4-guía-de-pruebas-con-postman)

---

## 📁 Estructura del Proyecto

```
src/main/java/psp/af5/Gestion_Empleados/
├── GestionDeEmpleadosApplication.java          ← Clase principal (punto de entrada)
├── modelo/
│   └── Empleado.java                           ← Entidad JPA con validaciones
├── repositorio/
│   └── EmpleadoRepositorio.java                ← Interfaz JPA (acceso a datos)
├── servicio/
│   └── EmpleadoServicio.java                   ← Lógica de negocio
├── controlador/
│   └── EmpleadoControlador.java                ← Controlador REST (endpoints)
└── configuracion/
    └── ConfiguracionSeguridad.java             ← Spring Security + BCrypt + Roles
```

---

## 1. Funcionamiento General y Pruebas (RA4)

### 🔗 Endpoints de la API

La API expone los siguientes endpoints REST en la ruta base `/api/empleados`:

| Método HTTP | Endpoint              | Descripción                    | Rol Requerido |
|-------------|----------------------|--------------------------------|---------------|
| `GET`       | `/api/empleados`      | Listar todos los empleados     | USER o ADMIN  |
| `GET`       | `/api/empleados/{id}` | Obtener un empleado por su ID  | USER o ADMIN  |
| `POST`      | `/api/empleados`      | Crear un nuevo empleado        | Solo ADMIN    |
| `PUT`       | `/api/empleados/{id}` | Actualizar un empleado         | Solo ADMIN    |
| `DELETE`    | `/api/empleados/{id}` | Eliminar un empleado           | Solo ADMIN    |

### 👤 Usuarios del Sistema

| Usuario | Contraseña | Rol   | Permisos                         |
|---------|------------|-------|----------------------------------|
| `admin` | `admin123` | ADMIN | Acceso completo (CRUD completo)  |
| `user`  | `user123`  | USER  | Solo lectura (GET)               |

> ⚠️ Las contraseñas se almacenan cifradas con **BCrypt**. No se usa `{noop}` en ningún momento.

### 🗄️ Consola H2

Accesible en: `http://localhost:8080/h2-console`

| Parámetro       | Valor                          |
|-----------------|--------------------------------|
| JDBC URL        | `jdbc:h2:file:./data/empleados_db` |
| User Name       | `admin`                        |
| Password        | `admin1234`                    |

---

## 2. Justificación de Criterios RA4

### RA4.a — Protocolos de comunicación y verbos HTTP

La API REST utiliza el **protocolo HTTP** como mecanismo de comunicación entre el cliente (Postman, navegador, etc.) y el servidor (Spring Boot + Tomcat embebido). Cada operación CRUD se mapea a un **verbo HTTP** específico:

- **GET** → Consultar recursos (listar todos o buscar por ID)
- **POST** → Crear un nuevo recurso
- **PUT** → Actualizar un recurso existente
- **DELETE** → Eliminar un recurso

Esto se implementa en la clase `EmpleadoControlador.java` con las anotaciones `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.

### RA4.b — Autenticación de usuarios

Se implementa **autenticación HTTP Basic Auth** mediante Spring Security. El cliente debe enviar sus credenciales (usuario y contraseña codificados en Base64) en la cabecera `Authorization` de cada petición HTTP. La configuración se encuentra en `ConfiguracionSeguridad.java`, donde se definen dos usuarios en memoria con contraseñas cifradas con BCrypt.

### RA4.c — Formato de intercambio de datos (JSON)

Todos los datos se transmiten en **formato JSON**, gracias a la anotación `@RestController` que configura automáticamente la serialización/deserialización de objetos Java a JSON (y viceversa) utilizando la librería Jackson integrada en Spring Boot.

**Ejemplo de recurso Empleado en JSON:**
```json
{
    "id": 1,
    "nombre": "Juan García",
    "puesto": "Desarrollador Senior",
    "salario": 35000.0
}
```

### RA4.d — Procesamiento de peticiones del cliente

Cada petición del cliente es procesada siguiendo el flujo **Controlador → Servicio → Repositorio**:

1. El **controlador** (`EmpleadoControlador`) recibe la petición HTTP y extrae los datos.
2. El **servicio** (`EmpleadoServicio`) ejecuta la lógica de negocio.
3. El **repositorio** (`EmpleadoRepositorio`) persiste o recupera los datos de la base de datos H2.
4. La respuesta se devuelve al cliente con el código HTTP apropiado (200, 201, 204, 400, 403, 404).

### RA4.e — Comunicación simultánea (multihilo)

Spring Boot utiliza **Apache Tomcat embebido** como servidor web, que gestiona un **pool de hilos** para atender múltiples peticiones HTTP de forma simultánea. Cada petición entrante se asigna a un hilo del pool, lo que permite la comunicación concurrente de varios clientes sin bloqueos. Esta configuración es automática y no requiere código adicional.

### RA4.f — Mecanismo de persistencia (JPA + H2)

- La clase `Empleado.java` está anotada con `@Entity`, lo que permite a **JPA/Hibernate** mapear automáticamente los campos de la clase a columnas de la tabla `empleados` en la base de datos **H2**.
- La base de datos se configura en modo **archivo local** (`jdbc:h2:file:./data/empleados_db`), lo que garantiza la **persistencia real** de los datos entre reinicios del servidor.
- La propiedad `spring.jpa.hibernate.ddl-auto=update` crea y actualiza las tablas automáticamente.

### RA4.g — Arquitectura multicapa

El proyecto sigue la **arquitectura multicapa estándar** de Spring:

| Capa              | Clase                        | Responsabilidad                              |
|-------------------|------------------------------|----------------------------------------------|
| **Controlador**   | `EmpleadoControlador.java`   | Recibir peticiones HTTP y devolver respuestas |
| **Servicio**      | `EmpleadoServicio.java`      | Lógica de negocio                            |
| **Repositorio**   | `EmpleadoRepositorio.java`   | Acceso a datos (JPA)                         |
| **Modelo**        | `Empleado.java`              | Representación de la entidad                 |
| **Configuración** | `ConfiguracionSeguridad.java`| Seguridad y autenticación                    |

Spring utiliza **inyección de dependencias (IoC)** para conectar las capas entre sí de forma desacoplada.

---

## 3. Medidas de Seguridad Aplicadas (RA5)

### RA5.h — Validación de datos de entrada

Se utilizan anotaciones de **Jakarta Validation** en la entidad `Empleado.java` para validar los datos antes de ser procesados:

| Campo     | Anotación    | Regla                                        |
|-----------|-------------|----------------------------------------------|
| `nombre`  | `@NotBlank`  | No puede ser nulo, vacío ni solo espacios    |
| `puesto`  | `@NotBlank`  | No puede ser nulo, vacío ni solo espacios    |
| `salario` | `@Positive`  | Debe ser un número mayor que cero            |

En el controlador, la anotación `@Valid` activa la validación automática. Si los datos no cumplen las reglas, se devuelve un **error 400 Bad Request** con los detalles de los campos inválidos:

```json
{
    "nombre": "El nombre del empleado es obligatorio",
    "salario": "El salario debe ser un número positivo mayor que cero"
}
```

### RA5.i — Técnicas criptográficas

Se utiliza el algoritmo **BCrypt** para cifrar las contraseñas de los usuarios. BCrypt es un algoritmo de hashing adaptativo que:

- Genera un **salt aleatorio** diferente para cada contraseña.
- Hace computacionalmente inviable un ataque de fuerza bruta o por tablas rainbow.
- Se configura en `ConfiguracionSeguridad.java` mediante un bean de `BCryptPasswordEncoder`.

### RA5.j — Políticas de acceso y autorización

Se definen reglas de acceso basadas en roles en la clase `ConfiguracionSeguridad.java`:

- Los endpoints **GET** (`/api/empleados`) son accesibles por **cualquier usuario autenticado** (rol USER o ADMIN).
- Todos los demás endpoints requieren específicamente el **rol ADMIN**.
- La consola H2 se permite sin autenticación para facilitar la revisión de datos durante la evaluación.

### RA5.k — Restricción de rutas según roles

La configuración de seguridad restringe las operaciones de escritura (POST, PUT, DELETE) al rol **ADMIN**:

```
GET    /api/empleados/**  → USER, ADMIN  ✅
POST   /api/empleados     → Solo ADMIN   🔒
PUT    /api/empleados/**  → Solo ADMIN   🔒
DELETE /api/empleados/**  → Solo ADMIN   🔒
```

Si un usuario con rol **USER** intenta una operación restringida, el servidor devuelve automáticamente un error **403 Forbidden**.

### RA5.l — Protección de contraseñas con BCrypt

Las contraseñas de los usuarios **nunca se almacenan en texto plano**. Se utiliza `BCryptPasswordEncoder` para codificarlas antes de guardarlas:

```java
// Ejemplo en ConfiguracionSeguridad.java:
.password(codificadorContrasenas.encode("admin123")) // Hash BCrypt con salt aleatorio
```

Cada codificación genera un hash diferente gracias al salt, por ejemplo:
- `admin123` → `$2a$10$X9gE5...` (primera ejecución)
- `admin123` → `$2a$10$Rk7Yf...` (segunda ejecución)

Ambos hashes son válidos para la misma contraseña pero completamente distintos, haciendo imposible la comparación directa.

### RA5.m — Protección frente a inyección SQL

Al utilizar **Spring Data JPA** y **JpaRepository**, todas las consultas a la base de datos se ejecutan de forma **parametrizada automáticamente**. Esto significa que los valores introducidos por el usuario nunca se concatenan directamente en las sentencias SQL, eliminando por completo el riesgo de ataques de inyección SQL.

### RA5.n — Autenticación y verificación de identidad

Se implementa el mecanismo de **HTTP Basic Auth**:

1. El cliente envía la cabecera `Authorization: Basic <credenciales_base64>` en cada petición.
2. Spring Security decodifica las credenciales y las compara con los usuarios almacenados.
3. Si las credenciales son válidas, se permite el acceso según el rol asignado.
4. Si las credenciales son inválidas, se devuelve un error **401 Unauthorized**.

### RA5.o — Cabeceras de seguridad HTTP

Spring Security aplica automáticamente las siguientes **cabeceras de seguridad** en cada respuesta HTTP:

| Cabecera                       | Función                                              |
|-------------------------------|------------------------------------------------------|
| `X-Content-Type-Options: nosniff` | Previene la interpretación errónea del tipo MIME  |
| `X-Frame-Options: SAMEORIGIN`     | Protege contra ataques de clickjacking            |
| `Cache-Control: no-cache`          | Evita que datos sensibles se almacenen en caché   |
| `X-XSS-Protection`                | Protección adicional contra Cross-Site Scripting  |

Además, se deshabilita **CSRF** porque la API REST es stateless (sin estado) y no utiliza cookies de sesión.

---

## 4. Guía de Pruebas con Postman

> 🚀 **¡Colección Lista para Importar!**
> Para facilitar la corrección, se incluye en la raíz del proyecto el archivo `Coleccion_Postman_AF5.json`.
> Solo tienes que abrir Postman, darle a **"Import"** y cargar este archivo. Te aparecerán automáticamente las 10 pruebas ya configuradas con sus URL, parámetros, cuerpos JSON y los usuarios correctos (Basic Auth) en cada petición.

### 🔧 Configurar la autenticación en Postman (Modo Manual)

Para cada petición en Postman:

1. Ir a la pestaña **"Authorization"** (o "Auth").
2. Seleccionar **Type: "Basic Auth"**.
3. Introducir las credenciales:
   - Para ADMIN: `admin` / `admin123`
   - Para USER: `user` / `user123`

> 📌 Asegurarse de que en la pestaña **"Headers"**, la cabecera `Content-Type` esté configurada como `application/json` para las peticiones POST y PUT.

---

### ✅ Prueba 1: Listar empleados (GET) — Acceso con USER

- **Método:** `GET`
- **URL:** `http://localhost:8080/api/empleados`
- **Auth:** Basic Auth → `user` / `user123`
- **Resultado esperado:** `200 OK` con la lista de empleados (puede estar vacía al inicio)

```json
[]
```

---

### ✅ Prueba 2: Crear un empleado (POST) — Acceso con ADMIN

- **Método:** `POST`
- **URL:** `http://localhost:8080/api/empleados`
- **Auth:** Basic Auth → `admin` / `admin123`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**

```json
{
    "nombre": "Juan García",
    "puesto": "Desarrollador Senior",
    "salario": 35000.0
}
```

- **Resultado esperado:** `201 Created`

```json
{
    "id": 1,
    "nombre": "Juan García",
    "puesto": "Desarrollador Senior",
    "salario": 35000.0
}
```

---

### ✅ Prueba 3: Crear segundo empleado (POST) — Acceso con ADMIN

- **Método:** `POST`
- **URL:** `http://localhost:8080/api/empleados`
- **Auth:** Basic Auth → `admin` / `admin123`
- **Body (raw JSON):**

```json
{
    "nombre": "María López",
    "puesto": "Analista de Datos",
    "salario": 30000.0
}
```

- **Resultado esperado:** `201 Created`

---

### ✅ Prueba 4: Obtener empleado por ID (GET)

- **Método:** `GET`
- **URL:** `http://localhost:8080/api/empleados/1`
- **Auth:** Basic Auth → `user` / `user123`
- **Resultado esperado:** `200 OK` con los datos del empleado con ID 1

---

### ✅ Prueba 5: Actualizar un empleado (PUT) — Acceso con ADMIN

- **Método:** `PUT`
- **URL:** `http://localhost:8080/api/empleados/1`
- **Auth:** Basic Auth → `admin` / `admin123`
- **Body (raw JSON):**

```json
{
    "nombre": "Juan García Actualizado",
    "puesto": "Director de Tecnología",
    "salario": 50000.0
}
```

- **Resultado esperado:** `200 OK` con los datos actualizados

---

### ✅ Prueba 6: Eliminar un empleado (DELETE) — Acceso con ADMIN

- **Método:** `DELETE`
- **URL:** `http://localhost:8080/api/empleados/2`
- **Auth:** Basic Auth → `admin` / `admin123`
- **Resultado esperado:** `204 No Content` (sin cuerpo de respuesta)

---

### ❌ Prueba 7: Crear empleado con USER (POST) — ACCESO DENEGADO

- **Método:** `POST`
- **URL:** `http://localhost:8080/api/empleados`
- **Auth:** Basic Auth → `user` / `user123`
- **Body (raw JSON):**

```json
{
    "nombre": "Intento Fallido",
    "puesto": "Test",
    "salario": 20000.0
}
```

- **Resultado esperado:** `403 Forbidden` ❌

> 📸 **Captura de pantalla:** Esta prueba demuestra que el rol USER no puede crear empleados. Se debe ver el código de error 403 en Postman.

---

### ❌ Prueba 8: Eliminar con USER (DELETE) — ACCESO DENEGADO

- **Método:** `DELETE`
- **URL:** `http://localhost:8080/api/empleados/1`
- **Auth:** Basic Auth → `user` / `user123`
- **Resultado esperado:** `403 Forbidden` ❌

---

### ❌ Prueba 9: Validación fallida (POST con datos inválidos)

- **Método:** `POST`
- **URL:** `http://localhost:8080/api/empleados`
- **Auth:** Basic Auth → `admin` / `admin123`
- **Body (raw JSON):**

```json
{
    "nombre": "",
    "puesto": "",
    "salario": -500
}
```

- **Resultado esperado:** `400 Bad Request` con los mensajes de error de validación:

```json
{
    "nombre": "El nombre del empleado es obligatorio",
    "puesto": "El puesto del empleado es obligatorio",
    "salario": "El salario debe ser un número positivo mayor que cero"
}
```

> 📸 **Captura de pantalla:** Esta prueba demuestra que las validaciones RA5.h funcionan correctamente, rechazando datos inválidos con un mensaje descriptivo.

---

### ❌ Prueba 10: Sin autenticación — ACCESO DENEGADO

- **Método:** `GET`
- **URL:** `http://localhost:8080/api/empleados`
- **Auth:** **No Auth** (sin credenciales)
- **Resultado esperado:** `401 Unauthorized`

> 📸 **Captura de pantalla:** Esta prueba demuestra que la API está protegida y requiere autenticación obligatoria.

---

## 🚀 Cómo Ejecutar el Proyecto

1. Abrir el proyecto en un IDE (IntelliJ IDEA, Eclipse, VS Code).
2. Ejecutar la clase `GestionDeEmpleadosApplication.java`.
3. El servidor se inicia en `http://localhost:8080`.
4. Abrir Postman y seguir las pruebas de la sección anterior.
5. Para ver los datos directamente en la BD, acceder a `http://localhost:8080/h2-console`.

---

> 📝 **Nota:** Todos los archivos Java contienen comentarios que hacen referencia directa a los criterios de evaluación RA4 y RA5 para facilitar la revisión por parte del profesor.
