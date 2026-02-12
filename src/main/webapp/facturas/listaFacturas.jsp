<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Factura" %>
<%
    List<Factura> lista = (List<Factura>) request.getAttribute("listaFacturas");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Listado de Facturas</title>
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
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h1 class="h3 mb-0">Listado de facturas de artículos</h1>
            <a href="${pageContext.request.contextPath}/facturas?action=nuevo" class="btn btn-primary">
                Nueva factura
            </a>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-body">
                <%
                    if (lista != null && !lista.isEmpty()) {
                %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover align-middle mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">Cliente</th>
                            <th scope="col">Fecha</th>
                            <th scope="col">Hora</th>
                            <th scope="col">Total</th>
                            <th scope="col" class="text-end">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Factura f : lista) {
                        %>
                        <tr>
                            <td><%= f.getId() %></td>
                            <td><%= f.getCliente() %></td>
                            <td><%= f.getFecha() %></td>
                            <td><%= f.getHora() != null ? f.getHora().toString().substring(0,5) : "-" %></td>
                            <td>RD$ <%= String.format("%.2f", f.getTotal()) %></td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/facturas?action=editar&id=<%= f.getId() %>"
                                   class="btn btn-sm btn-outline-secondary me-1">
                                    Editar
                                </a>
                                <a href="${pageContext.request.contextPath}/facturas?action=eliminar&id=<%= f.getId() %>"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Seguro que deseas eliminar esta factura?');">
                                    Eliminar
                                </a>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
                <%
                    } else {
                %>
                <div class="alert alert-info mb-0">
                    No hay facturas registradas. Haz clic en <strong>Nueva factura</strong> para crear la primera.
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>

