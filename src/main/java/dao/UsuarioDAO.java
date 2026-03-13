package dao;

import model.Usuario;
import util.ConexionBD;

import java.sql.*;

public class UsuarioDAO {
    private static final String SELECT_BY_USER_PASS = "SELECT id, usuario, password, rol, estado FROM usuarios WHERE usuario = ? AND password = ? AND estado = 'ACTIVO'";
    private static final String INSERT_SQL = "INSERT INTO usuarios (usuario, password, rol, estado) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT id, usuario, rol, estado FROM usuarios";
    private static final String UPDATE_ESTADO_SQL = "UPDATE usuarios SET estado = ? WHERE id = ?";
    private static final String UPDATE_ROL_SQL = "UPDATE usuarios SET rol = ? WHERE id = ?";

    public Usuario autenticar(String usuario, String password) throws SQLException {
        Usuario u = null;
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_PASS)) {
            ps.setString(1, usuario);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setPassword(rs.getString("password"));
                    u.setRol(rs.getString("rol"));
                    u.setEstado(rs.getString("estado"));
                }
            }
        }
        return u;
    }

    public void registrarPendiente(String usuario, String password) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setString(1, usuario);
            ps.setString(2, password);
            ps.setString(3, "USUARIO");
            ps.setString(4, "PENDIENTE");
            ps.executeUpdate();
        }
    }

    public ResultSet listarTodos(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
        return ps.executeQuery();
    }

    public void cambiarEstado(int id, String nuevoEstado) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_ESTADO_SQL)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void cambiarRol(int id, String nuevoRol) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_ROL_SQL)) {
            ps.setString(1, nuevoRol);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}
