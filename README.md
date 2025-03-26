# -oauth2-authentication-system
# 🔐 **Gestión de Usuarios y Roles con Spring Boot**

Este proyecto implementa un sistema de gestión de **usuarios y roles** utilizando **microservicios** con **Spring Boot**. La solución utiliza **OAuth 2.1** para autenticación, **Feign Client** para la comunicación entre microservicios y **Spring Security** para garantizar seguridad en todo el sistema.

---

## 🌍 **Características del Proyecto**

- **Microservicios Independientes**: Gestión de usuarios y roles como microservicios separados.
- **Bases de Datos Separadas**: Cada microservicio tiene su propia base de datos para una gestión eficiente y escalable.
- **Autenticación Segura**: Implementación de **OAuth 2.1** y **JWT** para proteger los endpoints de los microservicios.
- **Comunicación Entre Microservicios**: Utilización de **Feign Client** para la integración entre el microservicio de **usuarios** y el de **roles**.
- **Roles**: Los usuarios pueden tener múltiples roles asociados (e.g. `ROLE_ADMIN`, `ROLE_USER`).

---

## 💡 **Tecnologías Utilizadas**

- **Spring Boot**: Framework principal para el desarrollo de los microservicios.
- **Spring Security**: Autenticación y autorización de usuarios.
- **OAuth 2.1**: Implementación de autorización y autenticación.
- **Feign Client**: Comunicación entre microservicios.
- **JWT**: Tokens para la autenticación basada en JSON.
- **MySQL**: Base de datos para cada microservicio.

---

## 🚀 **Cómo Ejecutarlo**

1. **Clona el repositorio**:

    ```bash
    git clone https://github.com/tuusuario/nombre-del-repositorio.git
    cd nombre-del-repositorio
    ```

2. **Configura las bases de datos** para cada microservicio en MySQL:
    - Un microservicio para **usuarios**.
    - Un microservicio para **roles**.

3. **Compila y ejecuta los microservicios**:

    Para el **microservicio de usuarios**:

    ```bash
    ./mvnw spring-boot:run
    ```

    Para el **microservicio de roles**:

    ```bash
    ./mvnw spring-boot:run
    ```

4. **Accede a los endpoints**:

    - **Usuarios**: `http://localhost:8002/api/users`
    - **Roles**: `http://localhost:8081/api/roles`

---

## 🛠️ **Comandos Básicos**

Si necesitas configurar los remotos de Git, utiliza estos comandos:

```bash
git remote add origin https://github.com/tuusuario/nombre-del-repositorio.git
git push -u origin main
```
📚 Endpoints
Microservicio de Usuarios
POST /api/users: Crear un nuevo usuario.

GET /api/users: Obtener todos los usuarios.

GET /api/users/{id}: Obtener un usuario por ID.

POST /api/users/{userId}/roles: Asignar un rol a un usuario.

Microservicio de Roles
GET /api/roles/{name}: Obtener un rol por nombre.

⚙️ Configuración de Seguridad
OAuth 2.1 y JWT se utilizan para proteger los endpoints.

El flujo de autenticación y autorización se gestiona de forma centralizada para ambos microservicios.

💬 Contribuir
¡Si tienes ideas o mejoras para este proyecto, siéntete libre de abrir un pull request o issue! Cualquier contribución será bienvenida. 🚀
