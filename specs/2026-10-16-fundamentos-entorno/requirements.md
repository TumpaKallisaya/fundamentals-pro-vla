# Requisitos — Fase 0: Fundamentos y entorno

## Contexto

Primera fase de implementación del **Sistema IndicadoresFundamentales** (ver `../mission.md` y
`../tech-stack.md`). Corresponde a la **Fase 0** del `../roadmap.md`.

**Objetivo de la fase**: dejar la base técnica operativa sobre la que se construirán las
siguientes fases (catálogo, persistencia, conectores, motor de reglas, API y dashboard).

## Alcance

En esta fase se debe entregar:

1. **Monorepo** en el workspace actual, con `backend-funds/` y `frontend-funds/` como carpetas
   hermanas de `specs/`.
2. **Backend** `backend-funds`:
   - Spring Boot **4.1.1**, Java **21 LTS**, **Maven**.
   - Health check con Spring Boot Actuator (`/actuator/health`).
   - Endpoint simple de estado `GET /api/health` (controlador propio) consumible por el frontend.
   - CORS habilitado para el origen del frontend.
   - Dependencias base: Web, Data JPA, Validation, Actuator, PostgreSQL, Flyway.
   - Configuración de zona horaria **America/New_York**.
   - **Arquitectura hexagonal** (Puertos y Adaptadores): paquetes `domain`, `application` e
     `infrastructure`, con puertos de entrada/salida (`domain.port.in` / `domain.port.out`) y
     adaptadores (`infrastructure.adapter`).
   - **Prueba unitaria** de cada controlador con **Mockito**.
3. **Frontend** `frontend-funds`:
   - Angular **22.2.x**, standalone components + Signals.
   - Página de inicio mínima **conectada al backend** (consume `GET /api/health` y muestra el
     estado de conexión).
   - **Arquitectura Feature-Based**: carpetas `core/`, `shared/` y `features/` bajo `src/app`,
     con la funcionalidad de salud en `features/health/`.
4. **Base de datos** PostgreSQL **18.6** levantada con **Docker Compose** (solo la BD).
   - Backend conectado a la BD.
   - **Flyway** habilitado con una migración inicial para validar el pipeline.
5. **Git** inicializado en la raíz del workspace (sin CI en esta fase), con remoto `origin`
   apuntando al repositorio externo.

## Decisiones confirmadas

| Decisión | Valor |
|----------|-------|
| Estructura del repo | Monorepo en el workspace actual (`backend-funds/` y `frontend-funds/` junto a `specs/`) |
| Repositorio remoto | `https://github.com/TumpaKallisaya/fundamentals-pro-vla.git` |
| Build backend | Maven |
| Java | 21 LTS |
| Migraciones | Flyway |
| Ejecución del entorno | Solo PostgreSQL en Docker; backend/frontend en modo dev local |
| Control de versiones | Solo Git, sin CI por ahora |
| Arquitectura backend | Hexagonal (Puertos y Adaptadores) |
| Arquitectura frontend | Feature-Based (orientada a funcionalidades) |

## Convenciones

- **GroupId / paquete base backend**: `com.funds` / `com.funds.backend` (ajustable).
- **Artefacto backend**: `backend-funds`.
- **Frontend**: proyecto Angular `frontend-funds`, estilos SCSS, SPA sin SSR en esta fase.
- **Zona horaria**: America/New_York (ET) para toda fecha/hora mostrada o calculada.
- **Ramas**: `main` como rama principal.
- **Repositorio remoto (origin)**: `https://github.com/TumpaKallisaya/fundamentals-pro-vla.git`.
- **Paquetes backend (hexagonal)**: `domain` (con `model`, `port.in`, `port.out`, `service`),
  `application.service` e `infrastructure` (con `adapter.in.web`, `adapter.out`, `config`).
- **Carpetas frontend (feature-based)**: `core/`, `shared/` y `features/<nombre>/` bajo `src/app`.

## Fuera de alcance (fases posteriores)

- Catálogo de indicadores y fuentes (Fase 1).
- Modelo de datos de dominio y entidades (Fase 2).
- Conectores de fuentes externas (Fase 3).
- Motor de reglas de interpretación (Fase 4).
- API REST de negocio (Fase 5).
- Dashboard con secciones (Fase 6).
- CI/CD y contenerización completa del stack (Fase 7).

## Prerequisitos del entorno local

- JDK 21 LTS.
- Maven 3.9+.
- Node.js LTS (20/22) y npm.
- Angular CLI 22 (`@angular/cli`).
- Docker + Docker Compose.
- Git.
