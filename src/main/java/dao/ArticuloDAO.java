package dao;

import model.Articulo;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAO {

    private static final String INSERT_SQL = "INSERT INTO articulos (descripcion, precio_unitario, estado) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, descripcion, precio_unitario, estado FROM articulos WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, descripcion, precio_unitario, estado FROM articulos ORDER BY descripcion";
    private static final String SELECT_ACTIVOS_SQL = "SELECT id, descripcion, precio_unitario, estado FROM articulos WHERE estado = 'ACTIVO' ORDER BY descripcion";
    private static final String UPDATE_SQL = "UPDATE articulos SET descripcion = ?, precio_unitario = ?, estado = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM articulos WHERE id = ?";

    public void insertar(Articulo a) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setString(1, a.getDescripcion());
            ps.setDouble(2, a.getPrecioUnitario());
            ps.setString(3, a.getEstado() != null ? a.getEstado() : "ACTIVO");
            ps.executeUpdate();
        }
    }

    public Articulo obtenerPorId(int id) throws SQLException {
        Articulo a = null;
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    a = mapear(rs);
                }
            }
        }
        return a;
    }

    public List<Articulo> listarTodos() throws SQLException {
        return listarConQuery(SELECT_ALL_SQL);
    }

    public List<Articulo> listarActivos() throws SQLException {
        return listarConQuery(SELECT_ACTIVOS_SQL);
    }

    /**
     * Lista con búsqueda y ordenamiento.
     * @param buscar término de búsqueda (ID o descripción), puede ser null/vacío
     * @param ordenarPor id, descripcion, precio_unitario
     * @param orden asc o desc
     */
    public List<Articulo> listarConFiltros(String buscar, String ordenarPor, String orden) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT id, descripcion, precio_unitario, estado FROM articulos WHERE 1=1");
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
        else if ("precio".equalsIgnoreCase(ordenarPor) || "precio_unitario".equalsIgnoreCase(ordenarPor)) col = "precio_unitario";
        else if ("descripcion".equalsIgnoreCase(ordenarPor)) col = "descripcion";
        String dir = "desc".equalsIgnoreCase(orden) ? "DESC" : "ASC";
        sql.append(" ORDER BY ").append(col).append(" ").append(dir);

        return listarConQueryParams(sql.toString(), params);
    }

    private List<Articulo> listarConQueryParams(String sql, List<Object> params) throws SQLException {
        List<Articulo> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
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

    private List<Articulo> listarConQuery(String sql) throws SQLException {
        List<Articulo> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void actualizar(Articulo a) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, a.getDescripcion());
            ps.setDouble(2, a.getPrecioUnitario());
            ps.setString(3, a.getEstado() != null ? a.getEstado() : "ACTIVO");
            ps.setInt(4, a.getId());
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

    private Articulo mapear(ResultSet rs) throws SQLException {
        return new Articulo(
                rs.getInt("id"),
                rs.getString("descripcion"),
                rs.getDouble("precio_unitario"),
                rs.getString("estado")
        );
    }
}
