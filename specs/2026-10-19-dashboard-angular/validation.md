# Validación — Fase 6: Frontend (dashboard Angular)

Cómo determinar si la implementación fue exitosa y puede integrarse al código principal.

## Criterios de éxito (Definition of Done)

- [ ] Existe la carpeta `specs/2026-10-19-dashboard-angular/` con `plan.md`, `requirements.md` y
  `validation.md`.
- [ ] `@angular/material` está instalado y el tema configurado.
- [ ] `app.ts` es un shell con `<router-outlet>` y la feature `dashboard` está enrutada en `/`.
- [ ] Existe `features/dashboard/` con componente, servicio y modelos.
- [ ] La **Sección 1** muestra el semáforo mensual (filas = indicadores, columnas = fechas,
  celdas verde/amarillo/rojo/gris) y es filtrable por índice.
- [ ] La **Sección 2** muestra la tendencia diaria por índice (NAS100/SP500 × fechas) con color.
- [ ] El botón **"Actualizar"** dispara `POST /api/refresco` y recarga ambas secciones.
- [ ] Hay indicadores de carga, error y último refresco.
- [ ] El filtro `desde`/`hasta` (por defecto mes en curso en ET) funciona.
- [ ] `ng build` y `ng test` pasan.
- [ ] Con backend y frontend arrancados, el dashboard muestra los datos reales.

## Verificación manual (comandos)

```
# frontend: compilar y probar
cd frontend-funds
npm install            # para @angular/material
ng build
ng test --watch=false

# abrir http://localhost:4200 y verificar el dashboard
```

## Criterios para integrar al principal

- Todos los checks anteriores están marcados.
- La estructura respeta la **Feature-Based** (`features/dashboard/`, shell enrutado).
- El frontend consume la API del backend (Fase 5) sin lógica de negocio duplicada.
- La zona horaria de presentación es America/New_York (ET).
- El commit contiene solo los cambios de esta fase (frontend + specs), sin tocar el backend.
