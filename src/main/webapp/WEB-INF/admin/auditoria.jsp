<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Auditoria" %>
<%
    List<Auditoria> logs = (List<Auditoria>) request.getAttribute("auditoria");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Auditoría</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="admin"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div class="page-header mb-0">
                <h1>Auditoría del sistema</h1>
                <p class="subtitle mb-0">Historial de acciones (quién hizo qué y cuándo)</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-secondary">Volver</a>
        </div>
        <div class="card card-custom">
            <div class="card-body">
                <% if (logs != null && !logs.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-custom">
                        <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Usuario</th>
                            <th>Acción</th>
                            <th>Tabla</th>
                            <th>ID</th>
                            <th>Detalle</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Auditoria a : logs) { %>
                        <tr>
                            <td><%= a.getFecha() != null ? a.getFecha().toString() : "-" %></td>
                            <td><%= a.getUsuarioNombre() != null ? a.getUsuarioNombre() : "-" %></td>
                            <td><%= a.getAccion() != null ? a.getAccion() : "-" %></td>
                            <td><%= a.getTablaAfectada() != null ? a.getTablaAfectada() : "-" %></td>
                            <td><%= a.getRegistroId() != null ? a.getRegistroId() : "-" %></td>
                            <td><%= a.getDetalle() != null ? a.getDetalle() : "-" %></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <p class="mb-0">No hay registros de auditoría.</p>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
