<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Factura" %>
<%
    Factura factura = (Factura) request.getAttribute("factura");
    boolean esEdicion = (factura != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= esEdicion ? "Editar factura" : "Nueva factura" %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Facturación de Artículos</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/">Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/facturas">Facturas</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="py-4">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h1 class="h4 mb-3"><%= esEdicion ? "Editar factura de artículos" : "Nueva factura de artículos" %></h1>

                        <form action="${pageContext.request.contextPath}/facturas" method="post" novalidate>
                            <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "insertar" %>"/>

                            <% if (esEdicion) { %>
                            <input type="hidden" name="id" value="<%= factura.getId() %>"/>
                            <% } %>

                            <div class="mb-3">
                                <label for="cliente" class="form-label">Cliente</label>
                                <input type="text" class="form-control" id="cliente" name="cliente"
                                       value="<%= esEdicion ? factura.getCliente() : "" %>" required>
                            </div>

                            <div class="mb-3">
                                <label for="fecha" class="form-label">Fecha</label>
                                <input type="date" class="form-control" id="fecha" name="fecha"
                                       value="<%= esEdicion ? factura.getFecha().toString() : "" %>" required>
                            </div>

                            <div class="mb-3">
                                <label for="hora" class="form-label">Hora</label>
                                <input type="time" class="form-control" id="hora" name="hora"
                                       value="<%= (esEdicion && factura.getHora() != null) ? factura.getHora().toString().substring(0,5) : "" %>">
                                <div class="form-text">
                                    Si dejas la hora vacía, se usará la hora actual.
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="total" class="form-label">Total (RD$)</label>
                                <div class="input-group">
                                    <span class="input-group-text">RD$</span>
                                    <input type="number" step="0.01" class="form-control" id="total" name="total"
                                           value="<%= esEdicion ? factura.getTotal() : "" %>" required>
                                </div>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/facturas" class="btn btn-outline-secondary">
                                    Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    Guardar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>

