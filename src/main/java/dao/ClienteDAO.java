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

    public List<Cliente> listarConFiltros(String buscar, String ordenarPor, String orden) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT id, nombre_comercial, rnc_cedula, cuenta_contable, estado FROM clientes WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (buscar != null && !buscar.trim().isEmpty()) {
            String t = buscar.trim();
            try {
                int id = Integer.parseInt(t);
                sql.append(" AND id = ?");
                params.add(id);
            } catch (NumberFormatException e) {
                sql.append(" AND nombre_comercial LIKE ?");
                params.add("%" + t + "%");
            }
        }
        String col = "nombre_comercial";
        if ("id".equalsIgnoreCase(ordenarPor)) col = "id";
        else if ("nombre".equalsIgnoreCase(ordenarPor) || "nombre_comercial".equalsIgnoreCase(ordenarPor)) col = "nombre_comercial";
        else if ("rnc".equalsIgnoreCase(ordenarPor) || "rnc_cedula".equalsIgnoreCase(ordenarPor)) col = "rnc_cedula";
        String dir = "desc".equalsIgnoreCase(orden) ? "DESC" : "ASC";
        sql.append(" ORDER BY ").append(col).append(" ").append(dir);
        return listarConQueryParams(sql.toString(), params);
    }

    private List<Cliente> listarConQueryParams(String sql, List<Object> params) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
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
