# Evaluación Desarrollador Java Senior 2026

Proyecto realizado para la evaluación técnica de Desarrollador Java Senior.

La solución está dividida en dos APIs desarrolladas con Spring Boot y un front en Angular. El flujo principal consiste en recibir una operación desde el front, cifrar el campo `secreto`, validar la petición en la primera API y posteriormente enviar la información a la segunda API para almacenarla en H2.

## Estructura del proyecto

```text
evaluacion-java-senior-2026/
├── api-operaciones/
├── api-transacciones/
├── front-operaciones/
└── README.md
```

- `api-operaciones`: API de entrada. Valida el request, descifra el campo `secreto` con AES-256-GCM y consume `api-transacciones` mediante OpenFeign.
- `api-transacciones`: administra la persistencia de transacciones y usuarios utilizando Spring Data JPA y H2.
- `front-operaciones`: aplicación Angular 15 para login, registro de operaciones y consulta paginada de transacciones.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.5.16
- Spring Cloud OpenFeign 2025.0.3
- Spring Data JPA
- H2 Database
- Bean Validation
- BCrypt
- AES-256-GCM
- Angular 15
- Swagger / OpenAPI con springdoc 2.8.17
- Maven

## Flujo general

```text
Angular
   |
   | POST operación
   | secreto cifrado con AES-256-GCM
   v
api-operaciones :8080
   |
   | Validaciones con Bean Validation
   | Descifrado del secreto
   | OpenFeign
   v
api-transacciones :8081
   |
   | JPA / H2
   | Generación de referencia de 6 dígitos
   | Estatus inicial: Aprobada
   v
Respuesta al front
```

## Requisitos

Antes de ejecutar el proyecto se requiere:

- Java 21
- Maven 3.x
- Node.js 18.x
- npm
- Angular CLI 15 opcional, ya que también puede utilizarse `npx ng`

Para validar las versiones:

```bash
java -version
mvn -version
node -v
npm -v
```

## Usuario de prueba

La aplicación crea un usuario de prueba al iniciar `api-transacciones`.

- Usuario: `admin`
- Password: `Admin123*`

El password se almacena utilizando BCrypt.

## Puertos

| Aplicación | Puerto |
|---|---:|
| API operaciones | 8080 |
| API transacciones | 8081 |
| Angular | 4200 |

## Ejecución

Se recomienda iniciar los proyectos en el siguiente orden.

### 1. API transacciones

```bash
cd api-transacciones
mvn clean install
mvn spring-boot:run
```

### 2. API operaciones

```bash
cd api-operaciones
mvn clean install
mvn spring-boot:run
```

### 3. Front Angular

```bash
cd front-operaciones
npm install
npm start
```

También se puede iniciar con:

```bash
npx ng serve
```

Abrir en el navegador:

```text
http://localhost:4200
```

## Swagger / OpenAPI

Las dos APIs cuentan con documentación Swagger.

### API operaciones

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

### API transacciones

```text
http://localhost:8081/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8081/v3/api-docs
```

## H2 Console

La consola de H2 se encuentra disponible en:

```text
http://localhost:8081/h2-console
```

Datos de conexión:

```text
JDBC URL: jdbc:h2:mem:evaluaciondb
User: sa
Password:
```

## Endpoints principales

### Login

```http
POST http://localhost:8080/api/auth/login
```

Request:

```json
{
  "usuario": "admin",
  "password": "Admin123*"
}
```

Si las credenciales son correctas, el usuario puede continuar al registro de operaciones.

## Registrar operación

```http
POST http://localhost:8080/api/operaciones
```

Desde el front se captura:

- operación
- importe
- cliente
- secreto

Antes de enviar la petición, Angular cifra el valor de `secreto` utilizando AES-256-GCM.

Ejemplo del body recibido por API 1:

```json
{
  "operacion": "venta",
  "importe": "100.00",
  "cliente": "Angel",
  "secreto": "BASE64_CIFRADO_AES"
}
```

La primera API valida la información, descifra el secreto y envía los datos a la segunda API mediante OpenFeign.

La segunda API guarda la transacción, genera una referencia aleatoria de 6 dígitos y asigna el estatus inicial `Aprobada`.

Ejemplo de respuesta:

```json
{
  "id": "1",
  "estatus": "Aprobada",
  "referencia": "262737",
  "operacion": "venta"
}
```

## Validaciones

Las validaciones se realizan mediante Bean Validation y `@RestControllerAdvice`.

