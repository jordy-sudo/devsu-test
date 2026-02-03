# Devsu Banking App

Aplicación bancaria de ejemplo desarrollada con **Spring Boot (Gradle)**, **Angular** y **PostgreSQL**, completamente dockerizada para facilitar su ejecución en cualquier entorno mediante **Docker Compose**.

---

## Tecnologías utilizadas

- Backend: Spring Boot (Gradle)
- Frontend: Angular
- Base de datos: PostgreSQL
- Contenedores: Docker + Docker Compose

---

## Estructura del proyecto

```
TEST/
├─ BackEnd/
│  └─ test/                 # Backend Spring Boot
├─ FrontEnd/
│  └─ devsu-front/          # Frontend Angular
├─ docker/
│  ├─ Dockerfile.backend.dev
│  └─ Dockerfile.front.dev
└─ docker-compose.yml
```

---

## Requisitos

Antes de iniciar, asegúrate de tener instalado:

- Docker Desktop (Windows / macOS)  
  o  
- Docker Engine + Docker Compose (Linux)

> No es necesario instalar Java, Gradle, Node.js ni PostgreSQL localmente.

---

## Ejecución del proyecto

### 1️⃣ Clonar el repositorio

```bash
git clone <https://github.com/jordy-sudo/devsu-test.git>
cd devsu-test
```

---

### 2️⃣ Levantar la aplicación con Docker Compose

```bash
docker compose up --build
```

Este comando levantará automáticamente:

- Base de datos PostgreSQL
- Backend Spring Boot
- Frontend Angular

---

## URLs de acceso

- Frontend (Angular):  
  http://localhost:4200

- Backend (Spring Boot):  
  http://localhost:8080

- Base de datos PostgreSQL:  
  Host: `localhost`  
  Puerto: `5432`  
  Base de datos: `devsu_db`  
  Usuario: `devsu_user`  
  Contraseña: `devsu_pass`

---

## Configuración de Base de Datos

Dentro del entorno Docker, el backend se conecta a PostgreSQL usando el nombre del servicio:

```
jdbc:postgresql://postgres:5432/devsu_db
```

Esta configuración ya está definida en el archivo `docker-compose.yml`, por lo que no requiere cambios manuales.

---

## Migraciones y datos de prueba

El proyecto utiliza migraciones SQL para:

- Crear las tablas principales (`persona`, `cliente`, `cuenta`, `movimiento`)
- Insertar datos de prueba (casos de uso)

Ubicación de las migraciones:

```
BackEnd/test/src/main/resources/db/migration
```

Ejemplo:

- `V1__init.sql`
- `V2__seed_casos_de_uso.sql`

Las migraciones se ejecutan automáticamente al iniciar la aplicación.

---

## Hot Reload (modo desarrollo)

### Frontend
Angular se ejecuta con `ng serve`, permitiendo recarga automática al detectar cambios.

### Backend
Spring Boot se ejecuta con:

```bash
gradle bootRun
```

Se recomienda incluir `spring-boot-devtools` para una mejor experiencia de desarrollo.

---

## 🛠️ Comandos útiles

### Detener los contenedores

```bash
docker compose down
```

### Detener y eliminar volúmenes (reinicia la base de datos)

```bash
docker compose down -v
```

### Ver logs de todos los servicios

```bash
docker compose logs -f
```

### Ver logs por servicio

Backend:
```bash
docker compose logs -f backend
```

Frontend:
```bash
docker compose logs -f front
```

---

## Solución de problemas

### El backend no conecta a la base de datos

Verifica que la URL de conexión sea:

```
jdbc:postgresql://postgres:5432/devsu_db
```

y que Docker esté ejecutándose correctamente.

---

### Angular no refleja cambios (Windows)

Reinicia el contenedor del frontend:

```bash
docker compose restart front
```

---

### Puertos ocupados

Si los puertos `4200`, `8080` o `5432` están ocupados, modifícalos en el archivo `docker-compose.yml`.

---

## Checklist rápido

- [ ] Docker instalado y ejecutándose
- [ ] Ejecutar comandos desde la carpeta `TEST`
- [ ] `docker compose up --build`
- [ ] Acceder a http://localhost:4200

---

## Licencia

Definir la licencia del proyecto (MIT, Apache 2.0, etc.).
