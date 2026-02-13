package dao;

import model.Cliente;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String INSERT_SQL = "INSERT INTO clientes (nombre_comercial, rnc_cedula, cuenta_contable, estado) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, nombre_comercial, rnc_cedula, cuenta_contable, estado FROM clientes WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, nombre_comercial, rnc_cedula, cuenta_contable, estado FROM clientes ORDER BY nombre_comercial";
    private static final String SELECT_ACTIVOS_SQL = "SELECT id, nombre_comercial, rnc_cedula, cuenta_contable, estado FROM clientes WHERE estado = 'ACTIVO' ORDER BY nombre_comercial";
    private static final String UPDATE_SQL = "UPDATE clientes SET nombre_comercial = ?, rnc_cedula = ?, cuenta_contable = ?, estado = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM clientes WHERE id = ?";

    public void insertar(Cliente c) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setString(1, c.getNombreComercial());
            ps.setString(2, c.getRncCedula());
            ps.setString(3, c.getCuentaContable());
            ps.setString(4, c.getEstado() != null ? c.getEstado() : "ACTIVO");
            ps.executeUpdate();
        }
    }

    public Cliente obtenerPorId(int id) throws SQLException {
        Cliente c = null;
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = mapear(rs);
                }
            }
        }
        return c;
    }

    public List<Cliente> listarTodos() throws SQLException {
        return listarConQuery(SELECT_ALL_SQL);
    }

    public List<Cliente> listarActivos() throws SQLException {
        return listarConQuery(SELECT_ACTIVOS_SQL);
    }

    private List<Cliente> listarConQuery(String sql) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void actualizar(Cliente c) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, c.getNombreComercial());
            ps.setString(2, c.getRncCedula());
            ps.setString(3, c.getCuentaContable());
            ps.setString(4, c.getEstado() != null ? c.getEstado() : "ACTIVO");
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id"),
                rs.getString("nombre_comercial"),
                rs.getString("rnc_cedula"),
                rs.getString("cuenta_contable"),
                rs.getString("estado")
        );
    }
}
