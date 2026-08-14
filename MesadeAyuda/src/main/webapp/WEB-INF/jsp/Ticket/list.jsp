<%-- 
    Document   : list
    Created on : 13/08/2026, 1:42:07 p. m.
    Author     : Dissmax
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Tickets - Mesa de Ayuda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <!-- Navbar igual al dashboard -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#"><i class="fas fa-headset me-2"></i>Mesa Ayuda CIMM</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="dashboard.jsp"><i class="fas fa-home me-1"></i>Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-ticket-alt me-1"></i>Tickets</a></li>
                    <li class="nav-item"><a class="nav-link" href="create.jsp"><i class="fas fa-plus-circle me-1"></i>Nuevo Ticket</a></li>
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
                        <li class="nav-item"><a class="nav-link" href="dashboard.jsp"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                        <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-list me-2"></i>Mis Tickets</a></li>
                        <li class="nav-item"><a class="nav-link" href="create.jsp"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-bell me-2"></i>Notificaciones <span class="badge bg-danger rounded-pill">3</span></a></li>
                        <li><hr></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-cog me-2"></i>Configuración</a></li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-list me-2 text-primary"></i>Lista de Tickets</h1>
                    <a href="create.jsp" class="btn btn-primary">
                        <i class="fas fa-plus-circle me-1"></i>Nuevo Ticket
                    </a>
                </div>

                <!-- Filtros -->
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <form class="row g-3 align-items-end" method="get">
                            <div class="col-md-3">
                                <label class="form-label"><i class="fas fa-search me-1"></i>Buscar</label>
                                <input type="text" class="form-control" placeholder="Título o descripción...">
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Estado</label>
                                <select class="form-select">
                                    <option value="">Todos</option>
                                    <option value="NUEVO">Nuevo</option>
                                    <option value="ASIGNADO">Asignado</option>
                                    <option value="EN_PROCESO">En Proceso</option>
                                    <option value="RESUELTO">Resuelto</option>
                                    <option value="CERRADO">Cerrado</option>
                                    <option value="CANCELADO">Cancelado</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Prioridad</label>
                                <select class="form-select">
                                    <option value="">Todas</option>
                                    <option value="BAJA">Baja</option>
                                    <option value="MEDIA">Media</option>
                                    <option value="ALTA">Alta</option>
                                    <option value="CRITICA">Crítica</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Categoría</label>
                                <select class="form-select">
                                    <option value="">Todas</option>
                                    <option value="Red">Red</option>
                                    <option value="Hardware">Hardware</option>
                                    <option value="Software">Software</option>
                                    <option value="Mantenimiento">Mantenimiento</option>
                                </select>
                            </div>
                            <div class="col-md-3 d-flex gap-2">
                                <button type="submit" class="btn btn-primary flex-grow-1">
                                    <i class="fas fa-filter me-1"></i>Filtrar
                                </button>
                                <button type="reset" class="btn btn-outline-secondary">
                                    <i class="fas fa-undo"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Tabla de tickets -->
                <div class="card shadow-sm">
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
                                        <th>SLA</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><span class="badge bg-secondary">#T-001</span></td>
                                        <td><a href="detail.jsp" class="text-decoration-none fw-semibold">Equipo no enciende</a></td>
                                        <td><span class="badge bg-info">Hardware</span></td>
                                        <td><span class="badge bg-danger">Crítica</span></td>
                                        <td><span class="badge bg-warning text-dark">En Proceso</span></td>
                                        <td><i class="fas fa-user-tie me-1"></i>Carlos R.</td>
                                        <td><span class="badge bg-danger"><i class="fas fa-clock me-1"></i>2h restantes</span></td>
                                        <td>
                                            <div class="btn-group">
                                                <a href="detail.jsp" class="btn btn-sm btn-outline-primary">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <button class="btn btn-sm btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#assignModal">
                                                    <i class="fas fa-user-check"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><span class="badge bg-secondary">#T-002</span></td>
                                        <td><a href="detail.jsp" class="text-decoration-none fw-semibold">Problema con VPN</a></td>
                                        <td><span class="badge bg-info">Red</span></td>
                                        <td><span class="badge bg-warning">Media</span></td>
                                        <td><span class="badge bg-success">Resuelto</span></td>
                                        <td><i class="fas fa-user-tie me-1"></i>Ana M.</td>
                                        <td><span class="badge bg-success"><i class="fas fa-check me-1"></i>Cumplido</span></td>
                                        <td>
                                            <a href="detail.jsp" class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><span class="badge bg-secondary">#T-003</span></td>
                                        <td><a href="detail.jsp" class="text-decoration-none fw-semibold">Solicitud de Office 365</a></td>
                                        <td><span class="badge bg-info">Software</span></td>
                                        <td><span class="badge bg-success">Baja</span></td>
                                        <td><span class="badge bg-secondary">Nuevo</span></td>
                                        <td><span class="text-muted">Sin asignar</span></td>
                                        <td><span class="badge bg-secondary"><i class="fas fa-clock me-1"></i>48h restantes</span></td>
                                        <td>
                                            <div class="btn-group">
                                                <a href="detail.jsp" class="btn btn-sm btn-outline-primary">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <button class="btn btn-sm btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#assignModal">
                                                    <i class="fas fa-user-check"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- Paginación -->
                    <div class="card-footer bg-white d-flex justify-content-between align-items-center">
                        <small>Mostrando 1-10 de 24 tickets</small>
                        <nav>
                            <ul class="pagination pagination-sm mb-0">
                                <li class="page-item disabled"><a class="page-link" href="#">Anterior</a></li>
                                <li class="page-item active"><a class="page-link" href="#">1</a></li>
                                <li class="page-item"><a class="page-link" href="#">2</a></li>
                                <li class="page-item"><a class="page-link" href="#">3</a></li>
                                <li class="page-item"><a class="page-link" href="#">Siguiente</a></li>
                            </ul>
                        </nav>
                    </div>
                </div>

                <!-- Modal Asignar Agente -->
                <div class="modal fade" id="assignModal" tabindex="-1">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title"><i class="fas fa-user-check me-2"></i>Asignar Agente</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                <p>Selecciona el agente responsable para el ticket <strong>#T-001</strong></p>
                                <select class="form-select">
                                    <option value="">--- Seleccionar ---</option>
                                    <option value="1">Carlos Rodríguez (Hardware)</option>
                                    <option value="2">Ana Martínez (Red)</option>
                                    <option value="3">Luis Gómez (Software)</option>
                                </select>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="button" class="btn btn-primary">Asignar</button>
                            </div>
                        </div>
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
    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>