package util;

/**
 * Constantes de roles del sistema.
 * ADMIN, CAJERO, SUPERVISOR, INVENTARIO, CONTADOR
 */
public final class Roles {
    public static final String ADMIN = "ADMIN";
    public static final String CAJERO = "CAJERO";
    public static final String SUPERVISOR = "SUPERVISOR";
    public static final String INVENTARIO = "INVENTARIO";
    public static final String CONTADOR = "CONTADOR";

    public static final String PENDING = "PENDING";
    public static final String ACTIVO = "ACTIVO";
    public static final String INACTIVO = "INACTIVO";

    public static String getLabel(String rol) {
        if (rol == null) return "";
        switch (rol) {
            case ADMIN: return "Administrador";
            case CAJERO: return "Cajero";
            case SUPERVISOR: return "Supervisor de Ventas";
            case INVENTARIO: return "Inventario/Almacén";
            case CONTADOR: return "Contador";
            default: return rol;
        }
    }

    private Roles() {}
}
