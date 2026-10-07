# Color API Java

Generador de colores del modulo de lenguajes modernos, pasado a un servicio desplegable.

La guia del curso devuelve el color en memoria y pide HTTPS propio en el puerto 8443. Esta version:

- conserva `GET /`, `GET /color` y `POST /saludar`
- guarda cada color y cada saludo en PostgreSQL
- escucha el puerto que inyecta la plataforma (`PORT`)
- no trae keystore ni certificado autofirmado: en Render el TLS lo pone la plataforma

Origen academico: `eshernan/informatica-i-arquitectura-ia`, carpeta API Java. El ZIP del curso no se versiona.

## Endpoints

| Metodo | Ruta | Que hace |
| --- | --- | --- |
| GET | `/` | Pagina del generador y ultimos saludos |
| GET | `/color` | Color aleatorio en JSON y lo guarda |
| POST | `/saludar` | `{"nombre":"Ana"}` guarda saludo y color |
| GET | `/saludos` | Ultimos 20 saludos |
| GET | `/colores` | Ultimos 20 colores |
| GET | `/actuator/health` | Salud del proceso y de la base |

## Local

Hace falta Docker.

```bash
docker compose up --build
```

Abre http://localhost:8080. La base queda en el volumen `colorapi-data`, asi que un reinicio no borra los saludos.

## Render

1. Entra a Render y elige **New > Blueprint**.
2. Conecta este repositorio. Render lee `render.yaml`.
3. Crea el web service y la base en el plan free, region Ohio.
4. Cuando el deploy termine, abre la URL del servicio y manda un saludo.
5. Reinicia el servicio. El saludo tiene que seguir en la pagina: eso demuestra la persistencia.

Si el plan `free` de Postgres no aparece en la cuenta, crea la base a mano en el plan mas chico y pega su **internal database URL** en la variable `DATABASE_URL` del web service. La app convierte `postgres://` a JDBC al arrancar.

El free tier se duerme tras unos minutos sin trafico. El primer request despues de eso tarda.
