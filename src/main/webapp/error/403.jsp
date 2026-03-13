<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Acceso denegado</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="inicio"/></jsp:include>
<main class="py-5">
    <div class="container">
        <div class="card card-custom">
            <div class="card-body text-center">
                <h1 class="display-5 mb-3">Acceso denegado</h1>
                <p class="lead mb-4">No tienes permisos para acceder a esta sección.</p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Volver al inicio</a>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

