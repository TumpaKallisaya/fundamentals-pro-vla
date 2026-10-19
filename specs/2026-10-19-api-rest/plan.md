# Plan — Fase 5: API REST (backend)

Serie de grupos de tareas numerados. Sigue la arquitectura hexagonal obligatoria (`../tech-stack.md`).

## Grupo 1 — Persistir el estado por evento (migración V4)

1. Crear `V4__resultado_estado.sql`: añadir columnas `estado` y `desviacion` a `resultado`.
2. Actualizar la entidad de dominio `Resultado`, la entidad JPA y el mapper con los nuevos campos.

## Grupo 2 — Calcular y persistir estado/desviación

1. Actualizar `CalcularTendenciasService` para calcular `estado` (VERDE/AMARILLO/ROJO/PENDIENTE) y
   `desviacion` (actual − forecast) y persistirlos en `Resultado`.

## Grupo 3 — Caso de uso de refresco (orquestación)

1. Crear `RefrescoService` (capa de aplicación) que orquesta el flujo completo:
   sincronizar calendario (Forex Factory) → resolver "actual" (FRED) → clasificar → calcular
   tendencias, con un único `Refresco` de trazabilidad.

## Grupo 4 — Controladores REST (adaptadores de entrada)

1. `POST /api/refresco` → dispara el flujo completo; devuelve el estado del refresco.
2. `GET /api/eventos` → eventos con resultado y clasificación, filtro `desde`/`hasta` (ET) e `indice`.
3. `GET /api/tendencias` → tendencia diaria por índice, filtro `desde`/`hasta` e `indice`.
4. `GET /api/refresco/ultimo` → estado del último refresco.

## Grupo 5 — DTOs de respuesta

1. Definir los DTOs de respuesta (`EventoDto`, `TendenciaDto`, `RefrescoDto`) en el adaptador de
   entrada y mapear desde el dominio.

## Grupo 6 — Pruebas

1. Pruebas unitarias de los controladores con **Mockito** (mock de los casos de uso/puertos).
2. Prueba unitaria de `RefrescoService` con Mockito.

## Grupo 7 — Documentación y validación

1. Actualizar `requirements.md` y `validation.md`.
2. Verificar coherencia con `mission.md` y `tech-stack.md`.
