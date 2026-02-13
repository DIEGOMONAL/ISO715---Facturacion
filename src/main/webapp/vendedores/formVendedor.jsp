<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Vendedor" %>
<%
    Vendedor vendedor = (Vendedor) request.getAttribute("vendedor");
    boolean esEdicion = (vendedor != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title><%= esEdicion ? "Editar" : "Nuevo" %> Vendedor</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="vendedores"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card card-custom">
                    <div class="card-body">
                        <h1 class="h4 mb-3"><%= esEdicion ? "Editar Vendedor" : "Nuevo Vendedor" %></h1>
                        <form action="${pageContext.request.contextPath}/vendedores" method="post">
                            <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "insertar" %>"/>
                            <% if (esEdicion) { %><input type="hidden" name="id" value="<%= vendedor.getId() %>"/><% } %>

                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="nombre" name="nombre"
                                       value="<%= esEdicion ? vendedor.getNombre() : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="porcientoComision" class="form-label">Porciento de Comisión (%)</label>
                                <input type="text" inputmode="decimal" class="form-control" id="porcientoComision" name="porcientoComision"
                                       value="<%= esEdicion ? new java.text.DecimalFormat("0.00").format(vendedor.getPorcientoComision()) : "0" %>" required placeholder="0.00">
                            </div>
                            <div class="mb-3">
                                <label for="estado" class="form-label">Estado</label>
                                <select class="form-select" id="estado" name="estado">
                                    <option value="ACTIVO" <%= (esEdicion && "ACTIVO".equals(vendedor.getEstado())) ? "selected" : "" %>>Activo</option>
                                    <option value="INACTIVO" <%= (esEdicion && "INACTIVO".equals(vendedor.getEstado())) ? "selected" : "" %>>Inactivo</option>
                                </select>
                            </div>
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/vendedores" class="btn btn-outline-secondary">Cancelar</a>
                                <button type="submit" class="btn btn-primary">Guardar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
