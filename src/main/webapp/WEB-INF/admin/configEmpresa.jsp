<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Empresa" %>
<%
    Empresa empresa = (Empresa) request.getAttribute("empresa");
    Double itbis = (Double) request.getAttribute("itbis");
    if (empresa == null) empresa = new Empresa();
    if (itbis == null) itbis = 18.0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Configuración empresa</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="admin"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div class="page-header mb-0">
                <h1>Configuración de la empresa</h1>
                <p class="subtitle mb-0">RNC, nombre, dirección</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-secondary">Volver</a>
        </div>
        <div class="card card-custom">
            <div class="card-body">
                <form method="post" action="${pageContext.request.contextPath}/admin/empresa">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">RNC</label>
                            <input type="text" class="form-control" name="rnc" value="<%= empresa.getRnc() != null ? empresa.getRnc() : "" %>" placeholder="RNC de la empresa">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Nombre / Razón social</label>
                            <input type="text" class="form-control" name="nombre" value="<%= empresa.getNombre() != null ? empresa.getNombre() : "" %>" placeholder="Nombre de la empresa">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Dirección</label>
                        <input type="text" class="form-control" name="direccion" value="<%= empresa.getDireccion() != null ? empresa.getDireccion() : "" %>">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Teléfono</label>
                        <input type="text" class="form-control" name="telefono" value="<%= empresa.getTelefono() != null ? empresa.getTelefono() : "" %>">
                    </div>
                    <hr>
                    <h6>Impuesto ITBIS</h6>
                    <div class="mb-3" style="max-width: 150px;">
                        <label class="form-label">Porcentaje (%)</label>
                        <input type="number" step="0.01" min="0" max="100" class="form-control" name="itbis" value="<%= itbis %>">
                    </div>
                    <button type="submit" class="btn btn-primary">Guardar</button>
                </form>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
