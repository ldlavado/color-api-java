# Color API Java

Generador de colores del módulo de lenguajes modernos, con la página del tutorial y persistencia en PostgreSQL.

La guía del curso devuelve el color en memoria. Esta versión conserva `GET /`, `GET /color` y `POST /saludar`, y guarda cada color y cada saludo. Origen académico: `eshernan/informatica-i-arquitectura-ia`, carpeta API Java. El ZIP del curso no se versiona.

## Endpoints

| Método | Ruta | Qué hace |
| --- | --- | --- |
| GET | `/` | Página del generador y últimos saludos |
| GET | `/color` | Color aleatorio en JSON y lo guarda |
| POST | `/saludar` | `{"nombre":"Ana"}` guarda saludo y color |
| GET | `/saludos` | Últimos 20 saludos |
| GET | `/colores` | Últimos 20 colores |
| GET | `/actuator/health` | Salud del proceso y de la base |

## Correrlo en local

Hace falta Docker.

```bash
docker compose up --build
```

Abre http://localhost:8080. Postgres queda en el volumen `colorapi-data`, así que un reinicio no borra los saludos.

Para pararlo:

```bash
docker compose down
```

`docker compose down -v` borra también los datos.

## Variables

| Variable | Default | Para qué |
| --- | --- | --- |
| `PORT` | `8080` | Puerto HTTP. El proceso no abre TLS. |
| `DATABASE_URL` | `postgres://colorapi:colorapi@localhost:5432/colorapi` | Conexión. Acepta `postgres://` o `postgresql://` y la convierte a JDBC. |

Ejemplo:

```text
postgres://usuario:clave@host:5432/colorapi
```

Si la URL trae `?sslmode=require`, se conserva.

## Llevarlo a otro lado

La imagen es un JAR de Spring Boot sobre Java 21. Sirve en cualquier sitio que construya el `Dockerfile` y le pase una base Postgres.

```bash
docker build -t color-api-java .
docker run --rm -p 8080:8080 \
  -e PORT=8080 \
  -e DATABASE_URL=postgres://usuario:clave@host:5432/colorapi \
  color-api-java
```

La base tiene que existir antes de arrancar. Hibernate crea las tablas (`ddl-auto=update`).

Para comprobar la persistencia: manda un saludo, reinicia el proceso y vuelve a abrir `/`. El saludo tiene que seguir ahí.
