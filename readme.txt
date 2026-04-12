Practica 5 - Docker Compose

Credenciales por defecto:
- Usuario: admin
- Password: se

Arquitectura:
- `haproxy` balancea en round-robin entre `app1`, `app2` y `app3`.
- `redis` almacena las sesiones de Spring Security para que el login sobreviva aunque el siguiente request llegue a otra instancia.
- `db` contiene los datos de la aplicacion.

Pasos para despliegue en AWS (Ubuntu 22.04):
1. Instalar Docker y Docker Compose V2.
2. Ajustar el DNS publico al servidor o usar la IP del host.
3. Abrir el puerto 80 en el Security Group.
4. Ejecutar `docker compose up -d --build`.
5. Verificar estado con `docker compose ps`.

Pruebas:
1. Round-robin: visitar `http://<tu-host>/instance` varias veces y revisar `instanceId`.
2. Sesion compartida: iniciar sesion en `http://<tu-host>/login`, entrar al dashboard y abrir `http://<tu-host>/session-info`.
3. Tolerancia a fallos: detener una instancia con `docker compose stop app2` y repetir `http://<tu-host>/instance`; HAProxy la sacara del pool por health check.
