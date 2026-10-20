# Plan — Fase 9: Reingeniería del dashboard y fuente de datos (según mockup)

Serie de grupos de tareas numerados. Sigue la arquitectura hexagonal (backend) y Feature-Based
(frontend) obligatorias.

## Parte A — Fuente de datos: Forex Factory con "actual" e histórico

## Grupo 1 — Frecuencia estructurada y calculador de "siguiente fecha"

1. Modelar la frecuencia de cada indicador de forma **estructurada** (tipo + regla de día) en el
   catálogo, reemplazando el texto libre.
2. Crear el servicio de dominio `CalculadorProximaFecha` que devuelva la próxima fecha de
   publicación de cada indicador (semanal/mensual/trimestral/irregular).

## Grupo 2 — Conector de scraping de Forex Factory

1. Crear el puerto de salida `CalendarioResultadosPort` (`obtenerEventos(desde, hasta)`), que
   devuelve eventos con `actual`.
2. Crear `ForexFactoryScraperAdapter` (Jsoup + RestClient) que obtiene el calendario de FF
   (API JSON para la semana actual + scraping de su web para el "actual" inmediato y el histórico).
3. Normalizar: título → alias, fecha → UTC, impacto, forecast, previous y **actual**.

## Grupo 3 — Histórico de 20 días (primer arranque)

1. En el primer refresco, consultar los **20 días anteriores** por indicador y persistir los
   eventos y resultados (`actual`) en las fechas que correspondan.
2. Persistir el `Resultado` con su `actual` (y `estado`/`desviacion`).

## Parte B — API de consulta

## Grupo 4 — Endpoint del dashboard

1. Adaptar/crear el endpoint de consulta que devuelva, por indicador: su grupo de impacto,
   **"siguiente fecha"** y el **valor del resultado** por día (últimos 20 días).
2. Mantener el endpoint de tendencia diaria por índice (últimos 20 días).

## Parte C — Dashboard (frontend)

## Grupo 5 — Rediseño de la vista según el mockup

1. Sección 1: indicadores **agrupados por impacto (alto/medio)**, columna "siguiente fecha" y
   20 días calendario; celda = valor del resultado (vacía si no hay dato).
2. Sección 2: NAS100/SP500 × 20 días con la tendencia.
3. Fechas en formato **dd/mm/YYYY**.

## Parte D — Pruebas y documentación

## Grupo 6 — Pruebas

1. Pruebas del calculador de "siguiente fecha" (semanal/mensual/trimestral).
2. Pruebas del adaptador de scraping (parseo del HTML con fixture).
3. Pruebas de los endpoints y del componente dashboard.

## Grupo 7 — Documentación

1. Actualizar `requirements.md` y `validation.md`.
2. Verificar coherencia con `mission.md` y `tech-stack.md` (scraping, ToS).
