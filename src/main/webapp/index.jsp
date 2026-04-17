<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.ClienteDAO" %>
<%@ page import="dao.FacturaDAO" %>
<%@ page import="model.Cliente" %>
<%@ page import="model.Factura" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    DecimalFormat money = new DecimalFormat("#,##0.00");
    String rol = (String) session.getAttribute("rol");
    boolean esAdmin = "ADMIN".equalsIgnoreCase(rol);
    boolean esAuditor = "AUDITOR".equalsIgnoreCase(rol);
    boolean esSupervisor = "SUPERVISOR".equalsIgnoreCase(rol);

    int totalClientes = 0;
    int totalFacturas = 0;
    int facturasMes = 0;
    double ventasMes = 0;
    double cuentasPorCobrar = 0;
    double totalFacturadoHistorico = 0;
    List<Factura> ultimasFacturas = new ArrayList<>();
    List<Cliente> topDeudores = new ArrayList<>();
    Map<String, Double> ventasPorDia = new HashMap<>();
    List<String> etiquetasDias = new ArrayList<>();
    List<Double> valoresDias = new ArrayList<>();
    List<String> etiquetasDeudores = new ArrayList<>();
    List<Double> valoresDeudores = new ArrayList<>();

    LocalDate hoy = LocalDate.now();
    LocalDate inicioMes = hoy.withDayOfMonth(1);
    for (int i = 6; i >= 0; i--) {
        LocalDate dia = hoy.minusDays(i);
        ventasPorDia.put(dia.toString(), 0.0);
    }

    try {
        ClienteDAO clienteDAO = new ClienteDAO();
        FacturaDAO facturaDAO = new FacturaDAO();

        List<Cliente> clientes = clienteDAO.listarActivos();
        List<Factura> facturas = facturaDAO.listarConFiltros(null, "fecha", "desc");

        totalClientes = clientes != null ? clientes.size() : 0;
        totalFacturas = facturas != null ? facturas.size() : 0;

        if (clientes != null) {
            for (Cliente c : clientes) {
                cuentasPorCobrar += c.getBalance();
            }
            topDeudores = new ArrayList<>(clientes);
            topDeudores.sort(Comparator.comparingDouble(Cliente::getBalance).reversed());
            if (topDeudores.size() > 5) {
                topDeudores = topDeudores.subList(0, 5);
            }
        }

        if (facturas != null) {
            for (Factura f : facturas) {
                if (f.getFecha() != null) {
                    LocalDate fecha = f.getFecha().toLocalDate();
                    totalFacturadoHistorico += f.getTotal();
                    if (!fecha.isBefore(inicioMes)) {
                        facturasMes++;
                        ventasMes += f.getTotal();
                    }
                    String key = fecha.toString();
                    if (ventasPorDia.containsKey(key)) {
                        ventasPorDia.put(key, ventasPorDia.get(key) + f.getTotal());
                    }
                }
            }

            for (int i = 0; i < facturas.size() && i < 6; i++) {
                ultimasFacturas.add(facturas.get(i));
            }
        }

        for (int i = 6; i >= 0; i--) {
            LocalDate dia = hoy.minusDays(i);
            String key = dia.toString();
            etiquetasDias.add(dia.getDayOfMonth() + "/" + dia.getMonthValue());
            valoresDias.add(ventasPorDia.containsKey(key) ? ventasPorDia.get(key) : 0.0);
        }

        if (topDeudores != null) {
            for (Cliente c : topDeudores) {
                if (c.getBalance() <= 0) continue;
                etiquetasDeudores.add(c.getNombreComercial());
                valoresDeudores.add(c.getBalance());
            }
        }
    } catch (Exception e) {
        request.setAttribute("dashboardError", "No se pudo cargar el dashboard: " + e.getMessage());
    }

    double cobradoHistorico = Math.max(totalFacturadoHistorico - cuentasPorCobrar, 0);

    StringBuilder labelsDiasJs = new StringBuilder("[");
    StringBuilder dataDiasJs = new StringBuilder("[");
    for (int i = 0; i < etiquetasDias.size(); i++) {
        if (i > 0) {
            labelsDiasJs.append(",");
            dataDiasJs.append(",");
        }
        labelsDiasJs.append("'").append(etiquetasDias.get(i)).append("'");
        dataDiasJs.append(String.format(java.util.Locale.US, "%.2f", valoresDias.get(i)));
    }
    labelsDiasJs.append("]");
    dataDiasJs.append("]");

    StringBuilder labelsDeudoresJs = new StringBuilder("[");
    StringBuilder dataDeudoresJs = new StringBuilder("[");
    for (int i = 0; i < etiquetasDeudores.size(); i++) {
        if (i > 0) {
            labelsDeudoresJs.append(",");
            dataDeudoresJs.append(",");
        }
        String nombre = etiquetasDeudores.get(i) != null ? etiquetasDeudores.get(i).replace("'", "\\'") : "Cliente";
        labelsDeudoresJs.append("'").append(nombre).append("'");
        dataDeudoresJs.append(String.format(java.util.Locale.US, "%.2f", valoresDeudores.get(i)));
    }
    labelsDeudoresJs.append("]");
    dataDeudoresJs.append("]");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Inicio - Sistema de Facturación</title>
    <jsp:include page="/includes/head.jsp"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="inicio"/></jsp:include>

