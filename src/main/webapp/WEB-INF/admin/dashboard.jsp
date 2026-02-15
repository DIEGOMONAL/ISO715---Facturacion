<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Administración</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="admin"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="page-header mb-4">
            <h1>Panel de Administración</h1>
            <p class="subtitle">Gestión del sistema</p>
        </div>
        <div class="row g-4">
            <div class="col-md-6 col-lg-4">
                <a href="${pageContext.request.contextPath}/admin/pendientes" class="text-decoration-none">
                    <div class="card card-custom h-100">
                        <div class="card-body">
                            <h5 class="card-title">Usuarios pendientes</h5>
                            <p class="text-muted mb-0">Aprobar registros de nuevos usuarios</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-md-6 col-lg-4">
                <a href="${pageContext.request.contextPath}/admin/usuarios" class="text-decoration-none">
                    <div class="card card-custom h-100">
                        <div class="card-body">
                            <h5 class="card-title">Gestión de usuarios</h5>
                            <p class="text-muted mb-0">Crear, editar y administrar usuarios</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-md-6 col-lg-4">
                <a href="${pageContext.request.contextPath}/admin/empresa" class="text-decoration-none">
                    <div class="card card-custom h-100">
                        <div class="card-body">
                            <h5 class="card-title">Configuración empresa</h5>
                            <p class="text-muted mb-0">RNC, nombre, logo</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-md-6 col-lg-4">
                <a href="${pageContext.request.contextPath}/admin/auditoria" class="text-decoration-none">
                    <div class="card card-custom h-100">
                        <div class="card-body">
                            <h5 class="card-title">Auditoría</h5>
                            <p class="text-muted mb-0">Historial de acciones del sistema</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
