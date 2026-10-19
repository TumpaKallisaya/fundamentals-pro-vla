# Plan — Fase 4: Motor de reglas de interpretación (y fuente del "actual")

Serie de grupos de tareas numerados. Sigue la arquitectura hexagonal obligatoria (`../tech-stack.md`).
La fase tiene dos partes: primero se resuelve la fuente del valor **"actual"** (FRED/BLS) y luego
se implementa el **motor de reglas**.

## Parte A — Resolver la fuente del "actual" (FRED/BLS)

## Grupo 1 — Puerto y adaptador del "actual"

1. Crear `domain.port.out.ValorActualPort` con `Optional<BigDecimal> obtenerActual(String indicadorId, Instant fechaHoraUtc)`.
2. Crear `infrastructure.adapter.out.web.FredBlsValorActualAdapter` que consulta FRED/BLS vía `RestClient`.
3. Configurar URL y API key de FRED/BLS en `application.yml`.

## Grupo 2 — Catálogo de series (indicador → serie FRED/BLS)

1. Definir el mapeo `indicador → serie FRED/BLS + transformación` (nivel → variación m/m, o directo).
2. Crear el seed/config con el mapeo de los 23 indicadores.

## Grupo 3 — Integrar "actual" en la sincronización

1. Extender la sincronización para capturar el "actual" de cada evento publicado y persistir
   `Resultado` (evento + refresco + actual).
2. Manejar "actual" no disponible (aún no publicado / latencia FRED) como `null` con elegancia.

## Parte B — Motor de reglas de interpretación

## Grupo 4 — Clasificación por evento (dominio)

1. Crear el enum `EstadoEvento` (`VERDE`, `AMARILLO`, `ROJO`, `PENDIENTE`).
2. Crear `domain.service.ClasificadorEventoService`: dado actual/forecast/dirección/tolerancia →
   estado + justificación (real, pronóstico, desviación).

## Grupo 5 — Refinar direcciones del catálogo (reglas del PDF)

1. Validar/ajustar las direcciones por indicador del catálogo según el PDF + web, traducidas al
   índice (mayor → alcista/bajista para NAS100/SP500).
2. Corregir los alias FF del catálogo: `FOMC Statement`, `Prelim GDP q/q`, `Prelim/Revised UoM
   Consumer Sentiment`, etc.

## Grupo 6 — Agregador de tendencia diaria

1. Crear `domain.service.AgregadorTendenciaService`: puntaje ponderado por impacto (+relevancia)
   por índice y día → `TendenciaDiaria` (puntaje + clasificación).
2. Persistir `TendenciaDiaria` por índice y fecha (ET).

## Grupo 7 — Pruebas

1. Pruebas unitarias del clasificador (verde/amarillo/rojo/pendiente y casos límite).
2. Pruebas unitarias del agregador (puntaje, umbrales) con **Mockito**.
3. Pruebas unitarias del adaptador FRED/BLS (parseo y transformación) con fixture.

## Grupo 8 — Documentación y validación

1. Actualizar `requirements.md` y `validation.md`.
2. Verificar coherencia con `mission.md` y `tech-stack.md`.
