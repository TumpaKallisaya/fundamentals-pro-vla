# Validación — Fase 1: Catálogo de indicadores y fuentes

Cómo determinar si la implementación fue exitosa y puede integrarse al código principal.

## Criterios de éxito (Definition of Done)

- [ ] Existe la carpeta `specs/2026-10-19-catalogo-indicadores-fuentes/` con `plan.md`,
  `requirements.md` y `validation.md`.
- [ ] El catálogo cubre **12 indicadores de impacto alto** y **11 de impacto medio** de EE.UU.
  para NAS100 y SP500, alineado con la sección 5 de `fuentes_trading_scraping_api.md`.
- [ ] Cada indicador define: `id` (único), `nombre`, `alias_ff`, `sector`, `impacto`,
  `frecuencia`, `hora_et`, `indices` (con relevancia), `fuente` y `regla` (dirección, peso,
  tolerancia).
- [ ] Todos los `id` son únicos y usan `snake_case`.
- [ ] La relevancia por índice (`alta`/`media`/`baja`) está definida y justificada para NAS100 y
  SP500.
- [ ] La regla de interpretación (dirección, peso, tolerancia) está propuesta por defecto y
  marcada para revisión del trader.
- [ ] Existe el **documento de catálogo** (Markdown) legible.
- [ ] Existe el **seed JSON** con el catálogo completo, y su estructura es válida (JSON parseable,
  campos obligatorios presentes, valores dentro del conjunto permitido).
- [ ] La fuente documentada es **Forex Factory** (feed JSON) con URL, campos, límites y la
  limitación del valor `actual` anotada.
- [ ] Los valores de `impacto` son `Alto` o `Medio` y los de `relevancia` son
  `alta`/`media`/`baja`.

## Verificación manual (comandos)

```
# validar el JSON del seed (con jq, si está disponible)
jq empty specs/2026-10-19-catalogo-indicadores-fuentes/indicadores.seed.json

# contar indicadores y comprobar ids únicos
jq 'length' specs/2026-10-19-catalogo-indicadores-fuentes/indicadores.seed.json
```

## Criterios para integrar al principal

- Todos los checks anteriores están marcados.
- El catálogo es coherente con `mission.md` (solo EE.UU., índices NAS100/SP500) y `tech-stack.md`
  (el catálogo será configuración/datos, no lógica rígida).
- El seed JSON está listo para que la Fase 2 lo siembre sin reformatear.
- Los valores de regla (dirección/peso/tolerancia) están marcados como propuestos y se ha
  solicitado revisión al trader.
- El commit contiene solo los artefactos de esta fase (specs + catálogo + seed), sin cambios de
  código.
