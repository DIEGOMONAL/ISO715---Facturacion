package filter;

import util.Roles;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * Filtro que restringe el acceso según el rol del usuario.
 * ADMIN: todo. CAJERO: facturas, artículos (consulta). SUPERVISOR: + anular. INVENTARIO: artículos, clientes. CONTADOR: reportes.
 */
public class RoleFilter implements Filter {

    // Rutas solo ADMIN
    private static final Set<String> ADMIN_ONLY = Set.of("/admin", "/usuarios");

    // Rutas CAJERO: facturas (crear, ver), artículos (listar para facturar), clientes (listar)
    // CAJERO NO: anular factura, editar precios, gestión completa de catálogos

    // Rutas CONTADOR: solo reportes y consultas (lectura)
    private static final Set<String> CONTADOR_READONLY = Set.of("/consulta", "/reporte", "/facturas");
    // CONTADOR puede ver facturas y reportes pero NO crear/editar/eliminar

    // Rutas INVENTARIO: artículos, clientes, condiciones, vendedores (gestión), NO facturas
    private static final Set<String> INVENTARIO_ALLOWED = Set.of("/articulos", "/clientes", "/condicionesPago", "/vendedores", "/");

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().replace(request.getContextPath(), "");

        // Ignorar recursos estáticos y rutas públicas
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/login")
                || path.startsWith("/logout") || path.startsWith("/registro")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        String rol = session != null ? (String) session.getAttribute("rol") : null;

        if (rol == null) {
            chain.doFilter(req, res);
            return;
        }

        // ADMIN tiene acceso total
        if (Roles.ADMIN.equals(rol)) {
            chain.doFilter(req, res);
            return;
        }

        // Rutas solo para ADMIN
        if (pathStartsWith(path, ADMIN_ONLY)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado. Se requiere rol de Administrador.");
            return;
        }

        // CONTADOR: solo lectura en consulta, reporte, ver facturas
        if (Roles.CONTADOR.equals(rol)) {
            if (pathStartsWith(path, Set.of("/articulos", "/clientes", "/condicionesPago", "/vendedores"))) {
                if (isWriteAction(request)) {
                    response.sendRedirect(request.getContextPath() + "/?error=sin_permiso");
                    return;
                }
            }
            if (path.startsWith("/facturas")) {
                if (isWriteAction(request) || "eliminar".equals(request.getParameter("action")) || "nuevo".equals(request.getParameter("action"))) {
                    response.sendRedirect(request.getContextPath() + "/facturas?error=sin_permiso");
                    return;
                }
            }
        }

        // INVENTARIO: no accede a facturas (crear/editar)
        if (Roles.INVENTARIO.equals(rol) && path.startsWith("/facturas")) {
            response.sendRedirect(request.getContextPath() + "/?error=sin_permiso");
            return;
        }
        if (Roles.INVENTARIO.equals(rol) && path.startsWith("/consulta")) {
            response.sendRedirect(request.getContextPath() + "/?error=sin_permiso");
            return;
        }
        if (Roles.INVENTARIO.equals(rol) && path.startsWith("/reporte")) {
            response.sendRedirect(request.getContextPath() + "/?error=sin_permiso");
            return;
        }

        // CAJERO: facturas (crear, ver). NO: anular facturas, modificar precios, gestionar catálogos
        if (Roles.CAJERO.equals(rol)) {
            if (path.startsWith("/admin") || path.startsWith("/usuarios")) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
                return;
            }
            if ("eliminar".equals(request.getParameter("action")) && path.startsWith("/facturas")) {
                response.sendRedirect(request.getContextPath() + "/facturas?error=No puedes anular facturas");
                return;
            }
            // Cajero NO gestiona catálogos (artículos, clientes, condiciones, vendedores)
            if (path.startsWith("/articulos") || path.startsWith("/clientes") || path.startsWith("/condicionesPago") || path.startsWith("/vendedores")) {
                if (isWriteAction(request) || "editar".equals(request.getParameter("action")) || "eliminar".equals(request.getParameter("action")) || "nuevo".equals(request.getParameter("action"))) {
                    response.sendRedirect(request.getContextPath() + "/?error=sin_permiso");
                    return;
                }
            }
        }

        // SUPERVISOR: puede anular facturas, validar cierres. No gestiona usuarios.
        // (Sin restricciones adicionales aquí, tiene más permisos que Cajero)

        chain.doFilter(req, res);
    }

    private boolean pathStartsWith(String path, Set<String> prefixes) {
        for (String p : prefixes) {
            if (path.startsWith(p)) return true;
        }
        return false;
    }

    private boolean isWriteAction(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }

    @Override
    public void destroy() {}
}
