<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Gestión de Usuarios</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="usuarios"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="page-header mb-4">
            <h1>Usuarios del Sistema</h1>
            <p class="subtitle">Aprueba nuevos usuarios, cambia su rol o bloquea el acceso.</p>
        </div>
        <div class="card card-custom">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-custom">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Usuario</th>
                            <th>Rol</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                            if (usuarios != null) for (Usuario u : usuarios) {
                        %>
                        <tr>
                            <td><%= u.getId() %></td>
                            <td><%= u.getUsuario() %></td>
                            <td><%= u.getRol() %></td>
                            <td><%= u.getEstado() %></td>
                            <td class="d-flex gap-1">
                                <form action="${pageContext.request.contextPath}/usuarios" method="post" class="d-inline">
                                    <input type="hidden" name="id" value="<%= u.getId() %>">
                                    <input type="hidden" name="action" value="aprobar">
                                    <button type="submit" class="btn btn-sm btn-success" <%=
                                            "ACTIVO".equals(u.getEstado()) ? "disabled" : "" %>>Aprobar</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/usuarios" method="post" class="d-inline">
                                    <input type="hidden" name="id" value="<%= u.getId() %>">
                                    <input type="hidden" name="action" value="bloquear">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" <%=
                                            "INACTIVO".equals(u.getEstado()) ? "disabled" : "" %>>Bloquear</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/usuarios" method="post" class="d-inline">
                                    <input type="hidden" name="id" value="<%= u.getId() %>">
                                    <input type="hidden" name="action" value="rol">
                                    <select name="rol" class="form-select form-select-sm d-inline w-auto">
                                        <option value="USUARIO" <%= "USUARIO".equals(u.getRol()) ? "selected" : "" %>>Usuario</option>
                                        <option value="VENDEDOR" <%= "VENDEDOR".equals(u.getRol()) ? "selected" : "" %>>Vendedor</option>
                                        <option value="AUDITOR" <%= "AUDITOR".equals(u.getRol()) ? "selected" : "" %>>Auditor</option>
                                        <option value="SUPERVISOR" <%= "SUPERVISOR".equals(u.getRol()) ? "selected" : "" %>>Supervisor</option>
                                        <option value="ADMIN" <%= "ADMIN".equals(u.getRol()) ? "selected" : "" %>>Administrador</option>
                                    </select>
                                    <button type="submit" class="btn btn-sm btn-outline-primary">Cambiar rol</button>
                                </form>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

