# BackTutorias

Backend para la gestión de tutorías académicas. Este proyecto expone una API REST desarrollada con **Spring Boot**, orientada a manejar tutores, tutorados, tutorías, inscripciones y control de acceso mediante autenticación con **JWT**.

> Proyecto desarrollado como parte de la formación en Ingeniería en Software y pensado para despliegue en producción.

---

## 📌 Características principales

* Autenticación y autorización con **Spring Security + JWT**
* Gestión de **Tutores** y **Tutorados** mediante herencia
* CRUD de **Tutorías**
* Inscripción de tutorados a tutorías
* Reglas de negocio (permisos, tiempo mínimo, validaciones)
* Envío de correos (recuperación de contraseña)
* Persistencia con **PostgreSQL**
* Desplegado en **Render**

---

## 🛠️ Tecnologías utilizadas

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA (Hibernate)
* PostgreSQL
* JWT (jjwt)
* Maven
* Docker (entorno local)
* Render (producción)

---

## 🧱 Arquitectura del proyecto

El proyecto sigue una arquitectura en capas:

* **Controller**: expone los endpoints REST
* **Service**: contiene la lógica de negocio
* **Repository**: acceso a datos (JPA)
* **Model / Entity**: entidades persistentes
* **Security**: autenticación, JWT y filtros

---

## 🔐 Autenticación

La autenticación se realiza mediante **JWT**:

1. El usuario inicia sesión
2. El backend genera un token JWT
3. El token se envía en cada request mediante el header:

```
Authorization: Bearer <token>
```

Los endpoints protegidos validan el token y el rol del usuario.

---

## 📡 Endpoints principales

### 🔑 Autenticación

| Método | Endpoint                  | Descripción            |
| ------ | ------------------------- | ---------------------- |
| POST   | `/auth/login`             | Iniciar sesión         |
| POST   | `/auth/register/tutor`    | Registrar tutor        |
| POST   | `/auth/register/tutorado` | Registrar tutorado     |
| POST   | `/auth/forgot-password`   | Solicitar recuperación |
| POST   | `/auth/reset-password`    | Cambiar contraseña     |

---

### 👨‍🏫 Tutores

| Método | Endpoint               | Descripción    |
| ------ | ---------------------- | -------------- |
| GET    | `/tutores`             | Listar tutores |
| GET    | `/tutores/{matricula}` | Obtener tutor  |

---

### 🎓 Tutorados

| Método | Endpoint                 | Descripción      |
| ------ | ------------------------ | ---------------- |
| GET    | `/tutorados`             | Listar tutorados |
| GET    | `/tutorados/{matricula}` | Obtener tutorado |

---

### 📚 Tutorías

| Método | Endpoint         | Descripción                |
| ------ | ---------------- | -------------------------- |
| POST   | `/tutorias`      | Crear tutoría (solo tutor) |
| GET    | `/tutorias`      | Listar tutorías            |
| GET    | `/tutorias/{id}` | Obtener tutoría            |
| DELETE | `/tutorias/{id}` | Eliminar tutoría           |

**Reglas de negocio importantes:**

* Solo el tutor creador puede eliminar la tutoría
* No se puede eliminar si faltan menos de 15 minutos
* No se puede eliminar si hay tutorados inscritos

---

### 🧑‍🎓 Inscripción a tutorías

| Método | Endpoint                     | Descripción          |
| ------ | ---------------------------- | -------------------- |
| POST   | `/tutorias/{id}/inscribirse` | Inscribirse          |
| DELETE | `/tutorias/{id}/cancelar`    | Cancelar inscripción |

---

## 🗄️ Base de datos

* Motor: **PostgreSQL**
* Manejo de esquemas mediante **Hibernate**
* Relaciones principales:

  * Usuario (herencia)
  * Tutoría
  * Inscripción Tutoría-Tutorado

> En producción, la base de datos es gestionada por Render.

---

## ⚙️ Variables de entorno (Producción)

Las siguientes variables se configuran en Render:

* `SPRING_DATASOURCE_URL`
* `SPRING_DATASOURCE_USERNAME`
* `SPRING_DATASOURCE_PASSWORD`
* `JWT_SECRET`
* `MAIL_USERNAME`
* `MAIL_PASSWORD`


---

## 🚀 Despliegue

El proyecto está desplegado en **Render**:

* Build: `mvn clean package`
* Start: `java -jar target/tutorias.jar`
* Perfil activo: `prod`

---

## 📄 Estado del proyecto

* Backend funcional y desplegado
* Seguridad implementada
* Base lista para consumo por frontend


## 👨‍💻 Autor

**Luis Ángel**
Estudiante de Ingeniería en Software
Universidad Veracruzana

GitHub: [https://github.com/LuisAngel832](https://github.com/LuisAngel832)

---

## 📜 Licencia

Este proyecto se distribuye con fines académicos y de aprendizaje.