Algunos ejemplos:

- `operacion`: obligatoria y únicamente caracteres.
- `importe`: obligatorio y con formato de moneda.
- `cliente`: obligatorio y únicamente caracteres.
- `secreto`: obligatorio.

Ejemplo de validación para importe:

```java
@NotBlank(message = "El importe es obligatorio")
@Pattern(
    regexp = "^\\d{1,10}\\.\\d{2}$",
    message = "El importe debe tener formato de moneda, ejemplo 100.00"
)
String importe
```

Cuando existe un error de validación, el servicio regresa el detalle del campo que falló.

Ejemplo:

```json
{
  "timestamp": "2026-09-13T18:20:00Z",
  "status": 400,
  "message": "El importe debe tener formato de moneda, ejemplo 100.00",
  "errores": {
    "importe": "El importe debe tener formato de moneda, ejemplo 100.00"
  }
}
```

## Cancelar una transacción

Para cambiar una transacción de `Aprobada` a `Cancelada` se utiliza `PATCH`.

```http
PATCH http://localhost:8080/api/transacciones/estatus
```

Request:

```json
{
  "id": 1,
  "referencia": "123456",
  "estatus": "cancelar"
}
```

La actualización en base de datos se realiza mediante `@Modifying` y `@Query` en el repositorio JPA.

## Consulta paginada

```http
GET http://localhost:8080/api/transacciones?page=0&size=5&sortBy=id&direction=desc
```

Parámetros:

| Parámetro | Descripción | Ejemplo |
|---|---|---|
| `page` | Número de página, inicia en 0 | `0` |
| `size` | Cantidad de registros por página | `5` |
| `sortBy` | Campo utilizado para ordenar | `id` |
| `direction` | Dirección del ordenamiento | `asc` / `desc` |

Campos permitidos para ordenar:

- `id`
- `operacion`
- `importe`
- `cliente`
- `referencia`
- `estatus`

El front muestra la información en una tabla e incluye navegación con los botones:

```text
Anterior    Página 1 de N    Siguiente
```

Al cambiar de página se vuelve a consultar el backend utilizando el número de página correspondiente.

Ejemplo de respuesta paginada:

```json
{
  "contenido": [],
  "pagina": 0,
  "registrosPorPagina": 5,
  "totalRegistros": 0,
  "totalPaginas": 0,
  "ultimaPagina": true
}
```

## Manejo de errores

Se utiliza `@RestControllerAdvice` para centralizar los errores de validación.

Para los errores de `@Valid`, se obtiene el mensaje definido en cada validación y también se devuelve un mapa con los errores por campo.

Esto permite que Angular muestre directamente mensajes como:

```text
El importe debe tener formato de moneda, ejemplo 100.00
```

en lugar de mostrar únicamente un mensaje genérico.

## AES-256-GCM

La evaluación solicita que el campo `secreto` sea cifrado desde el front y descifrado en backend.

Para demostrar este flujo, la llave utilizada por Angular y por `api-operaciones` debe coincidir.

La configuración se encuentra en:

```text
front-operaciones/src/environments/environment.ts
```

y:

```text
api-operaciones/src/main/resources/application.properties
```

El flujo es:

```text
Texto capturado
      |
      v
Angular
AES-256-GCM
      |
      v
Texto cifrado
      |
      v
API operaciones
      |
      v
Descifrado AES
```

> Nota: para fines de la evaluación se utiliza una llave compartida entre el front y el backend. En un sistema productivo no es recomendable exponer una llave simétrica dentro de una aplicación web; normalmente se utilizarían mecanismos adicionales de seguridad y gestión de secretos.

## Persistencia

`api-transacciones` utiliza:

```java
JpaRepository
```

Esto permite realizar operaciones CRUD, ordenamiento y paginación utilizando Spring Data JPA.

La base H2 es en memoria, por lo que la información se elimina cuando se detiene la aplicación.

## Consideraciones

La solución se mantuvo sencilla para cubrir los puntos solicitados en la evaluación:

- Dos APIs independientes.
- Comunicación mediante OpenFeign.
- Validaciones de request.
- Manejo centralizado de excepciones.
- Cifrado AES-256-GCM.
- Password con BCrypt.
- Persistencia utilizando JPA.
- Base de datos H2.
- Actualización mediante PATCH y `@Query`.
- Consulta paginada.
- Front Angular 15.
- Swagger/OpenAPI para probar y documentar las APIs.
