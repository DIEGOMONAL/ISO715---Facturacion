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

            // Tabla usuarios (para control de acceso)
            st.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "usuario VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'" +
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

            // Insertar usuario por defecto (admin/admin) si no existe
            try {
                st.executeUpdate("INSERT INTO usuarios (usuario, password) SELECT 'admin', 'admin' " +
                        "WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'admin')");
            } catch (SQLException ignored) {
            }

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
