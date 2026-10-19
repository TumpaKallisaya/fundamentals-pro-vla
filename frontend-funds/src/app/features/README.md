# Features

Una carpeta por cada **funcionalidad/dominio** del sistema (arquitectura orientada a
funcionalidades).

Cada feature es autocontenida: componentes, servicios, modelos, rutas y pruebas conviven en su
carpeta. Se registran en `app.routes.ts` (idealmente con lazy loading).

```
features/
└── <feature>/
    ├── <feature>.component.ts
    ├── <feature>.service.ts
    ├── <feature>.model.ts
    ├── <feature>.routes.ts   (opcional)
    └── *.spec.ts
```
