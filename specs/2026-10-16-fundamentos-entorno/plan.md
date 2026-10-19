# Plan — Fase 0: Fundamentos y entorno

Serie de grupos de tareas numerados. Cada grupo es un incremento verificable.

## Grupo 1 — Repositorio y estructura del monorepo

1. Inicializar Git en la raíz del workspace (`fundamentals-pro-vla`).
2. Configurar el remoto `origin` al repositorio externo
   `https://github.com/TumpaKallisaya/fundamentals-pro-vla.git`.
3. Crear `.gitignore` raíz (Java/Maven, Node/Angular, Docker, IDE, `target/`, `node_modules/`, `dist/`).
4. Crear `compose.yaml` para PostgreSQL 18.6.
5. Crear `README.md` de arranque con instrucciones de ejecución local.

## Grupo 2 — Backend (`backend-funds`)

1. Generar proyecto Spring Boot 4.1.1 (Maven, Java 21) con dependencias: Web, Data JPA,
   Validation, Actuator, PostgreSQL, Flyway.
2. Configurar `application.yml`: datasource PostgreSQL, Flyway, zona horaria America/New_York,
   Actuator.
3. Añadir migración inicial Flyway (`V1__init.sql`).
4. Exponer endpoint simple de estado `GET /api/health` (controlador propio) y habilitar CORS
   para el origen del frontend (`http://localhost:4200`).
5. Verificar health check `/actuator/health` y `/api/health` en `UP`.

## Grupo 3 — Frontend (`frontend-funds`)

1. Generar proyecto Angular 22.2.x (standalone, Signals, SCSS, sin SSR).
2. App base que compila y sirve (componente raíz con título del sistema).
3. Verificar compilación (`ng build`) y servidor de desarrollo (`ng serve`).

## Grupo 4 — Base de datos (Docker)

1. Definir servicio `postgres` (18.6) en Compose con credenciales, puerto y volumen.
2. Levantar `docker compose up -d` y verificar conexión.

## Grupo 5 — Página de inicio conectada al backend

1. Implementar en el frontend una página de inicio que consuma `GET /api/health` y muestre el
   estado de conexión (conectado/error) y el nombre del servicio.
2. Configurar la URL del backend en el entorno del frontend (p. ej. `environment.ts`).
3. Verificar que, con backend y frontend arrancados, la página muestra "conectado".

## Grupo 6 — Integración y verificación end-to-end

1. Arrancar backend y confirmar conexión a PostgreSQL + migración Flyway aplicada.
2. Arrancar frontend y confirmar que sirve la página de inicio conectada al backend.
3. Documentar cómo arrancar el entorno completo en el `README.md`.
4. Commit inicial en `main` y push al remoto `origin`.

## Grupo 7 — Arquitectura hexagonal (backend)

1. Reorganizar los paquetes del backend en `domain`, `application` e `infrastructure`
   (hexagonal: puertos y adaptadores).
2. Reubicar el health check: `HealthStatus` (dominio), `HealthUseCase` (puerto de entrada),
   `HealthService` (aplicación) y `HealthController` (adaptador de entrada REST).
3. Documentar los paquetes vacíos (puertos de salida, servicios de dominio, adaptadores de
   salida) con `package-info.java` como convención para fases posteriores.
4. Prueba unitaria de cada controlador con **Mockito** (mock del puerto de entrada).

## Grupo 8 — Arquitectura Feature-Based (frontend)

1. Crear las carpetas `core/`, `shared/` y `features/` bajo `src/app`.
2. Reubicar el servicio de salud en `features/health/` (servicio + modelo).
3. Documentar la convención en `src/app/README.md` y en los README de cada carpeta.
4. Verificar compilación (`ng build`) y pruebas (`ng test`).

## Grupo 9 — Estándar documentado y sincronizado

1. Declarar en `tech-stack.md` ambas arquitecturas como **obligatorias** para cualquier agente.
2. Sincronizar `requirements.md`, `validation.md`, `roadmap.md` y `README.md` con estos requisitos.
3. Commit y push al remoto `origin`.
