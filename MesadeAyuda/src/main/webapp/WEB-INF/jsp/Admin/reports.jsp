<%-- 
    Document   : reports
    Created on : 13/08/2026, 1:44:29 p. m.
    Author     : Dissmax
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reportes - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#"><i class="fas fa-headset me-2"></i>Mesa Ayuda CIMM</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="dashboard.jsp"><i class="fas fa-home me-1"></i>Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link" href="../tickets/list.jsp"><i class="fas fa-ticket-alt me-1"></i>Tickets</a></li>
                    <li class="nav-item"><a class="nav-link" href="users.jsp"><i class="fas fa-users me-1"></i>Usuarios</a></li>
                    <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-chart-bar me-1"></i>Reportes</a></li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-1"></i>Admin
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="#"><i class="fas fa-user me-1"></i>Mi perfil</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="login.jsp"><i class="fas fa-sign-out-alt me-1"></i>Cerrar sesión</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-2 d-md-block bg-light sidebar vh-100 p-3">
                <div class="position-sticky">
                    <ul class="nav flex-column">
                        <li class="nav-item"><a class="nav-link" href="dashboard.jsp"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                        <li class="nav-item"><a class="nav-link" href="../tickets/list.jsp"><i class="fas fa-list me-2"></i>Tickets</a></li>
                        <li class="nav-item"><a class="nav-link" href="users.jsp"><i class="fas fa-users me-2"></i>Usuarios</a></li>
                        <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-chart-bar me-2"></i>Reportes</a></li>
                    </ul>
                </div>
            </nav>

            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-chart-bar me-2 text-primary"></i>Reportes y Métricas</h1>
                    <button class="btn btn-outline-secondary"><i class="fas fa-download me-1"></i>Exportar</button>
                </div>

                <!-- KPIs -->
                <div class="row g-4 mb-4">
                    <div class="col-md-3">
                        <div class="card">
                            <div class="card-body text-center">
                                <h6 class="text-muted">Total Tickets</h6>
                                <h2 class="display-5 fw-bold">24</h2>
                                <span class="badge bg-success"><i class="fas fa-arrow-up"></i> 12%</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card">
                            <div class="card-body text-center">
                                <h6 class="text-muted">Tasa de resolución</h6>
                                <h2 class="display-5 fw-bold">62.5%</h2>
                                <span class="badge bg-warning text-dark"><i class="fas fa-clock"></i> 15 pendientes</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card">
                            <div class="card-body text-center">
                                <h6 class="text-muted">SLA promedio</h6>
                                <h2 class="display-5 fw-bold">4.2h</h2>
                                <span class="badge bg-success"><i class="fas fa-check"></i> Dentro del SLA</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card">
                            <div class="card-body text-center">
                                <h6 class="text-muted">Agentes activos</h6>
                                <h2 class="display-5 fw-bold">4</h2>
                                <span class="badge bg-info">+1 este mes</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Gráficos simulados (usando Bootstrap + CSS) -->
                <div class="row g-4">
                    <div class="col-md-6">
                        <div class="card shadow-sm">
                            <div class="card-header bg-white">
                                <h5 class="mb-0"><i class="fas fa-chart-pie me-2"></i>Tickets por Estado</h5>
                            </div>
                            <div class="card-body">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Nuevo</span>
                                    <span class="fw-bold">3 <span class="text-muted">(12.5%)</span></span>
                                </div>
                                <div class="progress mb-2" style="height: 20px;">
                                    <div class="progress-bar bg-secondary" style="width: 12.5%;">3</div>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Asignado</span>
                                    <span class="fw-bold">4 <span class="text-muted">(16.7%)</span></span>
                                </div>
                                <div class="progress mb-2" style="height: 20px;">
                                    <div class="progress-bar bg-primary" style="width: 16.7%;">4</div>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>En Proceso</span>
                                    <span class="fw-bold">7 <span class="text-muted">(29.2%)</span></span>
                                </div>
                                <div class="progress mb-2" style="height: 20px;">
                                    <div class="progress-bar bg-warning" style="width: 29.2%;">7</div>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Resuelto</span>
                                    <span class="fw-bold">8 <span class="text-muted">(33.3%)</span></span>
                                </div>
                                <div class="progress mb-2" style="height: 20px;">
                                    <div class="progress-bar bg-success" style="width: 33.3%;">8</div>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Cerrado</span>
                                    <span class="fw-bold">2 <span class="text-muted">(8.3%)</span></span>
                                </div>
                                <div class="progress" style="height: 20px;">
                                    <div class="progress-bar bg-dark" style="width: 8.3%;">2</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card shadow-sm">
                            <div class="card-header bg-white">
                                <h5 class="mb-0"><i class="fas fa-chart-simple me-2"></i>Tickets por Categoría</h5>
                            </div>
                            <div class="card-body">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>🌐 Red</span>
                                    <span class="fw-bold">6</span>
                                </div>
                                <div class="progress mb-2" style="height: 12px;">
                                    <div class="progress-bar bg-info" style="width: 25%;">25%</div>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>🖥️ Hardware</span>
                                    <span class="fw-bold">8</span>
                                </div>
                                <div class="progress mb-2" style="height: 12px;">
                                    <div class="progress-bar bg-danger" style="width: 33.3%;">33.3%</div>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>💻 Software</span>
                                    <span class="fw-bold">7</span>
                                </div>
                                <div class="progress mb-2" style="height: 12px;">
                                    <div class="progress-bar bg-success" style="width: 29.2%;">29.2%</div>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>🔧 Mantenimiento</span>
                                    <span class="fw-bold">3</span>
                                </div>
                                <div class="progress" style="height: 12px;">
                                    <div class="progress-bar bg-warning" style="width: 12.5%;">12.5%</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Tabla de SLA vencidos -->
                <div class="card shadow-sm mt-4">
                    <div class="card-header bg-white">
                        <h5 class="mb-0"><i class="fas fa-exclamation-triangle text-danger me-2"></i>SLA Vencidos</h5>
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Ticket</th>
                                    <th>Prioridad</th>
                                    <th>Tiempo transcurrido</th>
                                    <th>Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="table-danger">
                                    <td><a href="../tickets/detail.jsp">#T-005</a></td>
                                    <td><span class="badge bg-danger">Crítica</span></td>
                                    <td><i class="fas fa-clock me-1"></i>6h (SLA: 4h)</td>
                                    <td><button class="btn btn-sm btn-warning">Reasignar</button></td>
                                </tr>
                                <tr class="table-danger">
                                    <td><a href="../tickets/detail.jsp">#T-012</a></td>
                                    <td><span class="badge bg-danger">Crítica</span></td>
                                    <td><i class="fas fa-clock me-1"></i>5h (SLA: 4h)</td>
                                    <td><button class="btn btn-sm btn-warning">Reasignar</button></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <footer class="mt-5 pt-3 border-top text-muted">
                    <div class="d-flex justify-content-between align-items-center">
                        <small>© 2026 Mesa de Ayuda CIMM - Taller ADSO</small>
                        <small><i class="fas fa-code me-1"></i>Desarrollado con Java + Servlets</small>
                    </div>
                </footer>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
