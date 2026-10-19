# Catálogo de indicadores fundamentales (Fase 1)

Catálogo de los indicadores de **impacto alto y medio** de la economía de EE.UU. para los índices
**NAS100 (Nasdaq 100)** y **SP500 (S&P 500)**.

> **Fuente de datos (única en esta fase):** Forex Factory (feed JSON público
> `https://nfs.faireconomy.media/ff_calendar_thisweek.json`).
> **Limitación conocida:** el feed gratuito incluye `forecast` y `previous`, pero **no incluye el
> valor `actual`**. La obtención del `actual` se resolverá en la Fase 3 (conectores).

La versión machine-readable de este catálogo está en
[`indicadores.seed.json`](./indicadores.seed.json).

## Cómo leer este catálogo

| Campo | Descripción |
|---|---|
| **Dirección** | Efecto sobre el índice si el valor sale **mayor** al pronóstico: `alcista` (lo favorece) o `bajista` (lo perjudica) |
| **Peso** | Pondera el impacto en la tendencia del día: `Alto` = 3, `Medio` = 1 |
| **Tolerancia** | Umbral `±X%` sobre el pronóstico dentro del cual el resultado es "dentro de lo esperado" (amarillo) |
| **Relevancia** | Sensibilidad del indicador a cada índice: `alta` / `media` / `baja` |

> ⚠️ **Dirección, peso y tolerancia son valores propuestos por defecto** y deben ser revisados
> por el trader. La semántica exacta de la tolerancia (porcentaje vs. absoluto) se refinará en la
> Fase 4 (motor de reglas).

---

## Impacto ALTO (12)

| Indicador | id | Hora (ET) | Frecuencia | NAS100 | SP500 | Dirección | Peso | Tol. |
|---|---|---|---|---|---|---|---|---|
| FOMC — Decisión de tipos | `fomc_tasas` | 14:00 | 8 veces/año | alta | alta | bajista | 3 | 5% |
| Non-Farm Payrolls (NFP) | `nonfarm_payrolls` | 08:30 | Mensual (1er viernes) | alta | alta | alcista | 3 | 20% |
| Tasa de desempleo | `tasa_desempleo` | 08:30 | Mensual (1er viernes) | media | media | bajista | 3 | 5% |
| Salarios medios por hora | `salarios_hora` | 08:30 | Mensual (1er viernes) | alta | alta | bajista | 3 | 20% |
| CPI (m/m) | `cpi` | 08:30 | Mensual | alta | alta | bajista | 3 | 15% |
| Core CPI (m/m) | `core_cpi` | 08:30 | Mensual | alta | alta | bajista | 3 | 15% |
| PCE (m/m) | `pce` | 08:30 | Mensual | alta | alta | bajista | 3 | 15% |
| Core PCE (m/m) | `core_pce` | 08:30 | Mensual | alta | alta | bajista | 3 | 15% |
| PIB (GDP, avance) | `pib` | 08:30 | Trimestral | media | media | alcista | 3 | 15% |
| ISM Manufacturero | `ism_manufacturero` | 10:00 | Mensual (1er día hábil) | media | media | alcista | 3 | 5% |
| ISM de Servicios | `ism_servicios` | 10:00 | Mensual | media | media | alcista | 3 | 5% |
| Ventas minoristas (m/m) | `ventas_minoristas` | 08:30 | Mensual | media | alta | alcista | 3 | 20% |

## Impacto MEDIO (11)

