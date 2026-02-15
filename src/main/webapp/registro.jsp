<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="util.Roles" %>
<%
    String error = (String) request.getAttribute("error");
    String mensaje = (String) request.getAttribute("mensaje");
    String usuarioVal = (String) request.getAttribute("usuarioVal");
    String nombreVal = (String) request.getAttribute("nombreVal");
    String rolVal = (String) request.getAttribute("rolVal");
    if (rolVal == null) rolVal = Roles.CAJERO;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registro - Sistema de Facturación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/estilos.css" rel="stylesheet">
</head>
<body class="login-wrapper">
<div class="login-card" style="max-width: 420px;">
    <h4>Crear cuenta</h4>
    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% if (mensaje != null) { %>
    <div class="alert alert-success"><%= mensaje %></div>
    <% } %>
    <form action="${pageContext.request.contextPath}/registro" method="post">
        <div class="mb-3">
            <label for="usuario" class="form-label">Usuario</label>
            <input type="text" class="form-control" id="usuario" name="usuario" required
                   value="<%= usuarioVal != null ? usuarioVal : "" %>" placeholder="Nombre de usuario">
        </div>
        <div class="mb-3">
            <label for="nombreCompleto" class="form-label">Nombre completo</label>
            <input type="text" class="form-control" id="nombreCompleto" name="nombreCompleto"
                   value="<%= nombreVal != null ? nombreVal : "" %>" placeholder="Ej: Juan Pérez">
        </div>
        <div class="mb-3">
            <label for="rol" class="form-label">Rol solicitado</label>
            <select class="form-select" id="rol" name="rol" required>
                <option value="CAJERO" <%= Roles.CAJERO.equals(rolVal) ? "selected" : "" %>><%= Roles.getLabel(Roles.CAJERO) %></option>
                <option value="SUPERVISOR" <%= Roles.SUPERVISOR.equals(rolVal) ? "selected" : "" %>><%= Roles.getLabel(Roles.SUPERVISOR) %></option>
                <option value="INVENTARIO" <%= Roles.INVENTARIO.equals(rolVal) ? "selected" : "" %>><%= Roles.getLabel(Roles.INVENTARIO) %></option>
                <option value="CONTADOR" <%= Roles.CONTADOR.equals(rolVal) ? "selected" : "" %>><%= Roles.getLabel(Roles.CONTADOR) %></option>
            </select>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Contraseña</label>
            <input type="password" class="form-control" id="password" name="password" required minlength="4" placeholder="Mínimo 4 caracteres">
        </div>
        <div class="mb-3">
            <label for="password2" class="form-label">Confirmar contraseña</label>
            <input type="password" class="form-control" id="password2" name="password2" required minlength="4">
        </div>
        <button type="submit" class="btn btn-primary w-100">Registrarse</button>
    </form>
    <p class="text-muted small mt-3 mb-0">
        ¿Ya tienes cuenta? <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
    </p>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
