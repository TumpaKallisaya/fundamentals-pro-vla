# Requisitos — Fase 4: Motor de reglas de interpretación (y fuente del "actual")

## Contexto

Quinta fase de implementación del **Sistema IndicadoresFundamentales** (ver `../mission.md` y
`../tech-stack.md`). Corresponde a la **Fase 4** del `../roadmap.md`, ampliada por decisión del
trader para **resolver primero la fuente del valor "actual"** (necesaria para clasificar
"actual vs pronóstico").

**Objetivo**: capturar el valor "actual" de cada indicador (FRED/BLS) y calcular, de forma
determinista, la clasificación por evento (verde/amarillo/rojo) y la tendencia diaria por índice
(alcista/neutral/bajista).

## Alcance

En esta fase se entrega:

1. **Fuente del "actual"**: puerto `ValorActualPort` + adaptador FRED/BLS + catálogo de series.
2. **Clasificación por evento**: 3 estados (verde/amarillo/rojo) + pendiente, con justificación.
3. **Direcciones del catálogo** refinadas según el PDF (Solid Trading Latam) y fuentes web.
4. **Agregador de tendencia diaria** por índice (puntaje ponderado).
5. **Pruebas** unitarias del motor y del adaptador.

## Decisiones confirmadas

| Decisión | Valor |
|---|---|
| Fuente del "actual" | **FRED/BLS** (oficial, API key gratuita) |
| Objetivo de interpretación | **Para el índice** (NAS100/SP500), no el USD |
| Direcciones por indicador | **Propuestas por defecto** (PDF + web, traducidas al índice); revisión del trader |
| Empleo/crecimiento | **Dirección fija determinista** (sin depender del régimen de tasas) |
| Clasificación | Verde/amarillo/rojo + **pendiente** cuando `actual` es null |
| Agregación | Puntaje **ponderado por impacto (+relevancia)**, por índice |

## Reglas de clasificación (por evento, por índice)

Dado `actual`, `forecast`, `regla.direccion`, `regla.tolerancia_pct`:

1. Si `actual == null` o `forecast == null` → **PENDIENTE** (no clasificable).
2. Desviación relativa = `|actual - forecast| / |forecast|`.
3. Si la desviación relativa ≤ `tolerancia_pct / 100` → **AMARILLO** (dentro de lo esperado).
4. En otro caso (fuera de tolerancia):
   - `direccion = MAYOR_ES_ALCISTA`: `actual > forecast` → **VERDE**; `actual < forecast` → **ROJO**.
   - `direccion = MAYOR_ES_BAJISTA`: `actual > forecast` → **ROJO**; `actual < forecast` → **VERDE**.

**Justificación explícita**: real (=actual), pronóstico (=forecast), desviación (=actual − forecast)
y estado, para trazabilidad.

## Reglas de agregación (tendencia diaria por índice)

- Contribución por evento clasificado: **VERDE = +1**, **ROJO = −1**, **AMARILLO = 0**.
- Ponderación: `contribución × peso` (impacto: Alto=3, Medio=1) × `factor_relevancia` del índice.
  `factor_relevancia`: alta = 1.0, media = 0.5, baja = 0.0 (propuesto por defecto, configurable).
- **Puntaje del día (índice X)** = suma de contribuciones de los eventos del día (en ET).
- Clasificación: puntaje `> 0` → **ALCISTA**; `< 0` → **BAJISTA**; `= 0` → **NEUTRAL**
  (umbrales configurables para Fase 7).

## Direcciones por indicador (propuestas por defecto, para el índice)

Regla general, derivada del PDF (impacto en USD) **invertida para el índice** en los indicadores
de tasas/inflación:

- **Inflación/costos** (CPI, Core CPI, PCE, Core PCE, PPI, salarios por hora): mayor = inflación =
  hawkish → **`mayor_es_bajista`** (índice baja).
- **Tasas (FOMC)**: tasa mayor = hawkish → **`mayor_es_bajista`**.
- **Desempleo** (tasa de desempleo, Jobless Claims): mayor = debilidad → **`mayor_es_bajista`**.
- **Empleo** (NFP, ADP, JOLTS): mayor = fortaleza → **`mayor_es_alcista`** (dirección fija).
- **Crecimiento/actividad** (PIB, ISM, PMI, ventas minoristas, bienes duraderos, producción
  industrial, vivienda): mayor = crecimiento → **`mayor_es_alcista`**.
- **Confianza/sentimiento** (UMich, Conference Board): mayor = optimismo → **`mayor_es_alcista`**.
- **Balanza comercial**: mayor (menos déficit) = positivo → **`mayor_es_alcista`** (relevancia baja).
- **Inventarios mayoristas**: matizado; por defecto **`mayor_es_alcista`** (relevancia baja).

> Las direcciones coinciden mayormente con el catálogo de Fase 1; el refinamiento principal son
> los **alias FF** (`FOMC Statement`, `Prelim GDP q/q`, `Prelim/Revised UoM Consumer Sentiment`).

## Fuente del "actual" (FRED/BLS)

- **Puerto** `ValorActualPort.obtenerActual(indicadorId, fechaHoraUtc)` → `Optional<BigDecimal>`.
- **Adaptador** `FredBlsValorActualAdapter` (RestClient): consulta la serie FRED/BLS asociada al
  indicador y transforma el nivel en la variación comparable al `forecast` de FF.
- **Catálogo de series**: mapeo `indicador → serie + transformación` (nivel→variación m/m, o
  directo). Ejemplos: NFP → `PAYEMS` (variación), desempleo → `UNRATE` (directo), CPI → `CPIAUCSL`
  (variación m/m).
- **Latencia**: FRED/BLS actualizan con demora tras la publicación; un "actual" ausente se trata
  como `null` (PENDIENTE).

## Convenciones

- El motor de reglas es un **servicio de dominio** (puro, sin framework): clasificación y
  agregación deterministas y unit-testables.
- `EstadoEvento` (VERDE/AMARILLO/ROJO/PENDIENTE) y `ClasificacionTendencia` (ALCISTA/NEUTRAL/BAJISTA)
  son enums de dominio.
- Los parámetros (`factor_relevancia`, umbrales) son **configurables por defecto**, revisables.

## Fuera de alcance (fases posteriores)

- API REST de refresco y consulta (Fase 5).
- Dashboard (Fase 6).
- Ajuste dinámico por régimen de tasas (se usa dirección fija determinista).
- Ampliar el catálogo con indicadores adicionales del PDF (Flash PMI, Core PPI, ECI, CPI y/y…);
  se puede hacer como catálogo en una iteración posterior.
