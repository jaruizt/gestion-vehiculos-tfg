# Gestión de Vehículos - TFG UOC

> Sistema web para la gestión integral de empresas del sector automovilístico (PyMEs)

## Descripción

Aplicación web que permite gestionar el ciclo de vida completo de vehículos desde su adquisición hasta su venta final:

- Compra de vehículos
- Contratos de alquiler de larga duración
- Seguimiento de cuotas y pagos
- Reservas de venta
- Facturación de compra y venta
- Gestión de clientes

## Contexto Académico

**Trabajo Final de Grado (TFG)**  
**Universidad:** Universitat Oberta de Catalunya (UOC)  
**Grado:** Ingeniera de Telecomucnicaciones 
**Autor:** José Antonio Ruiz Traid  
**Fecha:** 2025

## Tecnologías

### Backend:
- **Framework:** Spring Boot 4.0.0
- **Seguridad:** Spring Security 7 + JWT
- **Base de Datos:** MySQL 8
- **ORM:** Hibernate 7 (JPA)
- **Lenguaje:** Java 17
- **Build:** Maven

### Frontend:
- Angular 18+
- TypeScript
- Bootstrap / Tailwind CSS


## Instalación y Configuración

### Requisitos previos:
```bash
Java 17+
MySQL 8+
Maven 3.8+
```

### 1. Clonar repositorio:
```bash
git clone https://github.com/tu-usuario/gestion-vehiculos-tfg.git
cd gestion-vehiculos-tfg
```

### 2. Configurar base de datos:
```sql
CREATE DATABASE gestion_vehiculos_tfg CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'tfg_user'@'localhost' IDENTIFIED BY 'tfg_1234';
GRANT ALL PRIVILEGES ON gestion_vehiculos_tfg.* TO 'tfg_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_vehiculos_tfg
spring.datasource.username=tfg_user
spring.datasource.password=tfg_1234
```

### 4. Ejecutar aplicación:
```bash
mvn spring-boot:run
```

La aplicación arranca en: **http://localhost:8080/api**

## Testing con Postman

### Endpoints disponibles:

#### Autenticación (público):

**Login:**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin1234"
}
```

**Registro:**
```http
POST http://localhost:8080/api/auth/registro
Content-Type: application/json

{
  "username": "nuevouser",
  "password": "password123",
  "email": "user@example.com",
  "nombre": "Nombre",
  "apellidos": "Apellidos",
  "telefono": "666777888"
}
```

### Usuarios de prueba:

| Username | Password | Rol | Descripción |
|----------|----------|-----|-------------|
| admin | admin1234 | ADMIN | Acceso total al sistema |
| gerente | gerente1234 | GERENTE | Gestión de vehículos, contratos e informes |
| comercial | comercial1234 | COMERCIAL | Gestión de clientes, contratos y ventas |

##  Contribuciones

Este es un proyecto académico (TFG), por lo que no se aceptan contribuciones externas. Sin embargo, cualquier feedback o sugerencia es bienvenida.

## Licencia

© 2025 José Antonio Ruiz Traid - Todos los derechos reservados  
Proyecto académico para la Universitat Oberta de Catalunya (UOC)

---

⭐ Si este proyecto te resulta útil o interesante, ¡no dudes en darle una estrella!