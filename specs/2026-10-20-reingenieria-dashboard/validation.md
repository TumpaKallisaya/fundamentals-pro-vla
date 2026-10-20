# Validación — Fase 9: Reingeniería del dashboard y fuente de datos (según mockup)

Cómo determinar si la implementación fue exitosa y puede integrarse al código principal.

## Criterios de éxito (Definition of Done)

- [ ] Existe la carpeta `specs/2026-10-20-reingenieria-dashboard/` con `plan.md`, `requirements.md`
  y `validation.md`.
- [ ] El roadmap incluye la **Fase 9** de reingeniería.
- [ ] La frecuencia de cada indicador está **estructurada** (no texto libre).
- [ ] Existe `CalculadorProximaFecha` y devuelve correctamente la próxima fecha (semanal/mensual/
  trimestral).
- [ ] Existe el conector de Forex Factory (API + scraping) que obtiene el "actual" inmediato y el
  histórico de 20 días.
- [ ] El primer refresco persiste los resultados (`actual`) de los últimos 20 días.
- [ ] La API devuelve, por indicador: grupo de impacto, "siguiente fecha" y valor por día.
- [ ] El dashboard agrupa por **Alto/Medio impacto**, muestra **20 días**, la columna "siguiente
  fecha" y el valor por celda (vacía si no hay dato), con fechas en **dd/mm/YYYY**.
- [ ] La Sección 2 muestra NAS100/SP500 × 20 días con la tendencia.
- [ ] `mvn test` y `ng test` pasan.
- [ ] Con backend y frontend arrancados, el dashboard muestra el histórico real.

## Verificación manual (comandos)

```
cd backend-funds && ./mvnw test
cd frontend-funds && ng test --watch=false
# abrir http://localhost:4200 y verificar la vista según el mockup
```

## Criterios para integrar al principal

- Todos los checks anteriores están marcados.
- La vista coincide con `specs/MockupProyectado.xlsx`.
- El scraping de Forex Factory está documentado (endpoint, campos, ToS).
- El commit contiene solo los cambios de esta fase (backend, frontend, specs), sin tocar fases
  anteriores.
