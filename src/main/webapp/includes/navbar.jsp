<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark navbar-custom">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Sistema de Facturación</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link ${param.page eq 'inicio' ? 'active' : ''}" href="${pageContext.request.contextPath}/">Inicio</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${param.page eq 'articulos' || param.page eq 'clientes' || param.page eq 'condiciones' || param.page eq 'vendedores' ? 'active' : ''}" href="#" id="catDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">Catálogos</a>
                    <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="catDropdown">
                        <li><a class="dropdown-item ${param.page eq 'articulos' ? 'active' : ''}" href="${pageContext.request.contextPath}/articulos">Artículos</a></li>
                        <li><a class="dropdown-item ${param.page eq 'clientes' ? 'active' : ''}" href="${pageContext.request.contextPath}/clientes">Clientes</a></li>
                        <li><a class="dropdown-item ${param.page eq 'condiciones' ? 'active' : ''}" href="${pageContext.request.contextPath}/condicionesPago">Condiciones de Pago</a></li>
                        <li><a class="dropdown-item ${param.page eq 'vendedores' ? 'active' : ''}" href="${pageContext.request.contextPath}/vendedores">Vendedores</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${param.page eq 'facturas' || param.page eq 'consulta' || param.page eq 'reporte' ? 'active' : ''}" href="#" id="factDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">Facturación</a>
                    <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="factDropdown">
                        <li><a class="dropdown-item ${param.page eq 'facturas' ? 'active' : ''}" href="${pageContext.request.contextPath}/facturas">Facturas</a></li>
                        <li><a class="dropdown-item ${param.page eq 'consulta' ? 'active' : ''}" href="${pageContext.request.contextPath}/consulta">Consulta</a></li>
                        <li><a class="dropdown-item ${param.page eq 'reporte' ? 'active' : ''}" href="${pageContext.request.contextPath}/reporte">Reporte Cliente</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <span class="nav-link usuario-label"><%= session.getAttribute("usuario") != null ? session.getAttribute("usuario") : "" %></span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">Salir</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
