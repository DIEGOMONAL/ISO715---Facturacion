package dao;

import model.Empresa;
import util.ConexionBD;

import java.sql.*;

public class EmpresaDAO {

    public Empresa obtener() throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, rnc, nombre, logo_path, direccion, telefono FROM empresa WHERE id = 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Empresa e = new Empresa();
                e.setId(rs.getInt("id"));
                e.setRnc(rs.getString("rnc"));
                e.setNombre(rs.getString("nombre"));
                e.setLogoPath(rs.getString("logo_path"));
                e.setDireccion(rs.getString("direccion"));
                e.setTelefono(rs.getString("telefono"));
                return e;
            }
        }
        return new Empresa();
    }

    public void guardar(Empresa e) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE empresa SET rnc = ?, nombre = ?, direccion = ?, telefono = ? WHERE id = 1")) {
            ps.setString(1, e.getRnc());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getDireccion());
            ps.setString(4, e.getTelefono());
            if (ps.executeUpdate() == 0) {
                try (PreparedStatement ins = con.prepareStatement("INSERT INTO empresa (id, rnc, nombre, direccion, telefono) VALUES (1, ?, ?, ?, ?)")) {
                    ins.setString(1, e.getRnc());
                    ins.setString(2, e.getNombre());
                    ins.setString(3, e.getDireccion());
                    ins.setString(4, e.getTelefono());
                    ins.executeUpdate();
                }
            }
        }
    }
}
