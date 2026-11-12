# Sistema Académico UCO – Backend

API REST con **Spring Boot 3 + Java 17 + MySQL + Swagger (springdoc-openapi)** para gestionar estudiantes y profesores.
Cada rol tiene su propio panel:

- **Panel del profesor:** registra notas, asistencia / llegadas tarde y puntos positivos y negativos de sus estudiantes.
- **Panel del estudiante:** consulta sus notas, promedio por materia, tardanzas, ausencias y balance de puntos.

## Integrantes

| Nombre | Rol / Aporte |
|--------|--------------|
| [NOMBRE 1] | [p. ej. Modelo y repositorios] |
| [NOMBRE 2] | [p. ej. Servicios y reglas de negocio] |
| [NOMBRE 3] | [p. ej. Controladores y Swagger] |

## Requisitos

- JDK 17+
- Maven 3.9+ (o el IDE: IntelliJ / Eclipse / VS Code)
- MySQL 8+ corriendo en `localhost:3306`

## Ejecutar

```bash
# Opcional: credenciales propias (por defecto root / sin contraseña)
export DB_USER=root
export DB_PASSWORD=tu_clave

mvn spring-boot:run
```

La base `uco_academico` se crea sola y las tablas se generan con JPA (`ddl-auto=update`).
Swagger UI: **http://localhost:8080/swagger-ui.html** · OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Modelo de datos

`Estudiante`, `Profesor`, `Materia` (un profesor dicta varias materias), `matriculas` (estudiante ↔ materia),
`Nota`, `Asistencia` (PRESENTE / TARDE / AUSENTE / EXCUSADO, con `minutosTarde`), `PuntoConducta` (POSITIVO / NEGATIVO).

## Flujo de uso sugerido en Swagger

1. `POST /api/profesores` → crear profesor
2. `POST /api/estudiantes` → crear estudiante
3. `POST /api/materias` → crear materia con `profesorId`
4. `POST /api/materias/{materiaId}/estudiantes/{estudianteId}` → matricular
5. Panel profesor: `POST /api/panel-profesor/{profesorId}/notas | asistencias | puntos`
6. Panel estudiante: `GET /api/panel-estudiante/{estudianteId}`

Ejemplos de cuerpos:

```json
// POST /api/panel-profesor/1/notas
{ "materiaId": 1, "estudianteId": 1, "descripcion": "Parcial 1", "valor": 4.2, "porcentaje": 30 }

// POST /api/panel-profesor/1/asistencias  (llegada tarde)
{ "materiaId": 1, "estudianteId": 1, "fecha": "2026-11-11", "estado": "TARDE", "minutosTarde": 15, "observacion": "Llegó después del descanso" }

// POST /api/panel-profesor/1/puntos
{ "materiaId": 1, "estudianteId": 1, "tipo": "POSITIVO", "puntos": 2, "motivo": "Excelente participación" }
```

## Reglas de negocio

- Un profesor solo puede registrar sobre **sus** materias (si no → `403`).
- Solo se puede registrar a estudiantes **matriculados** en la materia (si no → `400`).
- Nota entre 0.0 y 5.0. Si el estado es `TARDE`, `minutosTarde` es obligatorio y > 0.
- Errores uniformes en JSON (`404`, `400`, `403`, `409`).

## Estructura

```
src/main/java/co/edu/uco/academico
├── config/       OpenApiConfig (Swagger)
├── controller/   Endpoints REST
├── dto/          Requests (validaciones) y Responses
├── exception/    Excepciones y manejador global
├── model/        Entidades JPA
├── repository/   Spring Data JPA
└── service/      Lógica de negocio
```

## Flujo de trabajo en Git

- Ramas: `main` (estable) ← `develop` ← `feature/<tema>`. Nadie hace push directo a `main`.
- Cada integrante trabaja en su rama `feature/...` y abre un **Pull Request** hacia `develop`; otro compañero lo revisa.
- Commits pequeños y con sentido, en formato [Conventional Commits](https://www.conventionalcommits.org/es/):
  `feat:`, `fix:`, `docs:`, `refactor:`, `chore:`, `test:`.
  Ejemplo: `feat(panel-profesor): registrar llegadas tarde con minutosTarde`.
- Nunca subir contraseñas (usar variables de entorno `DB_USER` / `DB_PASSWORD`).
