# Sistema de Facturación (JSP + Servlets)

Este proyecto es un ejemplo sencillo de CRUD de **facturas** utilizando:

- JSP
- Servlets
- JDBC (MySQL)
- Maven

## Estructura principal

- `pom.xml`: configuración Maven y dependencias.
- `src/main/java`:
  - `model/Factura.java`
  - `dao/FacturaDAO.java`
  - `servlet/FacturaServlet.java`
- `src/main/webapp`:
  - `index.jsp`
  - `facturas/listaFacturas.jsp`
  - `facturas/formFactura.jsp`
  - `WEB-INF/web.xml`

## Base de datos

Ejecuta en MySQL:

```sql
CREATE DATABASE facturacion CHARACTER SET utf8mb4;

USE facturacion;

CREATE TABLE facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente VARCHAR(100) NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL
);
```

Configura usuario y contraseña en la clase `FacturaDAO`.

## Ejecución

1. Ejecuta `mvn clean package`.
2. Despliega el `.war` generado en tu servidor (Tomcat u otro).
3. Accede a `http://localhost:8080/FacturacionWeb` (ajusta el contexto según tu servidor).

