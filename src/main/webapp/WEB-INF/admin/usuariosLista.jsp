<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Usuario" %>
<%@ page import="util.Roles" %>
<%
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Gestión de usuarios</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="admin"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div class="page-header mb-0">
                <h1>Usuarios</h1>
                <p class="subtitle mb-0">Todos los usuarios del sistema</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-secondary">Volver</a>
        </div>
        <div class="card card-custom">
            <div class="card-body">
                <% if (usuarios != null && !usuarios.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-custom">
                        <thead>
                        <tr>
                            <th>Usuario</th>
                            <th>Nombre</th>
                            <th>Rol</th>
                            <th>Estado</th>
                            <th>Registro</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Usuario u : usuarios) { %>
                        <tr>
                            <td><%= u.getUsuario() %></td>
                            <td><%= u.getNombreCompleto() != null ? u.getNombreCompleto() : "-" %></td>
                            <td><%= Roles.getLabel(u.getRol()) %></td>
                            <td><span class="badge badge-status <%= "ACTIVO".equals(u.getEstado()) ? "bg-success" : "PENDING".equals(u.getEstado()) ? "bg-warning text-dark" : "bg-secondary" %>"><%= u.getEstado() %></span></td>
                            <td><%= u.getFechaRegistro() != null ? u.getFechaRegistro().toString().substring(0,16) : "-" %></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <p class="mb-0">No hay usuarios registrados.</p>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
