# Core

Capa de servicios **singleton** y transversales de la aplicación.

Aquí van:

- Servicios globales (auth, configuración, estado compartido).
- Interceptores HTTP (autenticación, manejo de errores, headers).
- Guards de rutas.

No debe contener componentes de UI ni lógica específica de una feature.
