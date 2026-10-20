# Requisitos — Fase 9: Reingeniería del dashboard y fuente de datos (según mockup)

## Contexto

Fase de reingeniería solicitada por el trader. Redefine la vista del dashboard según
`specs/MockupProyectado.xlsx` y cambia la fuente de datos para mostrar el **"actual"** de forma
inmediata y el **histórico de 20 días**, usando **Forex Factory** como fuente principal.
Corresponde a la **Fase 9** del `../roadmap.md`.

## Alcance

1. **Frecuencia estructurada** por indicador + calculador de **"siguiente fecha"**.
2. **Conector Forex Factory** (API + scraping web) con `actual` e histórico.
3. **Histórico de 20 días** en el primer refresco.
4. **API** de consulta para el dashboard (agrupación, "siguiente fecha", valores por día).
5. **Dashboard** rediseñado según el mockup.

## Decisiones confirmadas

| Decisión | Valor |
|---|---|
| Fuente del "actual" | **Forex Factory** (API JSON + **scraping de su web**, priorizando inmediatez) |
| Histórico de 20 días | **Scraping histórico de Forex Factory** |
| "Siguiente fecha" | **Calculador desde el catálogo** (frecuencia estructurada) |
| Agrupación | **Alto impacto / Medio impacto** |
| Ventana de visualización | **Últimos 20 días calendario** |
| Formato de fechas | **dd/mm/YYYY** |
| Contenido de celda (Sección 1) | **Valor del resultado** (vacía si no hay dato) |

## Vista final (según mockup)

### Sección 1 — Resultados por indicador

- Filas: todos los indicadores, **agrupados** en "Alto impacto" y "Medio impacto".
- Columnas: **"Siguiente fecha"** + **20 días calendario** (dd/mm/YYYY).
- Celda: **valor del resultado** de ese día; **vacía** si no hay información.

### Sección 2 — Tendencia por índice

- Filas: **NAS100** y **SP500**; columnas: los mismos 20 días.
- Celda: **Alcista / Neutral / Bajista**.

## Fuente de datos (Forex Factory)

- **API JSON** (`ff_calendar_thisweek.json`): calendario de la semana actual (forecast/previous).
- **Scraping de la web de FF** (Jsoup): valor **"actual"** (inmediato) y **histórico de semanas
  pasadas** para cubrir 20 días.
- ⚠️ **Riesgo/ToS**: el scraping de la web de Forex Factory va contra sus términos de uso (marcado
  🚫 en `fuentes_trading_scraping_api.md`). Se acepta por decisión del trader para priorizar la
  inmediatez. El endpoint/estructura HTML exactos se determinarán durante la implementación.

## Frecuencia estructurada y "siguiente fecha"

- `frecuencia` pasa de texto libre a estructura: `tipo` (SEMANAL / MENSUAL / TRIMESTRAL / IRREGULAR)
  y regla de día (día de la semana, día del mes, "primer viernes", etc.).
- `CalculadorProximaFecha` devuelve la próxima fecha de publicación de cada indicador.

## Histórico (primer arranque)

- El primer refresco consulta los **20 días anteriores** y persiste los eventos y resultados
  (`actual`) en las fechas correspondientes.
- Refrescos posteriores actualizan el día actual y los faltantes.

## Convenciones

- El conector de scraping es un **adaptador de salida** (`infrastructure.adapter.out.web`); el
  dominio no conoce HTTP ni Jsoup.
- El calculador de "siguiente fecha" es un **servicio de dominio puro**.
- Fechas internas en UTC; presentación en America/New_York con formato dd/mm/YYYY.

## Fuera de alcance

- Ejecución de órdenes / trading automatizado.
- Sectores forex/cripto (Fase 8).
- Scheduler automático (solo refresco manual por botón).
