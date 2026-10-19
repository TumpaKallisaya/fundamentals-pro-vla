# Requisitos — Fase 3: Conectores de fuentes externas (backend)

## Contexto

Cuarta fase de implementación del **Sistema IndicadoresFundamentales** (ver `../mission.md` y
`../tech-stack.md`). Corresponde a la **Fase 3** del `../roadmap.md`.

**Objetivo de la fase**: conectar el backend con la fuente externa (Forex Factory) para obtener el
calendario económico, normalizarlo al modelo de dominio (Fase 2) y persistirlo con trazabilidad.
Sigue la arquitectura hexagonal: el conector es un **puerto de salida** y su implementación un
**adaptador**.

## Alcance

En esta fase se entrega:

1. **Puerto de salida** `CalendarioEconomicoPort` y modelo `EventoCalendario`.
2. **Adaptador** `ForexFactoryCalendarioAdapter` (RestClient) que consulta el feed JSON.
3. **Normalización** de los datos al modelo de dominio (filtro, zona horaria, parseo de valores).
4. **Resiliencia**: caché en memoria con TTL, reintentos con backoff y timeouts.
5. **Caso de uso** `SincronizarCalendarioService` que persiste eventos y registra el `Refresco`.
6. **Pruebas** unitarias (Mockito) del adaptador y del caso de uso.

## Decisiones confirmadas

| Decisión | Valor |
|---|---|
| Fuente | **Solo Forex Factory** (feed JSON del calendario) |
| Valor `actual` | **Queda pendiente** (null); el feed gratuito no lo incluye |
| Flujo | **Sincronizar + persistir con Refresco** (trazabilidad) |
| Cliente HTTP | **RestClient** (síncrono) |
| Resiliencia | **Caché en memoria (TTL) + reintentos con backoff + timeouts** |
| Zona horaria | **UTC** interno (TIMESTAMPTZ), como en Fase 2 |

## Modelo del conector

### Puerto (domain.port.out)

```java
public interface CalendarioEconomicoPort {
    List<EventoCalendario> obtenerEventos();
}
```

### Modelo de dominio (domain.model)

```java
public record EventoCalendario(
        String titulo,          // title del feed (→ alias_ff)
        String pais,            // country/divisa (p. ej. "USD")
        Instant fechaHoraUtc,   // date normalizado a UTC
        Impacto impacto,        // ALTO | MEDIO
        BigDecimal forecast,    // parseado, nullable
        BigDecimal previous     // parseado, nullable
) {
}
```

## Normalización del feed de Forex Factory

Feed: `https://nfs.faireconomy.media/ff_calendar_thisweek.json` (eventos agrupados por día).

1. **Aplanar**: convertir la estructura (días → eventos) en una lista plana.
2. **País**: conservar solo `country == "USD"` (EE.UU.).
3. **Impacto**: conservar `impact` en {High, Medium} → `Impacto.ALTO`/`MEDIO`; descartar
   Low/Holiday.
4. **Fecha**: `date` llega con offset US/Eastern → parsear a `Instant` (UTC). Si llega sin offset,
   interpretar como America/New_York y convertir a UTC.
5. **Valores**: `forecast`/`previous` como string → `BigDecimal` (null si vacío). Reglas: quitar
   `%`; sufijos `K`=×10³, `M`=×10⁶, `B`=×10⁹.
6. **`actual`**: no disponible en el feed gratuito → queda `null` (decisión de Fase 1).

## Caso de uso de sincronización

`SincronizarCalendarioService` (capa de aplicación):

1. Crea un `Refresco` en estado `EN_PROCESO`.
2. Consulta `CalendarioEconomicoPort.obtenerEventos()`.
3. Por cada `EventoCalendario`: busca el `Indicador` por `alias_ff` (`findByAliasFf`); si no
   existe en el catálogo, lo descarta.
4. Crea/actualiza el `Evento` (clave natural `indicador_id + fecha_hora_utc`).
5. Cierra el `Refresco` como `COMPLETADO` (con resumen) o `ERROR`/`PARCIAL` ante fallos.

## Resiliencia (en el adaptador)

- **Caché en memoria** con TTL de 30–60 min (configurable) para no exceder el rate limit de FF.
- **Reintentos**: hasta 3 intentos con backoff exponencial (p. ej. 1s, 2s, 4s) ante HTTP 429/5xx.
- **Timeouts**: conexión y lectura configurables (p. ej. 5s / 10s).

## Convenciones

- El conector vive en `infrastructure.adapter.out.web` (adaptador de salida); el dominio no conoce
  HTTP ni Jackson.
- Los DTOs de lectura del feed (estructura JSON) son privados del adaptador.
- El parseo de valores y fechas es responsabilidad del adaptador; el caso de uso solo orquesta.

## Fuera de alcance (fases posteriores)

- Obtener el valor **`actual`** (requiere decidir fuente: oficial o scraping). **Nota:** la Fase 4
  (interpretación actual vs. pronóstico) dependerá de resolver este punto.
- Otras fuentes (FRED, BLS, BEA) y scraping.
- API REST de refresco (Fase 5) y motor de reglas (Fase 4).
- Scheduler automático (solo refresco bajo demanda).
