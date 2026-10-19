# Sistema IndicadoresFundamentales

## Contexto de los Stakeholders

- Soy un trader scalper que necesita tener un contexto de las noticias fundamentales que afectan los mercados financieros.
- Soy un trader scalper que revisa 4 hr, 1 hr y 15 min para ejecutar en 1 minuto.
- Ya tengo una estrategia para ejecución, sin embargo requiero contar de forma inmediata de los resultados que se presenta en relación a noticias fundamentales.
- Requiero que se revisen apis y paginas web al momento de presionar un boton y que se acutalice toda la información y la interpretación (bajista o alcista) proyectado para el mercado.
- Necesito que cada vez que se presione dicho boton se actualice la información de los resultados faltantes y se actualice la tendencia proyectada para el día en función a la interpretación integra.

## Estructura del proyecto

```
fundamentals-pro-vla/
├── specs/            # Constitución y especificaciones (mission, tech-stack, roadmap, fases)
├── backend-funds/    # Backend Spring Boot 4.1.1 (Java 21, Maven) — arquitectura hexagonal
├── frontend-funds/   # Frontend Angular 22.2.x (TypeScript) — arquitectura feature-based
└── compose.yaml      # PostgreSQL 18.6 (Docker Compose)
```

> **Arquitecturas obligatorias**: backend **hexagonal** (Puertos y Adaptadores:
> `domain` / `application` / `infrastructure`) y frontend **Feature-Based**
> (`core/` / `shared/` / `features/`). Ver `specs/tech-stack.md`.

## Requisitos previos

- JDK 21+ (compatible con Java 27)
- Maven 3.9+ (o usar el wrapper `mvnw` incluido)
- Node.js 22.22+ / 24+ y npm
- Angular CLI 22 (`@angular/cli@22`)
- Docker Desktop (para PostgreSQL)

## Cómo ejecutar

### 1. Base de datos (PostgreSQL 18.6)

```bash
docker compose up -d
```

Crea la base `funds` con usuario `funds` y contraseña `funds` en `localhost:5432`.

### 2. Backend (`backend-funds`)

```bash
cd backend-funds
./mvnw spring-boot:run     # Windows: mvnw.cmd spring-boot:run
```

- Health check (Actuator): http://localhost:8080/actuator/health
- Endpoint de estado: http://localhost:8080/api/health
- Flyway aplica la migración inicial (`V1__init.sql`) al arrancar.

### 3. Frontend (`frontend-funds`)

```bash
cd frontend-funds
npm install
npm start                  # ng serve
```

Abrir http://localhost:4200 → la página de inicio muestra "Conectado al backend".

> La URL del backend está en `frontend-funds/src/environments/environment.ts`.

## Estado

- Fase 0 (fundamentos y entorno) implementada: backend y frontend compilan, arquitecturas
  hexagonal (backend) y feature-based (frontend) establecidas, PostgreSQL vía Docker Compose,
  página de inicio conectada al backend y pruebas unitarias (Mockito en backend).
