<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.CondicionPago" %>
<%
    CondicionPago cp = (CondicionPago) request.getAttribute("condicionPago");
    boolean esEdicion = (cp != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title><%= esEdicion ? "Editar" : "Nueva" %> Condición de Pago</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="condiciones"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card card-custom">
                    <div class="card-body">
                        <h1 class="h4 mb-3"><%= esEdicion ? "Editar Condición de Pago" : "Nueva Condición de Pago" %></h1>
                        <form action="${pageContext.request.contextPath}/condicionesPago" method="post">
                            <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "insertar" %>"/>
                            <% if (esEdicion) { %><input type="hidden" name="id" value="<%= cp.getId() %>"/><% } %>

                            <div class="mb-3">
                                <label for="descripcion" class="form-label">Descripción</label>
                                <input type="text" class="form-control" id="descripcion" name="descripcion"
                                       value="<%= esEdicion ? cp.getDescripcion() : "" %>"
                                       placeholder="Ej: Al contado, A crédito 30 días, Cheque" required>
                            </div>
                            <div class="mb-3">
                                <label for="cantidadDias" class="form-label">Cantidad de Días</label>
                                <input type="number" min="0" class="form-control" id="cantidadDias" name="cantidadDias"
                                       value="<%= esEdicion ? cp.getCantidadDias() : "0" %>">
                            </div>
                            <div class="mb-3">
                                <label for="estado" class="form-label">Estado</label>
                                <select class="form-select" id="estado" name="estado">
                                    <option value="ACTIVO" <%= (esEdicion && "ACTIVO".equals(cp.getEstado())) ? "selected" : "" %>>Activo</option>
                                    <option value="INACTIVO" <%= (esEdicion && "INACTIVO".equals(cp.getEstado())) ? "selected" : "" %>>Inactivo</option>
                                </select>
                            </div>
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/condicionesPago" class="btn btn-outline-secondary">Cancelar</a>
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
