# Arquitectura del frontend — Feature-Based (obligatoria)

Este proyecto Angular sigue una **arquitectura orientada a funcionalidades** (Feature-Based
Architecture). El código se organiza por **dominio/funcionalidad**, no por tipo de archivo
(componentes, servicios, etc.).

## Reglas generales

- Cada funcionalidad del sistema (p. ej. `health`, `dashboard`) vive en su propia carpeta bajo
  `src/app/features/<nombre>/`.
- Dentro de cada feature conviven todos sus artefactos: componentes, servicios, modelos, rutas y
  pruebas. No se reparten en carpetas globales por tipo.
- El código transversal y reutilizable va en `core/` y `shared/` (ver abajo).
- Las features no se importan entre sí de forma directa; se comunican vía `core/` (estado/servicios
  globales) o mediante el router (lazy loading).
- Cualquier agente que desarrolle o mantenga el sistema **debe** respetar esta estructura.

## Estructura

```
src/app/
├── app.ts                 # Componente raíz (shell): router-outlet y layout global
├── app.config.ts          # Configuración global de la aplicación (providers)
├── app.routes.ts          # Rutas raíz (lazy loading hacia las features)
├── core/                  # Singleton: servicios globales, interceptores, guards, estado
│   └── services/          #   servicios de aplicación transversales (auth, api, ...)
├── shared/                # Reutilizable entre features: componentes, directivas, pipes, modelos
│   ├── components/
│   ├── directives/
│   ├── pipes/
│   └── models/
└── features/              # Una carpeta por funcionalidad/dominio
    └── <feature>/
        ├── <feature>.component.ts   # componente(s) de la feature
        ├── <feature>.service.ts     # servicio(s) de la feature
        ├── <feature>.model.ts       # modelos/tipos de la feature
        ├── <feature>.routes.ts      # rutas internas de la feature (si aplica)
        └── *.spec.ts                # pruebas de la feature
```

## Convenciones por carpeta

| Carpeta | Propósito | Ejemplos |
|---------|-----------|----------|
| `core/` | Servicios **singleton** y transversales de toda la app | HTTP interceptor, AuthService, estado global |
| `shared/` | Piezas **reutilizables** y sin estado de negocio | botones, tablas, pipes de formato, modelos comunes |
| `features/` | Funcionalidades de negocio autocontenidas | health, dashboard (fases futuras) |

## Referencia

- Stack y arquitectura: `specs/tech-stack.md`.
