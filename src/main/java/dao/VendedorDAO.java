package dao;

import model.Vendedor;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO {

    private static final String INSERT_SQL = "INSERT INTO vendedores (nombre, porciento_comision, estado) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, nombre, porciento_comision, estado FROM vendedores WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, nombre, porciento_comision, estado FROM vendedores ORDER BY nombre";
    private static final String SELECT_ACTIVOS_SQL = "SELECT id, nombre, porciento_comision, estado FROM vendedores WHERE estado = 'ACTIVO' ORDER BY nombre";
    private static final String UPDATE_SQL = "UPDATE vendedores SET nombre = ?, porciento_comision = ?, estado = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM vendedores WHERE id = ?";

    public void insertar(Vendedor v) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setString(1, v.getNombre());
            ps.setDouble(2, v.getPorcientoComision());
            ps.setString(3, v.getEstado() != null ? v.getEstado() : "ACTIVO");
            ps.executeUpdate();
        }
    }

    public Vendedor obtenerPorId(int id) throws SQLException {
        Vendedor v = null;
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    v = mapear(rs);
                }
            }
        }
        return v;
    }

    public List<Vendedor> listarTodos() throws SQLException {
        return listarConQuery(SELECT_ALL_SQL);
    }

    public List<Vendedor> listarActivos() throws SQLException {
        return listarConQuery(SELECT_ACTIVOS_SQL);
    }

    public List<Vendedor> listarConFiltros(String buscar, String ordenarPor, String orden) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT id, nombre, porciento_comision, estado FROM vendedores WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (buscar != null && !buscar.trim().isEmpty()) {
            String t = buscar.trim();
            try {
                int id = Integer.parseInt(t);
                sql.append(" AND id = ?");
                params.add(id);
            } catch (NumberFormatException e) {
                sql.append(" AND nombre LIKE ?");
                params.add("%" + t + "%");
            }
        }
        String col = "nombre";
        if ("id".equalsIgnoreCase(ordenarPor)) col = "id";
        else if ("nombre".equalsIgnoreCase(ordenarPor)) col = "nombre";
        else if ("comision".equalsIgnoreCase(ordenarPor) || "porciento_comision".equalsIgnoreCase(ordenarPor)) col = "porciento_comision";
        String dir = "desc".equalsIgnoreCase(orden) ? "DESC" : "ASC";
        sql.append(" ORDER BY ").append(col).append(" ").append(dir);
        return listarConQueryParams(sql.toString(), params);
    }

    private List<Vendedor> listarConQueryParams(String sql, List<Object> params) throws SQLException {
        List<Vendedor> lista = new ArrayList<>();
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

    private List<Vendedor> listarConQuery(String sql) throws SQLException {
        List<Vendedor> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void actualizar(Vendedor v) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, v.getNombre());
            ps.setDouble(2, v.getPorcientoComision());
            ps.setString(3, v.getEstado() != null ? v.getEstado() : "ACTIVO");
            ps.setInt(4, v.getId());
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

    private Vendedor mapear(ResultSet rs) throws SQLException {
        return new Vendedor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDouble("porciento_comision"),
                rs.getString("estado")
        );
    }
}
