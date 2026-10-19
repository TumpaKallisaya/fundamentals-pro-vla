# Requisitos — Fase 2: Modelo de datos y persistencia (backend)

## Contexto

Tercera fase de implementación del **Sistema IndicadoresFundamentales** (ver `../mission.md` y
`../tech-stack.md`). Corresponde a la **Fase 2** del `../roadmap.md`.

**Objetivo de la fase**: modelar las entidades de dominio, crear el esquema de base de datos
(migraciones Flyway) y los repositorios Spring Data JPA, y sembrar el catálogo de indicadores
definido en la Fase 1. Todo bajo la **arquitectura hexagonal estricta** (dominio sin framework).

## Alcance

En esta fase se entrega:

1. **Entidades de dominio puras** (solo JDK) en `domain.model`.
2. **Puertos de salida** (interfaces de repositorio) en `domain.port.out`.
3. **Migración Flyway** `V2__create_tablas.sql` con las 7 tablas.
4. **Entidades JPA + repositorios Spring Data + mappers + adaptadores** en
   `infrastructure.adapter.out.persistence`.
5. **Seeder** que carga el catálogo de la Fase 1 (2 índices y 23 indicadores).
6. **Pruebas** unitarias (Mockito) y de integración de persistencia.

## Decisiones confirmadas

| Decisión | Valor |
|---|---|
| Separación dominio/persistencia | **Estricta**: dominio puro (JDK); JPA en infraestructura con mappers |
| Modelo de `Resultado` | **Histórico por refresco** (una fila por evento y por refresco) |
| Índices | **Entidad `Indice`** + tabla de relación `Indicador↔Indice` con relevancia |
| Zona horaria | **UTC** en BD (`TIMESTAMPTZ`); convertir a ET solo al mostrar |
| Carga del catálogo | **Seeder Java** (ApplicationRunner) leyendo el JSON de Fase 1 |
| Migraciones | Flyway (ya configurado; `V1__init.sql` existente) |
| IDs | Clave natural (String) en `Indice`/`Indicador`; `BIGSERIAL` (Long) en el resto |

## Modelo de datos (7 tablas)

### Entidades de dominio

| Entidad | Clave | Campos | Relaciones |
|---|---|---|---|
| **Indice** | `codigo` (String) | `nombre`, `sector` | 1—N con `IndicadorIndice` |
| **Indicador** | `id` (String, slug) | `nombre`, `aliasFf`, `sector`, `impacto`, `frecuencia`, `horaEt`, `fuente` (VO), `regla` (VO) | 1—N con `IndicadorIndice` |
| **IndicadorIndice** | `id` (Long) | `relevancia` | N—1 `Indicador`, N—1 `Indice`; unique(indicador, indice) |
| **Evento** | `id` (Long) | `fechaHoraUtc`, `forecast`, `previous` | N—1 `Indicador`; unique(indicador, fechaHoraUtc) |
| **Resultado** | `id` (Long) | `actual` | N—1 `Evento`, N—1 `Refresco`; unique(evento, refresco) |
| **TendenciaDiaria** | `id` (Long) | `fecha`, `puntaje`, `clasificacion` | N—1 `Indice`; unique(indice, fecha) |
| **Refresco** | `id` (Long) | `iniciadoEnUtc`, `finalizadoEnUtc`, `estado`, `detalle` | 1—N con `Resultado` |

### Value objects

- **`Fuente`**: `nombre`, `tipo`, `url`, `notas` (mapeado como `@Embeddable`).
- **`Regla`**: `direccion`, `peso`, `toleranciaPct` (mapeado como `@Embeddable`).

### Enums

- `Impacto`: `ALTO`, `MEDIO`.
- `Direccion`: `MAYOR_ES_ALCISTA`, `MAYOR_ES_BAJISTA`.
- `Relevancia`: `ALTA`, `MEDIA`, `BAJA`.
- `EstadoRefresco`: `EN_PROCESO`, `COMPLETADO`, `PARCIAL`, `ERROR`.
- `ClasificacionTendencia`: `ALCISTA`, `NEUTRAL`, `BAJISTA`.

### Observaciones

- `forecast`, `previous` y `actual` se almacenan como `NUMERIC` (nullable). La normalización de los
  valores textuales del feed (p. ej. `"150K"`, `"0.2%"`) la hará el conector en la Fase 3.
- `horaEt` es documental (patrón típico de publicación); la hora exacta de cada `Evento` se
  normaliza a UTC en `fechaHoraUtc`.
- El seguimiento de **revisiones** (`previous` revisado) se podrá añadir en Fase 3 sobre el modelo
  histórico de `Resultado`, sin cambios de esquema.

## Estructura de paquetes (hexagonal estricta)

```
com.funds.backend
├── domain
│   ├── model              # Indicador, Indice, IndicadorIndice, Evento, Resultado,
│   │                      # TendenciaDiaria, Refresco + Fuente, Regla + enums
│   ├── port
│   │   ├── in             # (HealthUseCase existente)
│   │   └── out            # IndicadorRepository, IndiceRepository, EventoRepository,
│   │                      # ResultadoRepository, TendenciaDiariaRepository, RefrescoRepository
│   └── service            # (vacío; motor de reglas en Fase 4)
├── application
│   └── service            # HealthService + CatalogoSeedService (seeder)
└── infrastructure
    ├── adapter
    │   ├── in/web         # HealthController (existente)
    │   └── out/persistence
    │       ├── entity     # IndicadorEntity, IndiceEntity, ... (JPA)
    │       ├── repository # JpaIndicadorRepository, ... (Spring Data)
    │       ├── mapper     # IndicadorMapper, ... (dominio ↔ JPA)
    │       └── adapter    # IndicadorRepositoryAdapter, ... (implementan puertos)
    └── config             # WebConfig + CatalogSeedRunner (ApplicationRunner)
```

## Seed del catálogo

- El JSON `indicadores.seed.json` de la Fase 1 se copia a
  `backend-funds/src/main/resources/db/seed/indicadores.json` (única fuente de verdad en runtime).
- El seeder (`CatalogoSeedService` + `CatalogSeedRunner`) lo lee y siembra de forma **idempotente**:
  2 índices (`NAS100`, `SP500`) y 23 indicadores con sus relevancias por índice.

## Zona horaria

- Las columnas de fecha/hora se declaran `TIMESTAMPTZ` y se guardan en **UTC**.
- La conversión a America/New_York (ET) se hace únicamente en la capa de presentación o en la API
  (fases posteriores), nunca en el modelo de dominio.

## Convenciones

- El dominio **no** importa clases de Spring/JPA; los repositorios son interfaces puras en
  `domain.port.out`.
- Los adaptadores de salida son los únicos que conocen JPA/Spring Data.
- Nombres de tabla en `snake_case`; columnas de FK como `xxx_id`.
- Valores de dinero/cifras económicas como `NUMERIC` (null si no disponible).

## Fuera de alcance (fases posteriores)

- Conectores a fuentes externas y captura del valor `actual` (Fase 3).
- Motor de reglas de interpretación y cálculo de `TendenciaDiaria` (Fase 4).
- API REST de negocio (Fase 5).
- Cualquier endpoint nuevo (solo persistencia y seed en esta fase).