<main class="py-4">
    <div class="container">
        <div class="dashboard-hero mb-3">
            <div>
                <h1>Panel de control</h1>
                <p class="subtitle mb-0">Vista rápida de ventas, deuda de clientes y operaciones recientes</p>
            </div>
            <div class="hero-badge">Hoy: <%= hoy.getDayOfMonth() %>/<%= hoy.getMonthValue() %>/<%= hoy.getYear() %></div>
        </div>

        <% if (request.getAttribute("dashboardError") != null) { %>
        <div class="alert alert-danger"><%= request.getAttribute("dashboardError") %></div>
        <% } %>

        <div class="row g-3 mb-3 row-cols-2 row-cols-md-2 row-cols-xl-4">
            <div class="col">
                <div class="kpi-card h-100">
                    <span class="kpi-label">Facturas del mes</span>
                    <h3><%= facturasMes %></h3>
                </div>
            </div>
            <div class="col">
                <div class="kpi-card h-100">
                    <span class="kpi-label">Ventas del mes</span>
                    <h3>RD$ <%= money.format(ventasMes) %></h3>
                </div>
            </div>
            <div class="col">
                <div class="kpi-card h-100">
                    <span class="kpi-label">Clientes activos</span>
                    <h3><%= totalClientes %></h3>
                </div>
            </div>
            <div class="col">
                <div class="kpi-card kpi-warning h-100">
                    <span class="kpi-label">Cuentas por cobrar</span>
                    <h3>RD$ <%= money.format(cuentasPorCobrar) %></h3>
                </div>
            </div>
        </div>

        <div class="row g-3 align-items-stretch dashboard-row-charts">
            <div class="col-lg-8 d-flex">
                <div class="card card-custom w-100 dashboard-card-fill">
                    <div class="card-body d-flex flex-column">
                        <div class="d-flex justify-content-between align-items-center mb-2 flex-shrink-0">
                            <h5 class="card-title mb-0">Tendencia de ventas (7 días)</h5>
                            <small class="text-muted">Total facturas: <%= totalFacturas %></small>
                        </div>
                        <div class="chart-container chart-container-lg chart-flex-grow">
                            <canvas id="ventasChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 d-flex">
                <div class="card card-custom w-100 dashboard-card-fill">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title mb-2 flex-shrink-0">Distribución de cartera</h5>
                        <div class="chart-container chart-container-sm chart-flex-grow mb-2">
                            <canvas id="carteraChart"></canvas>
                        </div>
                        <div class="kpi-legend flex-shrink-0 mt-auto">
                            <div><span class="dot dot-cobrado"></span>Cobrado histórico: <strong>RD$ <%= money.format(cobradoHistorico) %></strong></div>
                            <div><span class="dot dot-pendiente"></span>Pendiente por cobrar: <strong>RD$ <%= money.format(cuentasPorCobrar) %></strong></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-3 mt-0 align-items-stretch dashboard-row-bottom">
            <div class="col-lg-8 d-flex">
                <div class="card card-custom w-100 dashboard-card-fill">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title mb-2 flex-shrink-0">Últimas facturas</h5>
                        <div class="table-responsive flex-grow-1 dashboard-table-wrap">
                            <table class="table table-hover table-custom mb-0">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Cliente</th>
                                    <th>Fecha</th>
                                    <th>Total</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <% if (ultimasFacturas != null && !ultimasFacturas.isEmpty()) {
                                       for (Factura f : ultimasFacturas) { %>
                                <tr>
                                    <td>#<%= f.getId() %></td>
                                    <td><%= f.getClienteNombre() != null ? f.getClienteNombre() : "-" %></td>
                                    <td><%= f.getFecha() != null ? f.getFecha() : "-" %></td>
                                    <td>RD$ <%= money.format(f.getTotal()) %></td>
                                    <td><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/facturas?action=ver&id=<%= f.getId() %>">Ver</a></td>
                                </tr>
                                <%   }
                                   } else { %>
                                <tr><td colspan="5" class="text-center text-muted">No hay facturas registradas.</td></tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 d-flex">
                <div class="card card-custom w-100 dashboard-card-fill">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title mb-2 flex-shrink-0">Top deudores</h5>
                        <% if (etiquetasDeudores != null && !etiquetasDeudores.isEmpty()) { %>
                        <div class="chart-container chart-container-md chart-flex-grow min-h-deudores">
                            <canvas id="deudoresChart"></canvas>
                        </div>
                        <% } else { %>
                        <div class="empty-deudores flex-grow-1 d-flex align-items-center justify-content-center">
                            <div class="text-center text-muted small px-2">
                                <span class="empty-deudores-icon" aria-hidden="true">&#10003;</span>
                                <div class="fw-semibold text-secondary">Sin deuda pendiente</div>
                                <div>Todos los clientes están al día.</div>
                            </div>
                        </div>
                        <% } %>

                        <hr class="dashboard-divider flex-shrink-0">
                        <div class="quick-actions-shell flex-shrink-0">
                            <div class="quick-actions-header">
                                <h6 class="mb-0">Accesos rápidos</h6>
                                <span class="quick-actions-pill">Operaciones frecuentes</span>
                            </div>
                            <div class="d-grid gap-2 quick-actions-btns">
                                <a href="${pageContext.request.contextPath}/facturas?action=nuevo"
                                   class="btn btn-primary btn-lg w-100 text-start d-flex align-items-center gap-3 py-3 px-3 rounded-3 shadow-sm">
                                    <i class="bi bi-file-earmark-plus fs-3 flex-shrink-0" aria-hidden="true"></i>
                                    <span class="flex-grow-1 min-w-0">
                                        <span class="d-block fw-semibold">Nueva factura</span>
                                        <small class="d-block text-white-50 mt-1">Registra una venta con sus artículos.</small>
                                    </span>
                                    <i class="bi bi-chevron-right fs-5 flex-shrink-0 opacity-75" aria-hidden="true"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/estadoCuenta"
                                   class="btn btn-outline-primary btn-lg w-100 text-start d-flex align-items-center gap-3 py-3 px-3 rounded-3 shadow-sm">
                                    <i class="bi bi-wallet2 fs-3 flex-shrink-0" aria-hidden="true"></i>
                                    <span class="flex-grow-1 min-w-0">
                                        <span class="d-block fw-semibold">Estado de cuenta</span>
                                        <small class="d-block text-muted mt-1">Consulta deuda y registra abonos.</small>
                                    </span>
                                    <i class="bi bi-chevron-right fs-5 flex-shrink-0 text-primary" aria-hidden="true"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/consulta"
                                   class="btn btn-outline-primary btn-lg w-100 text-start d-flex align-items-center gap-3 py-3 px-3 rounded-3 shadow-sm">
                                    <i class="bi bi-search fs-3 flex-shrink-0" aria-hidden="true"></i>
                                    <span class="flex-grow-1 min-w-0">
                                        <span class="d-block fw-semibold">Consulta de facturas</span>
                                        <small class="d-block text-muted mt-1">Filtra por cliente, fechas o artículo.</small>
                                    </span>
                                    <i class="bi bi-chevron-right fs-5 flex-shrink-0 text-primary" aria-hidden="true"></i>
                                </a>
                                <% if (esAdmin || esAuditor || esSupervisor) { %>
                                <a href="${pageContext.request.contextPath}/reporte"
                                   class="btn btn-outline-warning btn-lg w-100 text-start d-flex align-items-center gap-3 py-3 px-3 rounded-3 shadow-sm text-dark">
                                    <i class="bi bi-graph-up-arrow fs-3 flex-shrink-0 text-warning" aria-hidden="true"></i>
                                    <span class="flex-grow-1 min-w-0">
                                        <span class="d-block fw-semibold">Reporte por cliente</span>
                                        <small class="d-block text-muted mt-1">Analiza ventas por rango de fechas.</small>
                                    </span>
                                    <i class="bi bi-chevron-right fs-5 flex-shrink-0 text-warning" aria-hidden="true"></i>
                                </a>
                                <% } %>
                                <% if (esAdmin) { %>
                                <a href="${pageContext.request.contextPath}/clientes"
                                   class="btn btn-outline-dark btn-lg w-100 text-start d-flex align-items-center gap-3 py-3 px-3 rounded-3 shadow-sm">
                                    <i class="bi bi-people fs-3 flex-shrink-0" aria-hidden="true"></i>
                                    <span class="flex-grow-1 min-w-0">
                                        <span class="d-block fw-semibold">Gestionar clientes</span>
                                        <small class="d-block text-muted mt-1">Actualiza datos y estados del catálogo.</small>
                                    </span>
                                    <i class="bi bi-chevron-right fs-5 flex-shrink-0" aria-hidden="true"></i>
                                </a>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
<script>
    (function() {
        const formatMoney = (v) => 'RD$ ' + Number(v || 0).toLocaleString('es-DO', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

        const ventasCtx = document.getElementById('ventasChart');
        if (ventasCtx) {
            new Chart(ventasCtx, {
                type: 'line',
                data: {
                    labels: <%= labelsDiasJs.toString() %>,
                    datasets: [{
                        label: 'Ventas por día',
                        data: <%= dataDiasJs.toString() %>,
                        borderColor: '#0d9488',
                        backgroundColor: 'rgba(13,148,136,0.18)',
                        fill: true,
                        borderWidth: 3,
                        tension: 0.35,
                        pointRadius: 4,
                        pointHoverRadius: 6
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(ctx) {
                                    return formatMoney(ctx.parsed.y);
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) { return formatMoney(value); }
                            }
                        }
                    }
                }
            });
        }

        const carteraCtx = document.getElementById('carteraChart');
        if (carteraCtx) {
            new Chart(carteraCtx, {
                type: 'doughnut',
                data: {
                    labels: ['Cobrado', 'Pendiente'],
                    datasets: [{
                        data: [<%= String.format(java.util.Locale.US, "%.2f", cobradoHistorico) %>, <%= String.format(java.util.Locale.US, "%.2f", cuentasPorCobrar) %>],
                        backgroundColor: ['#10b981', '#f59e0b'],
                        borderWidth: 0
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { position: 'bottom' },
                        tooltip: {
                            callbacks: {
                                label: function(ctx) {
                                    return ctx.label + ': ' + formatMoney(ctx.parsed);
                                }
                            }
                        }
                    },
                    cutout: '68%'
                }
            });
        }

        const deudoresCtx = document.getElementById('deudoresChart');
        if (deudoresCtx) {
            new Chart(deudoresCtx, {
                type: 'bar',
                data: {
                    labels: <%= labelsDeudoresJs.toString() %>,
                    datasets: [{
                        label: 'Deuda',
                        data: <%= dataDeudoresJs.toString() %>,
                        backgroundColor: '#b45309',
                        borderRadius: 8
                    }]
                },
                options: {
                    indexAxis: 'y',
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(ctx) {
                                    return formatMoney(ctx.parsed.x);
                                }
                            }
                        }
                    },
                    scales: {
                        x: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) { return formatMoney(value); }
                            }
                        }
                    }
                }
            });
        }
    })();
</script>
</body>
</html>
