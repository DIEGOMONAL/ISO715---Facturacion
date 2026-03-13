<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registro de usuario - Sistema de Facturación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/estilos.css" rel="stylesheet">
</head>
<body class="login-wrapper">
<div class="login-card">
    <h4>Registro de Usuario</h4>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
    <% } %>
    <form action="${pageContext.request.contextPath}/registro" method="post">
        <div class="mb-3">
            <label for="usuario" class="form-label">Usuario</label>
            <input type="text" class="form-control" id="usuario" name="usuario" required autofocus>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Contraseña</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="mb-3">
            <label for="password2" class="form-label">Confirmar contraseña</label>
            <input type="password" class="form-control" id="password2" name="password2" required>
        </div>
        <button type="submit" class="btn btn-primary w-100">Registrarse</button>
    </form>
    <p class="text-muted mb-0 mt-2">Tu cuenta quedará pendiente hasta que un administrador la apruebe.</p>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

