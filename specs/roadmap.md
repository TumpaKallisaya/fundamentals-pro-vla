# Roadmap — Sistema IndicadoresFundamentales

Orden de implementación de alto nivel, dividido en fases pequeñas de trabajo. Cada fase es un
incremento verificable. Las fases 0–7 constituyen el MVP (NAS100 y SP500); la fase 8 es de
expansión.

## Fase 0 — Fundamentos y entorno

- Crear repositorio/estructura del monorepo con `backend-funds` y `frontend-funds`.
- Generar proyecto base Spring Boot 4.1.1 (Java 21, Maven) con health check.
- Generar proyecto base Angular 22.2.x (standalone components + Signals).
- Levantar PostgreSQL 18.6 (local vía Docker) y conectar el backend (Flyway/Liquibase).
- Configurar zona horaria America/New_York y convenciones de código/CI básica.
- Establecer la arquitectura **hexagonal** (Puertos y Adaptadores) en el backend.
- Establecer la arquitectura **Feature-Based** (orientada a funcionalidades) en el frontend.
- Pruebas unitarias de controladores del backend con **Mockito**.

**Resultado**: ambos proyectos corren, el backend conecta a PostgreSQL, y las arquitecturas
obligatorias (y sus pruebas) quedan establecidas como base de las siguientes fases.

## Fase 1 — Catálogo de indicadores y fuentes

- Elaborar el listado de indicadores de alto y medio impacto de EE.UU. para NAS100 (Nasdaq 100)
  y SP500 (S&P 500).
- Definir, por indicador: sector, impacto, hora (ET), pronóstico/anterior, **fuente** (API o
  página web) y **regla de interpretación** (dirección favorable, peso y tolerancia ±X%).
- Documentar fuentes (endpoints/URLs, formato, autenticación, límites).

**Resultado**: catálogo completo en documento/configuración, listo para modelar.

## Fase 2 — Modelo de datos y persistencia (backend)

- Entidades: `Indicador`, `Evento` (calendario), `Resultado` (observado), `TendenciaDiaria`,
  `Refresco` (histórico/trazabilidad).
- Migraciones y repositorios Spring Data JPA.
- Seed del catálogo definido en Fase 1.

**Resultado**: esquema de BD y repositorios funcionales.

## Fase 3 — Conectores de fuentes externas (backend)

- Abstracción de conector (interfaz) + adaptadores por fuente.
- Implementar conectores de APIs y, donde no exista API, scrapers de páginas web.
- Normalizar los datos a las entidades del modelo (hora en ET, actual/pronóstico/anterior).
- Manejo de errores, reintentos y timeouts.

**Resultado**: el backend obtiene datos reales de las fuentes configuradas.

## Fase 4 — Motor de reglas de interpretación (backend)

- Clasificación por evento en **tres estados**:
  - **Verde — mejor de lo esperado**: real favorable frente al pronóstico.
  - **Amarillo — dentro de lo esperado**: real dentro de ±X% del pronóstico (X por indicador).
  - **Rojo — peor de lo esperado**: real desfavorable frente al pronóstico.
- Dirección de impacto por indicador (si un valor mayor es favorable o desfavorable para el índice).
- Lectura por evento con justificación explícita (real, pronóstico, desviación, estado).
- Agregador de tendencia diaria por índice: **puntaje ponderado por impacto**
  (+1 alcista / 0 neutral / −1 bajista), clasificado por umbrales → alcista / neutral / bajista.
- Índices cubiertos: **NAS100** y **SP500**.

**Resultado**: dado un conjunto de resultados, se calcula el estado por evento y la tendencia
del día por índice.

## Fase 5 — API REST (backend)

- Endpoint de **refresco** (dispara consulta a fuentes, guarda resultados y recalcula).
- Endpoint de consulta de eventos/resultados y de tendencia diaria por índice, con soporte de
  **rango de fechas (desde/hasta)** para histórico.
- Endpoint de estado de último refresco.

**Resultado**: el backend expone la funcionalidad completa por HTTP.

## Fase 6 — Frontend (dashboard Angular)

Dashboard con dos secciones, ambas en **America/New_York (ET)** y con **filtro de rango de
fechas (desde / hasta)** para consultar histórico (por defecto, el mes en curso).

### Sección 1 — Resultados por indicador (semáforo mensual)
- Tabla donde las **filas** son los indicadores y las **columnas** son las fechas del mes.
- Cada celda coloreada según el estado del resultado:
  - **Rojo**: salió fuera (peor) de lo esperado.
  - **Amarillo**: dentro de lo esperado.
  - **Verde**: mejor de lo esperado.
  - **Vacío/gris**: fecha futura o indicador aún no publicado (pendiente).
- Filtrable por índice (NAS100 / SP500).

### Sección 2 — Resultado general proyectado del día por índice
- Tabla con los índices (**NAS100**, **SP500**) como filas y las **fechas del mes** como
  columnas; cada celda muestra la **tendencia proyectada del día** (alcista / neutral /
  bajista) con su color asociado.
- Refleja el puntaje ponderado por impacto calculado por el backend.

### Comunes
- Botón **"Actualizar"** que dispara el refresco contra el backend y actualiza ambas secciones.
- Indicadores de carga, error y último refresco.

**Resultado**: el usuario actualiza y visualiza ambas secciones con un clic (MVP completo).

## Fase 7 — Pruebas, hardening y despliegue

- Pruebas unitarias e integración (backend y frontend).
- Validación de zona horaria, concurrencia del refresco y tolerancia a fallos de fuentes.
- Empaquetado y despliegue (Docker Compose: backend + frontend + PostgreSQL).

**Resultado**: MVP estable y desplegable.

## Fase 8 — Expansión a nuevos sectores (futuro)

- Agregar sectores **forex (monedas)** y **criptomonedas** vía catálogo de indicadores.
- Ajustar/ampliar reglas de interpretación por sector si es necesario.
- Evaluar nuevas fuentes específicas por sector.

**Resultado**: la plataforma cubre múltiples mercados sin rediseñar el core.

## Fase 9 — Reingeniería: dashboard según mockup y fuente Forex Factory (actual + histórico)

- Redefinir la vista del dashboard según `MockupProyectado.xlsx`:
  - Sección 1: indicadores agrupados por **impacto (alto/medio)**, columna **"siguiente fecha"**
    y **20 días calendario**; cada celda muestra el **valor del resultado** (vacía si no hay dato).
  - Sección 2: tendencia por índice (NAS100/SP500) en los mismos 20 días.
  - Fechas en formato **dd/mm/YYYY**.
- Fuente principal **Forex Factory** (API + scraping de su web) para obtener el **"actual"** de
  forma inmediata y el **histórico de 20 días**.
- **"Siguiente fecha"** de publicación por indicador, calculada desde el catálogo.

**Resultado**: el dashboard muestra el histórico real de 20 días con los valores por indicador y
la próxima fecha de publicación, actualizable con un clic.
