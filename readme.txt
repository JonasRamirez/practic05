Practica 5 - Docker Compose

Credenciales por defecto:
- Usuario: admin
- Password: se

Pasos para despliegue en AWS (Ubuntu 22.04 t2.small):
1. Instalar Docker y Docker Compose V2.
2. Ajustar el DNS publico al servidor o usar la IP del host (puerto 80 abierto).
3. Ejecutar `docker compose build` y luego `docker compose up -d`.
4. Verificar balanceo en `http://<tu-host>/instance`; el encabezado `X-Instance-Id` o el pie de pagina de login muestran la instancia.
