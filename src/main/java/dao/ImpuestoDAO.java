package dao;

import util.ConexionBD;

import java.sql.*;

public class ImpuestoDAO {

    public double getPorcentajeITBIS() throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT porcentaje FROM impuestos WHERE nombre = 'ITBIS' LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("porcentaje");
        }
        return 18;
    }

    public void setPorcentajeITBIS(double porcentaje) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE impuestos SET porcentaje = ? WHERE nombre = 'ITBIS'")) {
            ps.setDouble(1, porcentaje);
            if (ps.executeUpdate() == 0) {
                try (PreparedStatement ins = con.prepareStatement("INSERT INTO impuestos (nombre, porcentaje) VALUES ('ITBIS', ?)")) {
                    ins.setDouble(1, porcentaje);
                    ins.executeUpdate();
                }
            }
        }
    }
}
