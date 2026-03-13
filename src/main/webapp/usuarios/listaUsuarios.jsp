<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.ResultSet" %>
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
                            ResultSet rs = (ResultSet) request.getAttribute("usuariosRS");
                            if (rs != null) {
                                while (rs.next()) {
                        %>
                        <tr>
                            <td><%= rs.getInt("id") %></td>
                            <td><%= rs.getString("usuario") %></td>
                            <td><%= rs.getString("rol") %></td>
                            <td><%= rs.getString("estado") %></td>
                            <td class="d-flex gap-1">
                                <form action="${pageContext.request.contextPath}/usuarios" method="post" class="d-inline">
                                    <input type="hidden" name="id" value="<%= rs.getInt("id") %>">
                                    <input type="hidden" name="action" value="aprobar">
                                    <button type="submit" class="btn btn-sm btn-success" <%=
                                            "ACTIVO".equals(rs.getString("estado")) ? "disabled" : "" %>>Aprobar</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/usuarios" method="post" class="d-inline">
                                    <input type="hidden" name="id" value="<%= rs.getInt("id") %>">
                                    <input type="hidden" name="action" value="bloquear">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" <%=
                                            "INACTIVO".equals(rs.getString("estado")) ? "disabled" : "" %>>Bloquear</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/usuarios" method="post" class="d-inline">
                                    <input type="hidden" name="id" value="<%= rs.getInt("id") %>">
                                    <input type="hidden" name="action" value="rol">
                                    <select name="rol" class="form-select form-select-sm d-inline w-auto">
                                        <option value="USUARIO" <%= "USUARIO".equals(rs.getString("rol")) ? "selected" : "" %>>Usuario</option>
                                        <option value="VENDEDOR" <%= "VENDEDOR".equals(rs.getString("rol")) ? "selected" : "" %>>Vendedor</option>
                                        <option value="AUDITOR" <%= "AUDITOR".equals(rs.getString("rol")) ? "selected" : "" %>>Auditor</option>
                                        <option value="SUPERVISOR" <%= "SUPERVISOR".equals(rs.getString("rol")) ? "selected" : "" %>>Supervisor</option>
                                        <option value="ADMIN" <%= "ADMIN".equals(rs.getString("rol")) ? "selected" : "" %>>Administrador</option>
                                    </select>
                                    <button type="submit" class="btn btn-sm btn-outline-primary">Cambiar rol</button>
                                </form>
                            </td>
                        </tr>
                        <%
                                }
                                rs.getStatement().close();
                                rs.close();
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

