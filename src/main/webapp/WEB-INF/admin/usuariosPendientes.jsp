<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Usuario" %>
<%@ page import="util.Roles" %>
<%
    List<Usuario> pendientes = (List<Usuario>) request.getAttribute("pendientes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Usuarios pendientes</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="admin"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div class="page-header mb-0">
                <h1>Usuarios pendientes</h1>
                <p class="subtitle mb-0">Aprobar registros de nuevos usuarios</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-secondary">Volver</a>
        </div>
        <div class="card card-custom">
            <div class="card-body">
                <% if (pendientes != null && !pendientes.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-custom">
                        <thead>
                        <tr>
                            <th>Usuario</th>
                            <th>Nombre</th>
                            <th>Rol solicitado</th>
                            <th>Fecha registro</th>
                            <th class="text-end">Acción</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Usuario u : pendientes) { %>
                        <tr>
                            <td><%= u.getUsuario() %></td>
                            <td><%= u.getNombreCompleto() != null ? u.getNombreCompleto() : "-" %></td>
                            <td><%= Roles.getLabel(u.getRol()) %></td>
                            <td><%= u.getFechaRegistro() != null ? u.getFechaRegistro() : "-" %></td>
                            <td class="text-end">
                                <form method="post" action="${pageContext.request.contextPath}/admin/aprobar" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= u.getId() %>">
                                    <button type="submit" class="btn btn-sm btn-primary">Aprobar</button>
                                </form>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">✓</div>
                    <p class="mb-0">No hay usuarios pendientes de aprobación.</p>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
