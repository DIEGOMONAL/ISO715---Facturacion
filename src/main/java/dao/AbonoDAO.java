package dao;

import model.Abono;
import util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AbonoDAO {

    private static final String INSERT_SQL =
            "INSERT INTO abonos_cliente (cliente_id, factura_id, monto, observacion) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_CLIENTE_SQL =
            "SELECT a.id, a.cliente_id, a.factura_id, a.monto, a.observacion, a.fecha_registro, " +
            "c.nombre_comercial AS cliente_nombre, " +
            "CONCAT('Factura #', IFNULL(CAST(f.id AS CHAR), 'N/A')) AS referencia_factura " +
            "FROM abonos_cliente a " +
            "JOIN clientes c ON c.id = a.cliente_id " +
            "LEFT JOIN facturas f ON f.id = a.factura_id " +
            "WHERE a.cliente_id = ? " +
            "ORDER BY a.fecha_registro DESC, a.id DESC";

    public void insertar(Abono abono) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, abono.getClienteId());
            if (abono.getFacturaId() != null && abono.getFacturaId() > 0) {
                ps.setInt(2, abono.getFacturaId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setDouble(3, abono.getMonto());
            ps.setString(4, abono.getObservacion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    abono.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Abono> listarPorCliente(int clienteId) throws SQLException {
        List<Abono> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_CLIENTE_SQL)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Abono a = new Abono();
                    a.setId(rs.getInt("id"));
                    a.setClienteId(rs.getInt("cliente_id"));
                    int facturaId = rs.getInt("factura_id");
                    a.setFacturaId(rs.wasNull() ? null : facturaId);
                    a.setMonto(rs.getDouble("monto"));
                    a.setObservacion(rs.getString("observacion"));
                    a.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    a.setClienteNombre(rs.getString("cliente_nombre"));
                    a.setReferenciaFactura(rs.getString("referencia_factura"));
                    lista.add(a);
                }
            }
        }
        return lista;
    }
}
