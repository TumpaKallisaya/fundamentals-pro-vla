# Validación — Fase 4: Motor de reglas de interpretación (y fuente del "actual")

Cómo determinar si la implementación fue exitosa y puede integrarse al código principal.

## Criterios de éxito (Definition of Done)

- [ ] Existe la carpeta `specs/2026-10-19-motor-reglas-interpretacion/` con `plan.md`,
  `requirements.md` y `validation.md`.
- [ ] Existen `domain.port.out.ValorActualPort` y `FredBlsValorActualAdapter` (FRED/BLS).
- [ ] Existe el catálogo de series (indicador → serie FRED/BLS + transformación).
- [ ] La sincronización persiste `Resultado` con el valor "actual" (o null si no disponible).
- [ ] Existe `domain.service.ClasificadorEventoService` con los 3 estados + PENDIENTE y
  justificación (real, pronóstico, desviación, estado).
- [ ] La clasificación respeta la regla: dentro de tolerancia → AMARILLO; fuera → VERDE/ROJO según
  dirección.
- [ ] Existe `domain.service.AgregadorTendenciaService` con puntaje ponderado por impacto y
  relevancia, por índice, y persiste `TendenciaDiaria`.
- [ ] Las direcciones del catálogo están validadas/ajustadas según el PDF + web (traducidas al
  índice) y los alias FF corregidos (`FOMC Statement`, `Prelim GDP q/q`, `Prelim/Revised UoM`).
- [ ] `mvn test` pasa (pruebas del clasificador, agregador y adaptador FRED/BLS).
- [ ] El motor es puro (sin framework) y determinista.

## Verificación manual (comandos)

```
# backend: compilar y probar
cd backend-funds
./mvnw test          # Windows: mvnw.cmd test
```

## Criterios para integrar al principal

- Todos los checks anteriores están marcados.
- El motor respeta la **arquitectura hexagonal** (servicios de dominio puros; puertos y
  adaptadores para FRED/BLS).
- La clasificación y agregación son **deterministas y trazables** (justificación explícita por
  evento), coherentes con `mission.md`.
- Los valores propuestos (direcciones, `factor_relevancia`, umbrales) están marcados para revisión
  del trader.
- El commit contiene solo los cambios de esta fase (backend + catálogo/seed), sin tocar fases
  posteriores.
