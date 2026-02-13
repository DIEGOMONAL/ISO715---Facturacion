<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Cliente" %>
<%
    Cliente cliente = (Cliente) request.getAttribute("cliente");
    boolean esEdicion = (cliente != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title><%= esEdicion ? "Editar" : "Nuevo" %> Cliente</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="clientes"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card card-custom">
                    <div class="card-body">
                        <h1 class="h4 mb-3"><%= esEdicion ? "Editar Cliente" : "Nuevo Cliente" %></h1>
                        <form action="${pageContext.request.contextPath}/clientes" method="post">
                            <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "insertar" %>"/>
                            <% if (esEdicion) { %><input type="hidden" name="id" value="<%= cliente.getId() %>"/><% } %>

                            <div class="mb-3">
                                <label for="nombreComercial" class="form-label">Nombre Comercial / Razón Social</label>
                                <input type="text" class="form-control" id="nombreComercial" name="nombreComercial"
                                       value="<%= esEdicion ? cliente.getNombreComercial() : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="rncCedula" class="form-label">RNC o Cédula</label>
                                <input type="text" class="form-control" id="rncCedula" name="rncCedula"
                                       value="<%= esEdicion ? cliente.getRncCedula() : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="cuentaContable" class="form-label">Cuenta Contable</label>
                                <input type="text" class="form-control" id="cuentaContable" name="cuentaContable"
                                       value="<%= esEdicion && cliente.getCuentaContable() != null ? cliente.getCuentaContable() : "" %>">
                            </div>
                            <div class="mb-3">
                                <label for="estado" class="form-label">Estado</label>
                                <select class="form-select" id="estado" name="estado">
                                    <option value="ACTIVO" <%= (esEdicion && "ACTIVO".equals(cliente.getEstado())) ? "selected" : "" %>>Activo</option>
                                    <option value="INACTIVO" <%= (esEdicion && "INACTIVO".equals(cliente.getEstado())) ? "selected" : "" %>>Inactivo</option>
                                </select>
                            </div>
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/clientes" class="btn btn-outline-secondary">Cancelar</a>
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
