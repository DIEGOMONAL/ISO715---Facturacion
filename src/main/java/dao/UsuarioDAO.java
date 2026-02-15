package dao;

import model.Usuario;
import util.ConexionBD;
import util.Roles;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario autenticar(String usuario, String password) throws SQLException {
        Usuario u = null;
        String sql = "SELECT id, usuario, password, nombre_completo, rol, estado, fecha_registro, aprobado_por FROM usuarios WHERE usuario = ? AND password = ? AND estado = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario != null ? usuario.trim() : "");
            ps.setString(2, password != null ? password : "");
            ps.setString(3, Roles.ACTIVO);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) u = mapear(rs);
            }
        }
        return u;
    }

    /** Verifica si el usuario existe y está pendiente (para mensaje en login) */
    public Usuario buscarPorUsuario(String usuario) throws SQLException {
        Usuario u = null;
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, usuario, rol, estado FROM usuarios WHERE usuario = ?")) {
            ps.setString(1, usuario != null ? usuario.trim() : "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setRol(rs.getString("rol"));
                    u.setEstado(rs.getString("estado"));
                }
            }
        }
        return u;
    }

    public List<Usuario> listarPendientes() throws SQLException {
        return listarPorEstado(Roles.PENDING);
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, usuario, nombre_completo, rol, estado, fecha_registro FROM usuarios ORDER BY fecha_registro DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearLista(rs));
        }
        return lista;
    }

    private List<Usuario> listarPorEstado(String estado) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, usuario, nombre_completo, rol, estado, fecha_registro FROM usuarios WHERE estado = ? ORDER BY fecha_registro")) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearLista(rs));
            }
        }
        return lista;
    }

    public Usuario obtenerPorId(int id) throws SQLException {
        Usuario u = null;
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, usuario, password, nombre_completo, rol, estado, fecha_registro, aprobado_por FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) u = mapear(rs);
            }
        }
        return u;
    }

    public void registrar(Usuario u) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO usuarios (usuario, password, nombre_completo, rol, estado) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getRol() != null ? u.getRol() : Roles.CAJERO);
            ps.setString(5, Roles.PENDING);
            ps.executeUpdate();
        }
    }

    public void aprobar(int usuarioId, int adminId) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET estado = ?, aprobado_por = ? WHERE id = ?")) {
            ps.setString(1, Roles.ACTIVO);
            ps.setInt(2, adminId);
            ps.setInt(3, usuarioId);
            ps.executeUpdate();
        }
    }

    public void actualizar(Usuario u) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET usuario = ?, nombre_completo = ?, rol = ?, estado = ? WHERE id = ?")) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getNombreCompleto());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getEstado());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
        }
    }

    public void actualizarPassword(int id, String password) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET password = ? WHERE id = ?")) {
            ps.setString(1, password);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public boolean existeUsuario(String usuario, Integer excluirId) throws SQLException {
        String sql = excluirId != null
                ? "SELECT 1 FROM usuarios WHERE usuario = ? AND id != ?"
                : "SELECT 1 FROM usuarios WHERE usuario = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            if (excluirId != null) ps.setInt(2, excluirId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setUsuario(rs.getString("usuario"));
        u.setPassword(rs.getString("password"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setRol(rs.getString("rol"));
        u.setEstado(rs.getString("estado"));
        u.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        try { u.setAprobadoPor(rs.getObject("aprobado_por") != null ? rs.getInt("aprobado_por") : null); } catch (SQLException ignored) {}
        return u;
    }

    private Usuario mapearLista(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setUsuario(rs.getString("usuario"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setRol(rs.getString("rol"));
        u.setEstado(rs.getString("estado"));
        u.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return u;
    }
}
