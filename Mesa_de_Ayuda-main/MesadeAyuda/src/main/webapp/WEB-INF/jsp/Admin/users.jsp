<%-- 
    Document   : users
    Created on : 13/08/2026, 1:45:48â€¯p.Â m.
    Author     : Dissmax
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GestiÃ³n de Usuarios - Admin</title>
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
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/DashboardServlet"><i class="fas fa-home me-1"></i>Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link" href="../tickets/list.jsp"><i class="fas fa-ticket-alt me-1"></i>Tickets</a></li>
                    <li class="nav-item"><a class="nav-link" href="../tickets/create.jsp"><i class="fas fa-plus-circle me-1"></i>Nuevo Ticket</a></li>
                    <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-users me-1"></i>Usuarios</a></li>
                    <li class="nav-item"><a class="nav-link" href="reports.jsp"><i class="fas fa-chart-bar me-1"></i>Reportes</a></li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-1"></i>Admin
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="#"><i class="fas fa-user me-1"></i>Mi perfil</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/LogoutServlet"><i class="fas fa-sign-out-alt me-1"></i>Cerrar sesiÃ³n</a></li>
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
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/DashboardServlet"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                        <li class="nav-item"><a class="nav-link" href="../tickets/list.jsp"><i class="fas fa-list me-2"></i>Tickets</a></li>
                        <li class="nav-item"><a class="nav-link" href="../tickets/create.jsp"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                        <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-users me-2"></i>Usuarios</a></li>
                        <li class="nav-item"><a class="nav-link" href="reports.jsp"><i class="fas fa-chart-bar me-2"></i>Reportes</a></li>
                        <li><hr></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-cog me-2"></i>ConfiguraciÃ³n</a></li>
                    </ul>
                </div>
            </nav>

            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-users me-2 text-primary"></i>GestiÃ³n de Usuarios</h1>
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#userModal">
                        <i class="fas fa-user-plus me-1"></i>Nuevo Usuario
                    </button>
                </div>

                <!-- Filtros -->
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <form class="row g-3">
                            <div class="col-md-4">
                                <input type="text" class="form-control" placeholder="ðŸ” Buscar usuario...">
                            </div>
                            <div class="col-md-3">
                                <select class="form-select">
                                    <option value="">Todos los roles</option>
                                    <option value="SOLICITANTE">Solicitante</option>
                                    <option value="AGENTE">Agente</option>
                                    <option value="ADMIN">Administrador</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <button class="btn btn-primary w-100">Filtrar</button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Tabla de usuarios -->
                <div class="card shadow-sm">
                    <div class="card-body p-0">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre</th>
                                    <th>Correo</th>
                                    <th>Rol</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>1</td>
                                    <td><i class="fas fa-user-circle me-2"></i>MarÃ­a GonzÃ¡lez</td>
                                    <td>maria.g@cimm.edu</td>
                                    <td><span class="badge bg-info">Solicitante</span></td>
                                    <td><span class="badge bg-success">Activo</span></td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary"><i class="fas fa-edit"></i></button>
                                        <button class="btn btn-sm btn-outline-danger"><i class="fas fa-trash"></i></button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td><i class="fas fa-user-circle me-2"></i>Carlos RodrÃ­guez</td>
                                    <td>carlos.r@cimm.edu</td>
                                    <td><span class="badge bg-warning text-dark">Agente</span></td>
                                    <td><span class="badge bg-success">Activo</span></td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary"><i class="fas fa-edit"></i></button>
                                        <button class="btn btn-sm btn-outline-danger"><i class="fas fa-trash"></i></button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td><i class="fas fa-user-circle me-2"></i>Ana MartÃ­nez</td>
                                    <td>ana.m@cimm.edu</td>
                                    <td><span class="badge bg-warning text-dark">Agente</span></td>
                                    <td><span class="badge bg-success">Activo</span></td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary"><i class="fas fa-edit"></i></button>
                                        <button class="btn btn-sm btn-outline-danger"><i class="fas fa-trash"></i></button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>4</td>
                                    <td><i class="fas fa-user-circle me-2"></i>Luis GÃ³mez</td>
                                    <td>luis.g@cimm.edu</td>
                                    <td><span class="badge bg-warning text-dark">Agente</span></td>
                                    <td><span class="badge bg-danger">Inactivo</span></td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary"><i class="fas fa-edit"></i></button>
                                        <button class="btn btn-sm btn-outline-danger"><i class="fas fa-trash"></i></button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Footer -->
                <footer class="mt-5 pt-3 border-top text-muted">
                    <div class="d-flex justify-content-between align-items-center">
                        <small>Â© 2026 Mesa de Ayuda CIMM - Taller ADSO</small>
                        <small><i class="fas fa-code me-1"></i>Desarrollado con Java + Servlets</small>
                    </div>
                </footer>
            </main>
        </div>
    </div>

    <!-- Modal Nuevo Usuario -->
    <div class="modal fade" id="userModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-user-plus me-2"></i>Nuevo Usuario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="mb-3">
                            <label class="form-label">Nombre completo</label>
                            <input type="text" class="form-control" placeholder="Nombre y apellido">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Correo electrÃ³nico</label>
                            <input type="email" class="form-control" placeholder="usuario@cimm.edu">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Rol</label>
                            <select class="form-select">
                                <option value="SOLICITANTE">Solicitante</option>
                                <option value="AGENTE">Agente</option>
                                <option value="ADMIN">Administrador</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">ContraseÃ±a</label>
                            <input type="password" class="form-control" placeholder="â€¢â€¢â€¢â€¢â€¢â€¢â€¢â€¢">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary">Crear Usuario</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
