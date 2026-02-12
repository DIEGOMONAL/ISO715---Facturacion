package dao;

import model.Factura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    // allowPublicKeyRetrieval=true soluciona el error
    // "Public Key Retrieval is not allowed" con MySQL 8
    private String jdbcURL = "jdbc:mysql://localhost:3306/facturacion?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private String jdbcUsername = "root";
    private String jdbcPassword = "1234";

    private static final String INSERT_SQL =
            "INSERT INTO facturas (cliente, fecha, hora, total) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id, cliente, fecha, hora, total FROM facturas WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id, cliente, fecha, hora, total FROM facturas";
    private static final String UPDATE_SQL =
            "UPDATE facturas SET cliente = ?, fecha = ?, hora = ?, total = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM facturas WHERE id = ?";

    public FacturaDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa la base de datos MySQL y la tabla de facturas si no existen.
     * Requiere que el usuario de MySQL tenga permisos para crear BD y tablas.
     */
    private void initDatabase() throws SQLException {
        // 1) Crear la base de datos si no existe
        String urlSinBD = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try (Connection con = DriverManager.getConnection(urlSinBD, jdbcUsername, jdbcPassword);
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS facturacion " +
                    "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        }

        // 2) Crear la tabla facturas si no existe
        try (Connection con = getConnection();
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS facturas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "cliente VARCHAR(100) NOT NULL, " +
                    "fecha DATE NOT NULL, " +
                    "hora TIME NULL, " +
                    "total DECIMAL(10,2) NOT NULL" +
                    ")");

            // Intentar añadir la columna hora si la tabla ya existía sin ella
            try {
                st.executeUpdate("ALTER TABLE facturas ADD COLUMN hora TIME NULL");
            } catch (SQLException ignored) {
                // Si la columna ya existe, ignoramos el error
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public void insertar(Factura f) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setString(1, f.getCliente());
            ps.setDate(2, f.getFecha());

            Time hora = f.getHora();
            if (hora == null) {
                hora = new Time(System.currentTimeMillis());
            }
            ps.setTime(3, hora);

            ps.setDouble(4, f.getTotal());
            ps.executeUpdate();
        }
    }

    public Factura obtenerPorId(int id) throws SQLException {
        Factura f = null;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    f = new Factura(
                            rs.getInt("id"),
                            rs.getString("cliente"),
                            rs.getDate("fecha"),
                            rs.getTime("hora"),
                            rs.getDouble("total")
                    );
                }
            }
        }
        return f;
    }

    public List<Factura> listarTodas() throws SQLException {
        List<Factura> lista = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Factura f = new Factura(
                        rs.getInt("id"),
                        rs.getString("cliente"),
                        rs.getDate("fecha"),
                        rs.getTime("hora"),
                        rs.getDouble("total")
                );
                lista.add(f);
            }
        }
        return lista;
    }

    public void actualizar(Factura f) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, f.getCliente());
            ps.setDate(2, f.getFecha());

            Time hora = f.getHora();
            if (hora == null) {
                hora = new Time(System.currentTimeMillis());
            }
            ps.setTime(3, hora);

            ps.setDouble(4, f.getTotal());
            ps.setInt(5, f.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

