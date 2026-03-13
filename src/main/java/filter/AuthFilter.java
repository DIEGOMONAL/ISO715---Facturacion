package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().replace(request.getContextPath(), "");

        boolean isPublic = path.startsWith("/login") || path.startsWith("/logout")
                || path.startsWith("/registro")
                || path.startsWith("/css/") || path.startsWith("/js/");

        if (!isPublic) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String rol = (String) session.getAttribute("rol");
            boolean esAdmin = "ADMIN".equalsIgnoreCase(rol);
            boolean esAuditor = "AUDITOR".equalsIgnoreCase(rol);
            boolean esSupervisor = "SUPERVISOR".equalsIgnoreCase(rol);

            boolean requiereAdminCatalogos = path.startsWith("/articulos")
                    || path.startsWith("/clientes")
                    || path.startsWith("/condicionesPago")
                    || path.startsWith("/vendedores")
                    || path.startsWith("/usuarios");

            boolean requiereRolReporte = path.startsWith("/reporte");

            if (requiereAdminCatalogos && !esAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para acceder a esta sección.");
                return;
            }
            if (requiereRolReporte && !(esAdmin || esAuditor || esSupervisor)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para acceder a los reportes.");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}
