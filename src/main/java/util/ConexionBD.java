package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utilidad centralizada para la conexión a la base de datos MySQL.
 */
public class ConexionBD {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/facturacion?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Inicializa la base de datos y todas las tablas del sistema de facturación.
     */
    public static void initDatabase() throws SQLException {
        String urlSinBD = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try (Connection con = DriverManager.getConnection(urlSinBD, JDBC_USER, JDBC_PASSWORD);
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS facturacion " +
                    "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        }

        try (Connection con = getConnection(); Statement st = con.createStatement()) {

            // Tabla articulos
            st.executeUpdate("CREATE TABLE IF NOT EXISTS articulos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "descripcion VARCHAR(255) NOT NULL, " +
                    "precio_unitario DECIMAL(12,2) NOT NULL, " +
                    "estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'" +
                    ")");

            // Tabla clientes
            st.executeUpdate("CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre_comercial VARCHAR(255) NOT NULL, " +
                    "rnc_cedula VARCHAR(20) NOT NULL, " +
                    "cuenta_contable VARCHAR(50), " +
                    "estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'" +
                    ")");

            // Tabla condiciones_pago
            st.executeUpdate("CREATE TABLE IF NOT EXISTS condiciones_pago (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "descripcion VARCHAR(100) NOT NULL, " +
                    "cantidad_dias INT NOT NULL DEFAULT 0, " +
                    "estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'" +
                    ")");

            // Tabla vendedores
            st.executeUpdate("CREATE TABLE IF NOT EXISTS vendedores (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(150) NOT NULL, " +
                    "porciento_comision DECIMAL(5,2) NOT NULL DEFAULT 0, " +
                    "estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'" +
                    ")");

            // Tabla usuarios (rol, estado PENDING/ACTIVO)
            st.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "usuario VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "nombre_completo VARCHAR(150), " +
                    "rol VARCHAR(20) NOT NULL DEFAULT 'CAJERO', " +
                    "estado VARCHAR(20) NOT NULL DEFAULT 'PENDING', " +
                    "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "aprobado_por INT NULL" +
                    ")");
            // Migrar tabla usuarios existente (añadir columnas si faltan)
            try { st.executeUpdate("ALTER TABLE usuarios ADD COLUMN rol VARCHAR(20) DEFAULT 'CAJERO'"); } catch (SQLException ignored) {}
            try { st.executeUpdate("ALTER TABLE usuarios ADD COLUMN nombre_completo VARCHAR(150)"); } catch (SQLException ignored) {}
            try { st.executeUpdate("ALTER TABLE usuarios ADD COLUMN fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP"); } catch (SQLException ignored) {}
            try { st.executeUpdate("ALTER TABLE usuarios ADD COLUMN aprobado_por INT"); } catch (SQLException ignored) {}
            try { st.executeUpdate("UPDATE usuarios SET rol='ADMIN', estado='ACTIVO' WHERE usuario='admin'"); } catch (SQLException ignored) {}

            // Tabla empresa (configuración)
            st.executeUpdate("CREATE TABLE IF NOT EXISTS empresa (" +
                    "id INT PRIMARY KEY DEFAULT 1, " +
                    "rnc VARCHAR(20), " +
                    "nombre VARCHAR(255), " +
                    "logo_path VARCHAR(255), " +
                    "direccion VARCHAR(255), " +
                    "telefono VARCHAR(50)" +
                    ")");
            try { st.executeUpdate("INSERT INTO empresa (id) VALUES (1) ON DUPLICATE KEY UPDATE id=1"); } catch (SQLException ignored) {}

            // Tabla impuestos (ITBIS)
            st.executeUpdate("CREATE TABLE IF NOT EXISTS impuestos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "porcentaje DECIMAL(5,2) NOT NULL DEFAULT 0" +
                    ")");
            try { st.executeUpdate("INSERT INTO impuestos (nombre, porcentaje) SELECT 'ITBIS', 18 " +
                    "WHERE NOT EXISTS (SELECT 1 FROM impuestos WHERE nombre='ITBIS')"); } catch (SQLException ignored) {}

            // Tabla auditoría
            st.executeUpdate("CREATE TABLE IF NOT EXISTS auditoria (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "usuario_id INT, " +
                    "usuario_nombre VARCHAR(50), " +
                    "accion VARCHAR(20) NOT NULL, " +
                    "tabla_afectada VARCHAR(50), " +
                    "registro_id INT, " +
                    "detalle TEXT, " +
                    "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Eliminar tablas de facturación si existen (para migración a nueva estructura)
            st.executeUpdate("DROP TABLE IF EXISTS factura_detalle");
            st.executeUpdate("DROP TABLE IF EXISTS facturas");

            // Tabla facturas (con FKs a cliente, condición de pago, vendedor)
            st.executeUpdate("CREATE TABLE facturas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "cliente_id INT NOT NULL, " +
                    "condicion_pago_id INT NOT NULL, " +
                    "vendedor_id INT NOT NULL, " +
                    "fecha DATE NOT NULL, " +
                    "hora TIME NULL, " +
                    "total DECIMAL(12,2) NOT NULL, " +
                    "FOREIGN KEY (cliente_id) REFERENCES clientes(id), " +
                    "FOREIGN KEY (condicion_pago_id) REFERENCES condiciones_pago(id), " +
                    "FOREIGN KEY (vendedor_id) REFERENCES vendedores(id)" +
                    ")");

            // Tabla factura_detalle (líneas de factura)
            st.executeUpdate("CREATE TABLE IF NOT EXISTS factura_detalle (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "factura_id INT NOT NULL, " +
                    "articulo_id INT NOT NULL, " +
                    "cantidad INT NOT NULL, " +
                    "precio_unitario DECIMAL(12,2) NOT NULL, " +
                    "subtotal DECIMAL(12,2) NOT NULL, " +
                    "FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (articulo_id) REFERENCES articulos(id)" +
                    ")");

            // Insertar admin por defecto si no existe
            try {
                st.executeUpdate("INSERT INTO usuarios (usuario, password, rol, estado) SELECT 'admin', 'admin', 'ADMIN', 'ACTIVO' " +
                        "WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'admin')");
            } catch (SQLException ignored) {}

            // Insertar condiciones de pago por defecto
            try {
                st.executeUpdate("INSERT INTO condiciones_pago (descripcion, cantidad_dias) SELECT 'Al contado', 0 " +
                        "WHERE NOT EXISTS (SELECT 1 FROM condiciones_pago WHERE descripcion = 'Al contado')");
                st.executeUpdate("INSERT INTO condiciones_pago (descripcion, cantidad_dias) SELECT 'A crédito 30 días', 30 " +
                        "WHERE NOT EXISTS (SELECT 1 FROM condiciones_pago WHERE descripcion = 'A crédito 30 días')");
                st.executeUpdate("INSERT INTO condiciones_pago (descripcion, cantidad_dias) SELECT 'Cheque', 0 " +
                        "WHERE NOT EXISTS (SELECT 1 FROM condiciones_pago WHERE descripcion = 'Cheque')");
            } catch (SQLException ignored) {
            }
        }
    }
}
