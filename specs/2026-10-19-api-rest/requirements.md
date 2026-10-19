# Requisitos — Fase 5: API REST (backend)

## Contexto

Sexta fase de implementación del **Sistema IndicadoresFundamentales** (ver `../mission.md` y
`../tech-stack.md`). Corresponde a la **Fase 5** del `../roadmap.md`.

**Objetivo**: exponer por HTTP la funcionalidad completa del backend: disparar el refresco (que
consulta fuentes, resuelve el "actual", clasifica y recalcula tendencias) y consultar eventos,
resultados y tendencias por rango de fechas.

## Alcance

En esta fase se entrega:

1. **Migración V4**: columnas `estado` y `desviacion` en `resultado`.
2. **Persistencia del estado**: `CalcularTendenciasService` calcula y guarda `estado` y `desviacion`.
3. **Caso de uso `RefrescoService`**: orquesta sincronización + resolución + cálculo.
4. **Endpoints REST** (ver abajo).
5. **DTOs de respuesta**.
6. **Pruebas** unitarias (Mockito).

## Decisiones confirmadas

| Decisión | Valor |
|---|---|
| Orquestación del refresco | **Un solo endpoint** `POST /api/refresco` (flujo completo) |
| Estado por evento | **Persistir** `estado` y `desviacion` en `Resultado` (migración V4) |
| Rango de fechas | `desde`/`hasta` en **America/New_York**; por defecto el mes en curso |
| Filtro por índice | Parámetro opcional `indice` (`NAS100`/`SP500`) |
| Autenticación | Sin autenticación en esta fase (uso local) |

## Endpoints REST (bajo `/api`)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/refresco` | Dispara el flujo completo; devuelve el estado del refresco |
| GET | `/api/eventos` | Eventos con resultado y clasificación; `desde`, `hasta`, `indice` |
| GET | `/api/tendencias` | Tendencia diaria por índice; `desde`, `hasta`, `indice` |
| GET | `/api/refresco/ultimo` | Estado del último refresco |

### Parámetros de consulta

- `desde` / `hasta`: fechas `YYYY-MM-DD` en ET (opcionales; por defecto, mes en curso).
- `indice`: `NAS100` o `SP500` (opcional; si se omite, se devuelven ambos).

## Flujo del refresco (`RefrescoService`)

1. Crea un `Refresco` en `EN_PROCESO`.
2. Sincroniza el calendario (Forex Factory) → persiste `Evento` (forecast/previous).
3. Resuelve el "actual" (FRED/BLS) → persiste `Resultado` con `actual`, `estado` y `desviacion`.
4. Clasifica cada evento y calcula la `TendenciaDiaria` por índice.
5. Cierra el `Refresco` en `COMPLETADO` (o `ERROR`/`PARCIAL`).

## DTOs de respuesta

- **EventoDto**: `id`, `indicadorId`, `nombre`, `fechaHoraUtc`, `forecast`, `previous`, `actual`,
  `estado`, `desviacion`.
- **TendenciaDto**: `indiceCodigo`, `fecha`, `puntaje`, `clasificacion`.
- **RefrescoDto**: `id`, `estado`, `detalle`, `iniciadoEnUtc`, `finalizadoEnUtc`.

## Convenciones

- Los controladores son **adaptadores de entrada** (`infrastructure.adapter.in.web`): solo traducen
  HTTP y delegan en casos de uso; no contienen lógica de negocio.
- Los DTOs de respuesta viven en el adaptador de entrada y se mapean desde el dominio.
- Las fechas se devuelven normalizadas a America/New_York (ET) en la presentación.

## Fuera de alcance (fases posteriores)

- Dashboard frontend (Fase 6).
- Autenticación/autorización.
- Scheduler automático (solo refresco bajo demanda vía botón).
