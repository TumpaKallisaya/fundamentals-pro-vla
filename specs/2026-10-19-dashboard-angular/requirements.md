# Requisitos — Fase 6: Frontend (dashboard Angular)

## Contexto

Séptima fase de implementación del **Sistema IndicadoresFundamentales** (ver `../mission.md` y
`../tech-stack.md`). Corresponde a la **Fase 6** del `../roadmap.md`: el dashboard Angular que
consume la API REST (Fase 5) y materializa el MVP (actualizar y visualizar con un clic).

## Alcance

En esta fase se entrega:

1. **Angular Material** como librería de UI.
2. **Shell + routing**: `app.ts` como shell con `<router-outlet>` y la feature `dashboard` enrutada.
3. **Dashboard** con dos secciones, filtros, botón "Actualizar" e indicadores de estado.

## Decisiones confirmadas

| Decisión | Valor |
|---|---|
| UI | **Angular Material** (tablas, botón, select, spinner, date pickers) |
| Estructura | **Shell + feature `dashboard` enrutada** (Feature-Based) |
| Estado | **Signals** (reactividad, sin NgRx) |
| Zona horaria | **America/New_York (ET)** para agrupar/mostrar días |
| Rango por defecto | **Mes en curso** (ET) |

## Estructura (Feature-Based)

```
src/app/
├── app.ts                        # shell: layout + <router-outlet>
├── app.routes.ts                 # ruta "/" → dashboard
├── core/                         # (sin cambios)
├── shared/                       # (sin cambios)
└── features/
    ├── health/                   # (existente)
    └── dashboard/
        ├── dashboard.component.ts
        ├── dashboard.service.ts
        ├── dashboard.model.ts
        └── dashboard.component.spec.ts
```

## API consumida (backend Fase 5)

| Operación | Endpoint |
|---|---|
| Refrescar | `POST /api/refresco` |
| Eventos (semáforo) | `GET /api/eventos?desde&hasta&indice` |
| Tendencias | `GET /api/tendencias?desde&hasta&indice` |
| Último refresco | `GET /api/refresco/ultimo` |

La URL base está en `src/environments/environment.ts` (`apiUrl`).

## Modelos (TypeScript)

- `EstadoEvento`: `'VERDE' | 'AMARILLO' | 'ROJO' | 'PENDIENTE'`.
- `ClasificacionTendencia`: `'ALCISTA' | 'NEUTRAL' | 'BAJISTA'`.
- `EventoDto`, `TendenciaDto`, `RefrescoDto` (espejo de los DTOs del backend).

## Secciones

### Sección 1 — Semáforo mensual por indicador

- Filas = indicadores; columnas = fechas del rango (ET).
- Celda coloreada por `estado`: **verde** (mejor), **amarillo** (dentro), **rojo** (peor),
  **gris** (pendiente/futuro).
- Filtro por índice (select `NAS100` / `SP500`).

### Sección 2 — Tendencia diaria por índice

- Filas = índices (`NAS100`, `SP500`); columnas = fechas del rango.
- Celda con la clasificación (alcista/neutral/bajista) y su color.

## Comunes

- Botón **"Actualizar"**: `POST /api/refresco` + recarga de ambas secciones y del último refresco.
- Indicadores: spinner de carga, mensaje de error y último refresco (estado + hora en ET).

## Zona horaria

- Los eventos llegan con `fechaHoraUtc` (Instant); el frontend los agrupa por **día en ET** para
  ubicarlos en las columnas.
- Las tendencias llegan con `fecha` (día ET) y se usan directamente.

## Convenciones

- Componentes standalone + Signals.
- El servicio del dashboard encapsula las llamadas HTTP; el componente gestiona el estado con
  Signals (cargando, error, filtros, datos).
- Sin librerías de gráficos: solo tablas (según el roadmap).

## Fuera de alcance (fases posteriores)

- Autenticación (Fase 7 / futura).
- Scheduler automático (solo refresco manual).
- Sectores forex/cripto (Fase 8).
