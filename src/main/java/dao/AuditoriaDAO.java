package dao;

import model.Auditoria;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO {

    public void registrar(int usuarioId, String usuarioNombre, String accion, String tablaAfectada, Integer registroId, String detalle) {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (usuario_id, usuario_nombre, accion, tabla_afectada, registro_id, detalle) VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, usuarioId);
            ps.setString(2, usuarioNombre);
            ps.setString(3, accion);
            ps.setString(4, tablaAfectada);
            ps.setObject(5, registroId);
            ps.setString(6, detalle);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Auditoria> listar(int limite) throws SQLException {
        List<Auditoria> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, usuario_id, usuario_nombre, accion, tabla_afectada, registro_id, detalle, fecha FROM auditoria ORDER BY fecha DESC LIMIT ?")) {
            ps.setInt(1, limite > 0 ? limite : 500);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Auditoria a = new Auditoria();
                    a.setId(rs.getInt("id"));
                    a.setUsuarioId(rs.getObject("usuario_id") != null ? rs.getInt("usuario_id") : null);
                    a.setUsuarioNombre(rs.getString("usuario_nombre"));
                    a.setAccion(rs.getString("accion"));
                    a.setTablaAfectada(rs.getString("tabla_afectada"));
                    a.setRegistroId(rs.getObject("registro_id") != null ? rs.getInt("registro_id") : null);
                    a.setDetalle(rs.getString("detalle"));
                    a.setFecha(rs.getTimestamp("fecha"));
                    lista.add(a);
                }
            }
        }
        return lista;
    }
}
