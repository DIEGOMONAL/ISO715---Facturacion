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

    public List<CondicionPago> listarConFiltros(String buscar, String ordenarPor, String orden) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT id, descripcion, cantidad_dias, estado FROM condiciones_pago WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (buscar != null && !buscar.trim().isEmpty()) {
            String t = buscar.trim();
            try {
                int id = Integer.parseInt(t);
                sql.append(" AND id = ?");
                params.add(id);
            } catch (NumberFormatException e) {
                sql.append(" AND descripcion LIKE ?");
                params.add("%" + t + "%");
            }
        }
        String col = "descripcion";
        if ("id".equalsIgnoreCase(ordenarPor)) col = "id";
        else if ("descripcion".equalsIgnoreCase(ordenarPor)) col = "descripcion";
        else if ("dias".equalsIgnoreCase(ordenarPor) || "cantidad_dias".equalsIgnoreCase(ordenarPor)) col = "cantidad_dias";
        String dir = "desc".equalsIgnoreCase(orden) ? "DESC" : "ASC";
        sql.append(" ORDER BY ").append(col).append(" ").append(dir);
        return listarConQueryParams(sql.toString(), params);
    }

    private List<CondicionPago> listarConQueryParams(String sql, List<Object> params) throws SQLException {
        List<CondicionPago> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
                else ps.setString(i + 1, (String) p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
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
