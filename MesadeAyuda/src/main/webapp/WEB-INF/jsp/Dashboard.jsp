<%-- 
    Document   : Dashboard
    Created on : 13/08/2026, 1:40:26 p. m.
    Author     : Dissmax
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Mesa de Ayuda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <!-- Header / Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="fas fa-headset me-2"></i>Mesa Ayuda CIMM
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="dashboard.jsp"><i class="fas fa-home me-1"></i>Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/tickets/list.jsp"><i class="fas fa-ticket-alt me-1"></i>Tickets</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/tickets/create.jsp"><i class="fas fa-plus-circle me-1"></i>Nuevo Ticket</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-1"></i>Agente Pérez
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
            <!-- Sidebar -->
            <nav class="col-md-2 d-md-block bg-light sidebar vh-100 p-3">
                <div class="position-sticky">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active" href="#">
                                <i class="fas fa-chart-pie me-2"></i>Resumen
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/tickets/list.jsp">
                                <i class="fas fa-list me-2"></i>Mis Tickets
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/tickets/create.jsp">
                                <i class="fas fa-plus-circle me-2"></i>Crear Ticket
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">
                                <i class="fas fa-bell me-2"></i>Notificaciones
                                <span class="badge bg-danger rounded-pill">3</span>
                            </a>
                        </li>
                        <li><hr></li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">
                                <i class="fas fa-cog me-2"></i>Configuración
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <!-- Encabezado -->
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-chart-simple me-2 text-primary"></i>Dashboard</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <span class="badge bg-success me-2"><i class="fas fa-clock me-1"></i>SLA: 2h 30m</span>
                        <button class="btn btn-sm btn-outline-secondary" onclick="window.location.reload();">
                            <i class="fas fa-sync-alt"></i>
                        </button>
                    </div>
                </div>

                <!-- Tarjetas de resumen -->
                <div class="row g-4 mb-4">
                    <div class="col-md-3">
                        <div class="card text-white bg-primary h-100">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">Total Tickets</h6>
                                        <h2 class="display-6 fw-bold">24</h2>
                                    </div>
                                    <i class="fas fa-ticket-alt fa-2x opacity-50"></i>
                                </div>
                                <small><i class="fas fa-arrow-up me-1"></i>+12% vs mes anterior</small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-warning h-100">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">En Proceso</h6>
                                        <h2 class="display-6 fw-bold">7</h2>
                                    </div>
                                    <i class="fas fa-spinner fa-2x opacity-50"></i>
                                </div>
                                <small><i class="fas fa-clock me-1"></i>3 con SLA próximo a vencer</small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-success h-100">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">Resueltos</h6>
                                        <h2 class="display-6 fw-bold">15</h2>
                                    </div>
                                    <i class="fas fa-check-circle fa-2x opacity-50"></i>
                                </div>
                                <small><i class="fas fa-check me-1"></i>92% de satisfacción</small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-danger h-100">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">SLA Vencidos</h6>
                                        <h2 class="display-6 fw-bold">2</h2>
                                    </div>
                                    <i class="fas fa-exclamation-triangle fa-2x opacity-50"></i>
                                </div>
                                <small><i class="fas fa-flag me-1"></i>Acción prioritaria requerida</small>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Últimos tickets -->
                <div class="card shadow-sm">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="fas fa-clock-rotate-left me-2"></i>Últimos tickets</h5>
                        <a href="${pageContext.request.contextPath}/tickets/list.jsp" class="btn btn-sm btn-primary">
                            Ver todos <i class="fas fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th>ID</th>
                                        <th>Título</th>
                                        <th>Categoría</th>
                                        <th>Prioridad</th>
                                        <th>Estado</th>
                                        <th>Asignado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><span class="badge bg-secondary">#T-001</span></td>
                                        <td><a href="${pageContext.request.contextPath}/tickets/detail.jsp" class="text-decoration-none">Equipo no enciende</a></td>
                                        <td><span class="badge bg-info">Hardware</span></td>
                                        <td><span class="badge bg-danger">Crítica</span></td>
                                        <td><span class="badge bg-warning text-dark">En Proceso</span></td>
                                        <td><i class="fas fa-user-tie me-1"></i>Carlos R.</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/tickets/detail.jsp" class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><span class="badge bg-secondary">#T-002</span></td>
                                        <td><a href="#" class="text-decoration-none">Problema con VPN</a></td>
                                        <td><span class="badge bg-info">Red</span></td>
                                        <td><span class="badge bg-warning">Media</span></td>
                                        <td><span class="badge bg-success">Resuelto</span></td>
                                        <td><i class="fas fa-user-tie me-1"></i>Ana M.</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/tickets/detail.jsp" class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><span class="badge bg-secondary">#T-003</span></td>
                                        <td><a href="#" class="text-decoration-none">Solicitud de Office 365</a></td>
                                        <td><span class="badge bg-info">Software</span></td>
                                        <td><span class="badge bg-success">Baja</span></td>
                                        <td><span class="badge bg-secondary">Nuevo</span></td>
                                        <td><i class="fas fa-user-tie me-1"></i>Sin asignar</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/tickets/detail.jsp" class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Footer -->
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
