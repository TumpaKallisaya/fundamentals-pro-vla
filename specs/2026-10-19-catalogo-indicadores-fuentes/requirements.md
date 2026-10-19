# Requisitos — Fase 1: Catálogo de indicadores y fuentes

## Contexto

Segunda fase de implementación del **Sistema IndicadoresFundamentales** (ver `../mission.md` y
`../tech-stack.md`). Corresponde a la **Fase 1** del `../roadmap.md`.

**Objetivo de la fase**: elaborar el **catálogo de indicadores** de alto y medio impacto de la
economía de EE.UU. para los índices **NAS100 (Nasdaq 100)** y **SP500 (S&P 500)**, con su fuente
de datos y su regla de interpretación, en un formato listo para modelar (Fase 2) y conectar
(Fase 3).

## Alcance

En esta fase se entrega:

1. **Modelo del catálogo**: campos por indicador (ver sección "Modelo del catálogo").
2. **Listado de indicadores**: 12 de impacto alto y 11 de impacto medio de EE.UU. (23 en total),
   ver sección "Listado de indicadores".
3. **Relevancia por índice**: para cada indicador, relevancia `alta`/`media`/`baja` para NAS100
   y SP500, con su justificación.
4. **Regla de interpretación** por indicador (dirección favorable, peso y tolerancia), con
   valores **propuestos por defecto** para revisión del trader.
5. **Documento de catálogo** (Markdown) legible.
6. **Seed machine-readable** (JSON) listo para importar en Fase 2.

## Decisiones confirmadas

| Decisión | Valor |
|---|---|
| Cobertura de indicadores | Todos los de impacto **alto y medio** de EE.UU. (23) |
| Sector | **Sector de mercado** (`índices` en esta fase; `forex`/`cripto` en Fase 8) |
| Formato del entregable | **Markdown** (legible) + **seed JSON** (machine-readable) |
| Fuente de datos | **Solo Forex Factory** (feed JSON público) |
| Regla de interpretación | **Propuesta por defecto** por el agente; revisión del trader antes de cerrar |
| Relación con índices | **Relevancia por índice** (NAS100 y SP500 pueden diferir) |
| Formato del seed | **JSON** (nativo de Spring/Jackson para sembrar en Fase 2) |

## Modelo del catálogo

Campos por indicador:

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | string | Slug único normalizado (`snake_case`), p. ej. `nonfarm_payrolls` |
| `nombre` | string | Título legible del evento |
| `alias_ff` | string | Título tal como aparece en el feed de Forex Factory (mapeo en Fase 3) |
| `sector` | string | Sector de mercado: `indices` (constante en esta fase) |
| `impacto` | string | `Alto` \| `Medio` |
| `frecuencia` | string | Periodicidad/momento típico (documental) |
| `hora_et` | string | Hora típica de publicación en ET (documental; la exacta viene del feed) |
| `indices` | array | Índices afectados con relevancia: `[{ indice, relevancia }]` |
| `fuente` | object | Fuente de datos (nombre, tipo, url, notas) |
| `regla` | object | `{ direccion, peso, tolerancia_pct }` |

Semántica de la regla:

- `regla.direccion`: `mayor_es_alcista` (un valor mayor al pronóstico favorece al índice) o
  `mayor_es_bajista` (un valor mayor lo perjudica).
- `regla.peso`: pondera el impacto (`Alto` = 3, `Medio` = 1; configurable).
- `regla.tolerancia_pct`: umbral `±X%` sobre el pronóstico dentro del cual el resultado se
  considera "dentro de lo esperado" (configurable por indicador).

Ejemplo de estructura en el seed:

```json
{
  "id": "nonfarm_payrolls",
  "nombre": "Non-Farm Employment Change",
  "alias_ff": "Non-Farm Employment Change",
  "sector": "indices",
  "impacto": "Alto",
  "frecuencia": "Mensual (primer viernes)",
  "hora_et": "08:30",
  "indices": [
    { "indice": "NAS100", "relevancia": "alta" },
    { "indice": "SP500", "relevancia": "alta" }
  ],
  "fuente": {
    "nombre": "Forex Factory",
    "tipo": "api",
    "url": "https://nfs.faireconomy.media/ff_calendar_thisweek.json",
    "notas": "Incluye forecast/previous; NO incluye actual."
  },
  "regla": { "direccion": "mayor_es_alcista", "peso": 3, "tolerancia_pct": 20 }
}
```

## Listado de indicadores (alcance)

**Impacto ALTO (12):** FOMC (decisión de tasas), Non-Farm Payrolls (NFP), tasa de desempleo,
salarios medios por hora, CPI, Core CPI, PCE, Core PCE, PIB (GDP, avance), ISM manufacturero,
ISM de servicios, ventas minoristas.

**Impacto MEDIO (11):** Jobless Claims (iniciales), ADP (empleo no agrícola), JOLTS (aperturas de
empleo), PPI, bienes duraderos, inventarios mayoristas, balanza comercial, sentimiento del
consumidor (UMich), confianza del consumidor (Conference Board), producción industrial, ventas de
viviendas.

> Los eventos sin semántica `actual`/`pronóstico`/`anterior` (discursos del FOMC, subastas del
> Tesoro, titulares geopolíticos, resultados corporativos) quedan fuera del catálogo de
> indicadores; se tratarán como contexto en fases posteriores.

## Fuentes

- **Fuente primaria (única en esta fase): Forex Factory** — feed JSON público
  `https://nfs.faireconomy.media/ff_calendar_thisweek.json` (semana actual).
- Campos disponibles: `title`, `country`, `date` (offset US/Eastern), `impact`, `forecast`,
  `previous`.
- **Limitación conocida:** el feed gratuito **no incluye el valor `actual`**. La obtención del
  `actual` se resolverá en la Fase 3 (conectores), evaluando entonces el cruce con fuentes
  oficiales (FRED/BLS/BEA) o scraping, según se decida. Esta limitación no bloquea el catálogo.
- Rate limit: cachear; consultar máx. 1 vez cada 30–60 min (ver
  `../fuentes_trading_scraping_api.md`).

## Convenciones

- **Id normalizado**: `snake_case`, estable y único (clave del catálogo).
- **Zona horaria**: America/New_York (ET) para toda hora mostrada o calculada.
- **Relevancia**: `alta` | `media` | `baja`, una por índice.
- **Dirección**: `mayor_es_alcista` | `mayor_es_bajista` (efecto sobre el índice).
- **Peso**: `Alto` = 3, `Medio` = 1 (configurable).
- **Tolerancia**: porcentaje `±X%` sobre el pronóstico (configurable por indicador).
- Los valores de regla son **propuestos por defecto** y deben ser revisados por el trader.

## Fuera de alcance (fases posteriores)

- Modelo de datos de dominio y persistencia (Fase 2).
- Conectores e integración con las fuentes (Fase 3).
- Motor de reglas de interpretación y agregación de tendencia (Fase 4).
