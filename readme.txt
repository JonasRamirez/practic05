Practica 5 - Docker Compose

Credenciales por defecto:
- Usuario: admin
- Password: se

Pasos para despliegue en AWS (Ubuntu 22.04 t2.small):
1. Instalar Docker y Docker Compose V2.
2. Generar el certificado Let’s Encrypt para el host (A record) y crear un archivo `certs/domain.pem` que concatene `fullchain.pem` + `privkey.pem` en ese orden.
3. Ajustar el DNS público al servidor (puerto 80 y 443 abiertos).
4. Ejecutar `docker compose build` y luego `docker compose up -d`.
5. Verificar balanceo en `https://<tu-dominio>/instance`; el encabezado `X-Instance-Id` o el pie de página de login muestran la instancia.
