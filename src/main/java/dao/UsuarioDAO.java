package dao;

import model.Usuario;
import util.ConexionBD;

import java.sql.*;

public class UsuarioDAO {
    private static final String SELECT_BY_USER_PASS = "SELECT id, usuario, password, estado FROM usuarios WHERE usuario = ? AND password = ? AND estado = 'ACTIVO'";

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
                    u.setEstado(rs.getString("estado"));
                }
            }
        }
        return u;
    }
}
