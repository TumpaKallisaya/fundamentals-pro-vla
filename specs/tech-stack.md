# Stack Tecnológico — Sistema IndicadoresFundamentales

## Resumen

| Capa | Tecnología | Versión | Proyecto |
|------|-----------|---------|----------|
| Backend | Spring Boot | 4.1.1 (última estable) | `backend-funds` |
| Frontend | Angular | 22.2.x (última estable) | `frontend-funds` |
| Base de datos | PostgreSQL | 18.6 (última estable) | — |

## Backend — `backend-funds` (Spring Boot)

- **Framework**: Spring Boot **4.1.1** (última versión estable, línea 4.x).
- **Lenguaje**: Java **21 LTS** (Spring Boot 4.x requiere Java 17+; se recomienda 21 LTS).
- **Build tool**: Maven (por defecto; Gradle como alternativa).
- **API**: REST (JSON) con Spring Web (spring-boot-starter-web).
- **Persistencia**: Spring Data JPA (Hibernate) sobre PostgreSQL.
- **Integración de fuentes externas**:
  - Cliente HTTP (Spring WebClient / RestClient) para APIs.
  - Módulo de scraping para páginas web (p. ej. HTML parsing) cuando no exista API.
  - Cada fuente se implementa como un **conector** intercambiable (interfaz + adaptador).
- **Motor de reglas de interpretación**:
  - Clasificación por evento en **tres estados**: **verde (mejor de lo esperado)**,
    **amarillo (dentro de lo esperado, ±X% del pronóstico)** y **rojo (peor de lo esperado)**;
    la tolerancia X es configurable por indicador.
  - Dirección de impacto por indicador (si un valor mayor es favorable o desfavorable).
  - Agregador de tendencia diaria por índice: **puntaje ponderado por impacto**
    (+1 alcista / 0 neutral / −1 bajista) clasificado por umbrales.
  - Reglas y tolerancias configurables (cargadas desde el catálogo, no hardcodeadas).
- **Zona horaria**: America/New_York (ET) para el cálculo y presentación del "día".
- **Configuración externa**: `application.yml` (credenciales, URLs, endpoints, timezone).

### Dependencias clave (previstas)

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `org.postgresql:postgresql`
- Librería de parsing HTML (p. ej. Jsoup) para scraping
- `spring-boot-starter-test` (pruebas)

## Frontend — `frontend-funds` (Angular)

- **Framework**: Angular **22.2.x** (última versión estable, línea 22).
- **Lenguaje**: TypeScript.
- **Node.js**: LTS vigente (20/22).
- **Patrón**: Standalone components + Signals (reactividad estable en v22).
- **Estado**: Signals / servicios con RxJS donde aplique.
- **UI**: Angular Material o estilos propios para el dashboard.
- **Funcionalidades clave**:
  - Botón **"Actualizar"** que dispara el refresco contra el backend.
  - **Sección 1**: tabla mensual de indicadores (filas) × fechas (columnas) con semáforo por
    celda (verde/amarillo/rojo/gris) en ET, filtrable por índice.
  - **Sección 2**: tabla mensual de la tendencia proyectada del día por índice
    (NAS100 / SP500) con color alcista/neutral/bajista.
  - Filtros de rango de fechas (desde/hasta) para consultar histórico en ambas secciones.
  - Indicador de estado de carga y de último refresco.

### Dependencias clave (previstas)

- `@angular/core`, `@angular/common`, `@angular/router`
- `@angular/forms` (o Signal Forms, estables en v22)
- `@angular/material` (opcional)
- `@angular-devkit/build-angular`

## Base de datos — PostgreSQL

- **Motor**: PostgreSQL **18.6** (última versión estable GA; la 19 aún está en RC).
- **Uso**:
  - Catálogo de **indicadores** (nombre, sector, país, impacto, fuente, regla).
  - **Eventos** del calendario (indicador, hora en ET, pronóstico, anterior).
  - **Resultados** observados (actual) por evento y por refresco.
  - **Tendencias diarias** calculadas por índice.
  - Histórico de refrescos para trazabilidad.
- **Acceso**: JDBC desde Spring Data JPA; migraciones con Flyway/Liquibase (a definir).

## Comunicación

- Frontend → Backend: **REST/JSON**.
- Backend → Fuentes externas: **HTTPS** (APIs o scraping de páginas web).
- CORS configurado para el origen del frontend.

## Fuentes de datos (catálogo)

- Se elaborará un **listado de todos los indicadores** a revisar y **sus fuentes**
  (APIs y/o páginas web) como entregable previo a la implementación de conectores.
- Criterio: priorizar fuentes con API estable; usar scraping como respaldo.
- Este catálogo es la base del motor de reglas y del calendario.

## Decisiones y supuestos

- Interpretación: **reglas por evento (actual vs. pronóstico)**; sin IA/LLM en la v1.
- Actualización: **solo botón manual** (sin scheduler automático en la v1).
- Zona horaria: **America/New_York (ET)**.
- Eventos iniciales: **alto y medio impacto** de EE.UU. para NAS100 (Nasdaq 100) y
  SP500 (S&P 500).
- Extensibilidad: nuevos sectores (forex, cripto) se añaden vía catálogo de indicadores.
