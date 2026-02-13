package dao;

import model.Factura;
import model.FacturaDetalle;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    private static final String INSERT_SQL = "INSERT INTO facturas (cliente_id, condicion_pago_id, vendedor_id, fecha, hora, total) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_DETALLE_SQL = "INSERT INTO factura_detalle (factura_id, articulo_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT f.id, f.cliente_id, f.condicion_pago_id, f.vendedor_id, f.fecha, f.hora, f.total, " +
                    "c.nombre_comercial AS cliente_nombre, cp.descripcion AS condicion_pago_desc, v.nombre AS vendedor_nombre " +
                    "FROM facturas f " +
                    "LEFT JOIN clientes c ON f.cliente_id = c.id " +
                    "LEFT JOIN condiciones_pago cp ON f.condicion_pago_id = cp.id " +
                    "LEFT JOIN vendedores v ON f.vendedor_id = v.id " +
                    "WHERE f.id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT f.id, f.cliente_id, f.condicion_pago_id, f.vendedor_id, f.fecha, f.hora, f.total, " +
                    "c.nombre_comercial AS cliente_nombre, cp.descripcion AS condicion_pago_desc, v.nombre AS vendedor_nombre " +
                    "FROM facturas f " +
                    "LEFT JOIN clientes c ON f.cliente_id = c.id " +
                    "LEFT JOIN condiciones_pago cp ON f.condicion_pago_id = cp.id " +
                    "LEFT JOIN vendedores v ON f.vendedor_id = v.id " +
                    "ORDER BY f.fecha DESC, f.id DESC";
    private static final String SELECT_DETALLES_SQL =
            "SELECT fd.id, fd.factura_id, fd.articulo_id, a.descripcion AS articulo_desc, fd.cantidad, fd.precio_unitario, fd.subtotal " +
                    "FROM factura_detalle fd LEFT JOIN articulos a ON fd.articulo_id = a.id WHERE fd.factura_id = ?";
    private static final String UPDATE_SQL = "UPDATE facturas SET cliente_id = ?, condicion_pago_id = ?, vendedor_id = ?, fecha = ?, hora = ?, total = ? WHERE id = ?";
    private static final String DELETE_DETALLE_SQL = "DELETE FROM factura_detalle WHERE factura_id = ?";
    private static final String DELETE_SQL = "DELETE FROM facturas WHERE id = ?";

    public FacturaDAO() {
    }

    public void insertar(Factura f) throws SQLException {
        Connection con = null;
        try {
            con = ConexionBD.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, f.getClienteId());
                ps.setInt(2, f.getCondicionPagoId());
                ps.setInt(3, f.getVendedorId());
                ps.setDate(4, f.getFecha());
                Time hora = f.getHora() != null ? f.getHora() : new Time(System.currentTimeMillis());
                ps.setTime(5, hora);
                ps.setDouble(6, f.getTotal());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        f.setId(rs.getInt(1));
                    }
                }
            }

            for (FacturaDetalle d : f.getDetalles()) {
                try (PreparedStatement ps = con.prepareStatement(INSERT_DETALLE_SQL)) {
                    ps.setInt(1, f.getId());
                    ps.setInt(2, d.getArticuloId());
                    ps.setInt(3, d.getCantidad());
                    ps.setDouble(4, d.getPrecioUnitario());
                    ps.setDouble(5, d.getCantidad() * d.getPrecioUnitario());
                    ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public Factura obtenerPorId(int id) throws SQLException {
        Factura f = null;
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    f = mapearFactura(rs);
                    f.setDetalles(obtenerDetalles(con, id));
                }
            }
        }
        return f;
    }

    public List<Factura> listarTodas() throws SQLException {
        List<Factura> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Factura f = mapearFactura(rs);
                f.setDetalles(obtenerDetalles(con, f.getId()));
                lista.add(f);
            }
        }
        return lista;
    }

    /**
     * Consulta facturas por criterios: artículo, vendedor, cliente, rango de fechas.
     */
    public List<Factura> consultarPorCriterios(Integer articuloId, Integer vendedorId, Integer clienteId,
                                               java.util.Date fechaDesde, java.util.Date fechaHasta) throws SQLException {
        String baseSelect = "SELECT f.id, f.cliente_id, f.condicion_pago_id, f.vendedor_id, f.fecha, f.hora, f.total, " +
                "c.nombre_comercial AS cliente_nombre, cp.descripcion AS condicion_pago_desc, v.nombre AS vendedor_nombre " +
                "FROM facturas f " +
                "LEFT JOIN clientes c ON f.cliente_id = c.id " +
                "LEFT JOIN condiciones_pago cp ON f.condicion_pago_id = cp.id " +
                "LEFT JOIN vendedores v ON f.vendedor_id = v.id ";
        String articuloJoin = (articuloId != null && articuloId > 0)
                ? "INNER JOIN factura_detalle fd ON fd.factura_id = f.id " : "";
        StringBuilder where = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (articuloId != null && articuloId > 0) {
            where.append("fd.articulo_id = ? ");
            params.add(articuloId);
        }
        if (vendedorId != null && vendedorId > 0) {
            if (where.length() > 0) where.append("AND ");
            where.append("f.vendedor_id = ? ");
            params.add(vendedorId);
        }
        if (clienteId != null && clienteId > 0) {
            if (where.length() > 0) where.append("AND ");
            where.append("f.cliente_id = ? ");
            params.add(clienteId);
        }
        if (fechaDesde != null) {
            if (where.length() > 0) where.append("AND ");
            where.append("f.fecha >= ? ");
            params.add(new java.sql.Date(fechaDesde.getTime()));
        }
        if (fechaHasta != null) {
            if (where.length() > 0) where.append("AND ");
            where.append("f.fecha <= ? ");
            params.add(new java.sql.Date(fechaHasta.getTime()));
        }

        String sql = baseSelect + articuloJoin;
        if (where.length() > 0) sql += "WHERE " + where.toString();
        sql += " ORDER BY f.fecha DESC, f.id DESC";

        List<Factura> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Factura f = mapearFactura(rs);
                    f.setDetalles(obtenerDetalles(con, f.getId()));
                    lista.add(f);
                }
            }
        }
        return lista;
    }

    /**
     * Reporte de facturación por cliente entre fechas.
     */
    public List<Factura> reportePorClienteEntreFechas(int clienteId, java.util.Date fechaDesde, java.util.Date fechaHasta) throws SQLException {
        String sql = "SELECT f.id, f.cliente_id, f.condicion_pago_id, f.vendedor_id, f.fecha, f.hora, f.total, " +
                "c.nombre_comercial AS cliente_nombre, cp.descripcion AS condicion_pago_desc, v.nombre AS vendedor_nombre " +
                "FROM facturas f " +
                "LEFT JOIN clientes c ON f.cliente_id = c.id " +
                "LEFT JOIN condiciones_pago cp ON f.condicion_pago_id = cp.id " +
                "LEFT JOIN vendedores v ON f.vendedor_id = v.id " +
                "WHERE f.cliente_id = ? AND f.fecha >= ? AND f.fecha <= ? " +
                "ORDER BY f.fecha, f.id";
        List<Factura> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setDate(2, new java.sql.Date(fechaDesde.getTime()));
            ps.setDate(3, new java.sql.Date(fechaHasta.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Factura f = mapearFactura(rs);
                    f.setDetalles(obtenerDetalles(con, f.getId()));
                    lista.add(f);
                }
            }
        }
        return lista;
    }

    private List<FacturaDetalle> obtenerDetalles(Connection con, int facturaId) throws SQLException {
        List<FacturaDetalle> detalles = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SELECT_DETALLES_SQL)) {
            ps.setInt(1, facturaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FacturaDetalle d = new FacturaDetalle();
                    d.setId(rs.getInt("id"));
                    d.setFacturaId(rs.getInt("factura_id"));
                    d.setArticuloId(rs.getInt("articulo_id"));
                    d.setArticuloDescripcion(rs.getString("articulo_desc"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    detalles.add(d);
                }
            }
        }
        return detalles;
    }

    public void actualizar(Factura f) throws SQLException {
        Connection con = null;
        try {
            con = ConexionBD.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {
                ps.setInt(1, f.getClienteId());
                ps.setInt(2, f.getCondicionPagoId());
                ps.setInt(3, f.getVendedorId());
                ps.setDate(4, f.getFecha());
                Time hora = f.getHora() != null ? f.getHora() : new Time(System.currentTimeMillis());
                ps.setTime(5, hora);
                ps.setDouble(6, f.getTotal());
                ps.setInt(7, f.getId());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(DELETE_DETALLE_SQL)) {
                ps.setInt(1, f.getId());
                ps.executeUpdate();
            }

            for (FacturaDetalle d : f.getDetalles()) {
                try (PreparedStatement ps = con.prepareStatement(INSERT_DETALLE_SQL)) {
                    ps.setInt(1, f.getId());
                    ps.setInt(2, d.getArticuloId());
                    ps.setInt(3, d.getCantidad());
                    ps.setDouble(4, d.getPrecioUnitario());
                    ps.setDouble(5, d.getCantidad() * d.getPrecioUnitario());
                    ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Factura mapearFactura(ResultSet rs) throws SQLException {
        Factura f = new Factura();
        f.setId(rs.getInt("id"));
        f.setClienteId(rs.getInt("cliente_id"));
        f.setClienteNombre(rs.getString("cliente_nombre"));
        f.setCondicionPagoId(rs.getInt("condicion_pago_id"));
        f.setCondicionPagoDescripcion(rs.getString("condicion_pago_desc"));
        f.setVendedorId(rs.getInt("vendedor_id"));
        f.setVendedorNombre(rs.getString("vendedor_nombre"));
        f.setFecha(rs.getDate("fecha"));
        f.setHora(rs.getTime("hora"));
        f.setTotal(rs.getDouble("total"));
        return f;
    }
}
