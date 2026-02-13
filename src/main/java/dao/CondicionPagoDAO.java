package dao;

import model.CondicionPago;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CondicionPagoDAO {

    private static final String INSERT_SQL = "INSERT INTO condiciones_pago (descripcion, cantidad_dias, estado) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, descripcion, cantidad_dias, estado FROM condiciones_pago WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, descripcion, cantidad_dias, estado FROM condiciones_pago ORDER BY descripcion";
    private static final String SELECT_ACTIVOS_SQL = "SELECT id, descripcion, cantidad_dias, estado FROM condiciones_pago WHERE estado = 'ACTIVO' ORDER BY descripcion";
    private static final String UPDATE_SQL = "UPDATE condiciones_pago SET descripcion = ?, cantidad_dias = ?, estado = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM condiciones_pago WHERE id = ?";

    public void insertar(CondicionPago cp) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setString(1, cp.getDescripcion());
            ps.setInt(2, cp.getCantidadDias());
            ps.setString(3, cp.getEstado() != null ? cp.getEstado() : "ACTIVO");
            ps.executeUpdate();
        }
    }

    public CondicionPago obtenerPorId(int id) throws SQLException {
        CondicionPago cp = null;
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cp = mapear(rs);
                }
            }
        }
        return cp;
    }

    public List<CondicionPago> listarTodos() throws SQLException {
        return listarConQuery(SELECT_ALL_SQL);
    }

    public List<CondicionPago> listarActivos() throws SQLException {
        return listarConQuery(SELECT_ACTIVOS_SQL);
    }

    private List<CondicionPago> listarConQuery(String sql) throws SQLException {
        List<CondicionPago> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void actualizar(CondicionPago cp) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, cp.getDescripcion());
            ps.setInt(2, cp.getCantidadDias());
            ps.setString(3, cp.getEstado() != null ? cp.getEstado() : "ACTIVO");
            ps.setInt(4, cp.getId());
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

    private CondicionPago mapear(ResultSet rs) throws SQLException {
        return new CondicionPago(
                rs.getInt("id"),
                rs.getString("descripcion"),
                rs.getInt("cantidad_dias"),
                rs.getString("estado")
        );
    }
}
