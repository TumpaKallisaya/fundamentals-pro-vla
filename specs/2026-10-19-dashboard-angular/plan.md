# Plan — Fase 6: Frontend (dashboard Angular)

Serie de grupos de tareas numerados. Sigue la arquitectura Feature-Based obligatoria
(`../tech-stack.md`).

## Grupo 1 — Dependencia y tema de Angular Material

1. Añadir `@angular/material` (versión compatible con Angular 22) y configurar el tema en
   `styles.scss`.

## Grupo 2 — Shell y routing (Feature-Based)

1. Convertir `app.ts` en un shell con `<router-outlet>` (layout global).
2. Configurar `app.routes.ts` con la ruta `/` → `features/dashboard`.

## Grupo 3 — Feature `dashboard` (servicio y modelos)

1. Crear `features/dashboard/dashboard.service.ts` con las llamadas al backend:
   `POST /api/refresco`, `GET /api/eventos`, `GET /api/tendencias`, `GET /api/refresco/ultimo`.
2. Crear `features/dashboard/dashboard.model.ts` con los tipos (`EventoDto`, `TendenciaDto`,
   `RefrescoDto`, `EstadoEvento`, `ClasificacionTendencia`).

## Grupo 4 — Sección 1: semáforo mensual por indicador

1. Tabla Material donde las filas son los indicadores y las columnas las fechas del rango.
2. Colorear cada celda según el estado: verde/amarillo/rojo; gris para pendiente/futuro.
3. Filtro por índice (NAS100/SP500).

## Grupo 5 — Sección 2: tendencia diaria por índice

1. Tabla Material con los índices (NAS100/SP500) como filas y las fechas como columnas.
2. Cada celda muestra alcista/neutral/bajista con su color.

## Grupo 6 — Botón "Actualizar" e indicadores

1. Botón que dispara `POST /api/refresco` y luego re-consulta ambas secciones y el último refresco.
2. Indicadores de carga (spinner), error y último refresco (estado + hora).

## Grupo 7 — Filtros de rango de fechas

1. Selector de `desde`/`hasta` (por defecto, mes en curso en ET) con recarga al cambiar.

## Grupo 8 — Pruebas y validación

1. Pruebas del componente y servicio (Vitest).
2. Verificar `ng build` y `ng test`.

## Grupo 9 — Documentación

1. Actualizar `requirements.md` y `validation.md`.
2. Verificar coherencia con `mission.md` y `tech-stack.md`.