| Indicador | id | Hora (ET) | Frecuencia | NAS100 | SP500 | Dirección | Peso | Tol. |
|---|---|---|---|---|---|---|---|---|
| Jobless Claims (iniciales) | `jobless_claims` | 08:30 | Semanal (jueves) | media | media | bajista | 1 | 10% |
| ADP — Empleo no agrícola | `adp` | 08:15 | Mensual | media | media | alcista | 1 | 20% |
| JOLTS — Aperturas de empleo | `jolts` | 10:00 | Mensual | media | media | alcista | 1 | 15% |
| PPI (m/m) | `ppi` | 08:30 | Mensual | media | media | bajista | 1 | 20% |
| Bienes duraderos (m/m) | `bienes_duraderos` | 08:30 | Mensual | media | media | alcista | 1 | 20% |
| Inventarios mayoristas (m/m) | `inventarios_mayoristas` | 10:00 | Mensual | baja | baja | alcista | 1 | 20% |
| Balanza comercial | `balanza_comercial` | 08:30 | Mensual | baja | baja | alcista | 1 | 20% |
| Sentimiento del consumidor (UMich) | `umich` | 10:00 | Mensual (prelim/final) | media | media | alcista | 1 | 5% |
| Confianza del consumidor (CB) | `confianza_consumidor` | 10:00 | Mensual | media | alta | alcista | 1 | 5% |
| Producción industrial (m/m) | `produccion_industrial` | 09:15 | Mensual | baja | media | alcista | 1 | 20% |
| Ventas de viviendas nuevas | `ventas_viviendas` | 10:00 | Mensual | baja | media | alcista | 1 | 15% |

---

## Justificación de la relevancia por índice

**NAS100 (Nasdaq 100)** concentra tecnológicas de larga duración, muy sensibles a las **tasas de
interés** y a la **expectativa de inflación** (descuento de flujos futuros). Por eso los eventos de
política monetaria e inflación (FOMC, CPI/Core CPI, PCE/Core PCE, salarios) tienen relevancia
**alta**.

**SP500 (S&P 500)** es más amplio y con mayor peso del **consumo** y del sector industrial
tradicional. Por eso eventos de consumo (ventas minoristas, confianza del consumidor) y de
actividad industrial (producción industrial, vivienda) tienen relevancia **alta** o **media** en
SP500, mientras que en NAS100 son **media** o **baja**.

- **Tasas e inflación** (FOMC, CPI, Core CPI, PCE, Core PCE, PPI, salarios): relevancia **alta** en
  ambos; el efecto bajista de una sorpresa alcista en inflación domina por el canal de tasas.
- **Empleo** (NFP, desempleo, ADP, JOLTS, Jobless Claims): relevancia **media/alta** en ambos;
  el NFP es el de mayor sensibilidad por su lectura simultánea de crecimiento y de presión salarial.
- **Consumo** (ventas minoristas, confianza, UMich): mayor sensibilidad en **SP500** que en NAS100.
- **Industria/vivienda** (producción industrial, vivienda, bienes duraderos, inventarios, balanza):
  relevancia **media/baja**, con ligera ventaja para SP500.

## Notas de interpretación (por indicador)

- **FOMC (`fomc_tasas`)**: la reacción del mercado depende más del **tono del comunicado** y de la
  **conferencia de prensa** (dot plot, forward guidance) que de la cifra de tasa en sí. El catálogo
  registra una dirección por defecto (`mayor_es_bajista`) que deberá matizarse en la Fase 4.
- **NFP (`nonfarm_payrolls`)**: dirección por defecto `alcista` (más empleo = crecimiento), pero en
  entornos hawkish una sorpresa muy fuerte puede leerse como bajista por expectativa de tasas.
- **Salarios (`salarios_hora`)**: un salario mayor al esperado se lee como **inflación** (hawkish),
  por eso `mayor_es_bajista` para el índice.

## Fuente de datos

- **Forex Factory (feed JSON público)**: `https://nfs.faireconomy.media/ff_calendar_thisweek.json`
  (semana actual; a veces `..._nextweek.json`).
- Campos usados: `title` (→ `alias_ff`), `impact` (→ `impacto`), `date` (offset US/Eastern → `hora_et`),
  `forecast` y `previous`.
- **Limitación:** no incluye `actual`.
- **Rate limit:** cachear; consultar máx. 1 vez cada 30–60 min y refrescar solo el día actual
  (ver `../fuentes_trading_scraping_api.md`).

## Limitaciones y pendientes

- Los `alias_ff` se basan en títulos típicos de Forex Factory; deben **validarse contra el feed
  real** en la Fase 3 (mapeo de alias → id).
- La semántica de `tolerancia_pct` (porcentaje del pronóstico) es poco natural para indicadores de
  nivel (tasa de desempleo, ISM, sentimiento); se refinará en la Fase 4.
- Eventos sin semántica `actual/pronóstico/anterior` (discursos del FOMC, subastas del Tesoro,
  titulares geopolíticos, resultados corporativos) están fuera de este catálogo.
