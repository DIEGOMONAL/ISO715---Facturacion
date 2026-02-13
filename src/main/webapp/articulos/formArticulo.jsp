<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Articulo" %>
<%
    Articulo articulo = (Articulo) request.getAttribute("articulo");
    boolean esEdicion = (articulo != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title><%= esEdicion ? "Editar" : "Nuevo" %> Artículo</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="articulos"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card card-custom">
                    <div class="card-body">
                        <h1 class="h4 mb-3"><%= esEdicion ? "Editar Artículo" : "Nuevo Artículo" %></h1>
                        <form action="${pageContext.request.contextPath}/articulos" method="post">
                            <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "insertar" %>"/>
                            <% if (esEdicion) { %><input type="hidden" name="id" value="<%= articulo.getId() %>"/><% } %>

                            <div class="mb-3">
                                <label for="descripcion" class="form-label">Descripción</label>
                                <input type="text" class="form-control" id="descripcion" name="descripcion"
                                       value="<%= esEdicion ? articulo.getDescripcion() : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="precioUnitario" class="form-label">Precio Unitario (RD$)</label>
                                <input type="number" step="0.01" class="form-control" id="precioUnitario" name="precioUnitario"
                                       value="<%= esEdicion ? articulo.getPrecioUnitario() : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="estado" class="form-label">Estado</label>
                                <select class="form-select" id="estado" name="estado">
                                    <option value="ACTIVO" <%= (esEdicion && "ACTIVO".equals(articulo.getEstado())) ? "selected" : "" %>>Activo</option>
                                    <option value="INACTIVO" <%= (esEdicion && "INACTIVO".equals(articulo.getEstado())) ? "selected" : "" %>>Inactivo</option>
                                </select>
                            </div>
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/articulos" class="btn btn-outline-secondary">Cancelar</a>
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
