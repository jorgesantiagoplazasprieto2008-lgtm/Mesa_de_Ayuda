<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="${sessionScope.temaModo == 'oscuro' ? 'dark' : 'light'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM - Configuración</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .sidebar { min-height: 100vh; }
        .sidebar .nav-link { color: inherit; font-weight: 500; margin-bottom: 0.2rem; border-radius: 0.5rem; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background-color: #0a2b4e; color: #fff; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/DashboardServlet">
                <i class="fas fa-headset me-2 text-warning"></i>Mesa de Ayuda CIMM
            </a>
            <div class="d-flex align-items-center ms-auto">
                <span class="text-white me-3 small">
                    <i class="fas fa-user-circle me-1 text-info"></i>${sessionScope.usuarioLogueado.nombre} 
                    <span class="badge bg-secondary ms-1">${sessionScope.usuarioLogueado.rol}</span>
                </span>
                <a class="btn btn-outline-danger btn-sm" href="${pageContext.request.contextPath}/LogoutServlet">
                    <i class="fas fa-sign-out-alt me-1"></i>Cerrar sesión
                </a>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-2 d-md-block sidebar p-3 border-end">
                <ul class="nav flex-column">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/DashboardServlet"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/TicketServlet?action=list"><i class="fas fa-list me-2"></i>Mis Tickets</a></li>
                    <c:if test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/TicketServlet?action=create"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                    </c:if>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ChatServlet"><i class="fas fa-comments me-2"></i>Chat en Vivo</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/ConfiguracionServlet"><i class="fas fa-gear me-2"></i>Configuración</a></li>
                </ul>
            </nav>

            <!-- Main Content -->
            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-gear me-2 text-primary"></i>Configuración del Sistema y Perfil</h1>
                </div>

                <c:if test="${not empty mensajeExito}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i>${mensajeExito}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/ConfiguracionServlet" method="post">
                    <input type="hidden" name="action" value="actualizarPerfil">

                    <div class="row g-4">
                        <!-- Formulario de actualización de Perfil -->
                        <div class="col-lg-6">
                            <div class="card shadow-sm h-100">
                                <div class="card-header bg-dark text-white fw-bold">
                                    <i class="fas fa-user-gear me-2"></i>Perfil de Usuario (${sessionScope.usuarioLogueado.rol})
                                </div>
                                <div class="card-body">
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">ID de Usuario</label>
                                        <input type="text" class="form-control" value="#USR-${sessionScope.usuarioLogueado.id}" disabled>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Rol del Sistema</label>
                                        <input type="text" class="form-control" value="${sessionScope.usuarioLogueado.rol}" disabled>
                                    </div>

                                    <div class="mb-3">
                                        <label for="nombre" class="form-label fw-semibold">Nombre Completo</label>
                                        <input type="text" class="form-control" id="nombre" name="nombre" value="${sessionScope.usuarioLogueado.nombre}" required>
                                    </div>

                                    <div class="mb-3">
                                        <label for="correo" class="form-label fw-semibold">Correo Electrónico</label>
                                        <input type="email" class="form-control" id="correo" value="${sessionScope.usuarioLogueado.correo}" disabled>
                                        <small class="text-muted">El correo electrónico está vinculado a tu cuenta.</small>
                                    </div>

                                    <div class="mb-3">
                                        <label for="password" class="form-label fw-semibold"><i class="fas fa-key me-1 text-warning"></i>Cambiar Contraseña</label>
                                        <input type="password" class="form-control" id="password" name="password" placeholder="Escribe tu nueva contraseña para actualizarla">
                                        <small class="text-muted">Deja el campo vacío si no deseas cambiar tu contraseña actual.</small>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Preferencias del Sistema y Modo Oscuro -->
                        <div class="col-lg-6">
                            <div class="card shadow-sm h-100">
                                <div class="card-header bg-dark text-white fw-bold">
                                    <i class="fas fa-sliders me-2"></i>Preferencias de Notificaciones y Apariencia
                                </div>
                                <div class="card-body">
                                    <div class="mb-4">
                                        <h6 class="fw-bold"><i class="fas fa-palette me-2 text-info"></i>Apariencia de la Interfaz (Modo Oscuro):</h6>
                                        <select class="form-select" id="tema" name="tema">
                                            <option value="claro" ${sessionScope.temaModo != 'oscuro' ? 'selected' : ''}>Modo Claro (Predeterminado SENA)</option>
                                            <option value="oscuro" ${sessionScope.temaModo == 'oscuro' ? 'selected' : ''}>Modo Oscuro</option>
                                        </select>
                                    </div>

                                    <hr>

                                    <div class="mb-4">
                                        <h6 class="fw-bold"><i class="fas fa-bell me-2 text-primary"></i>Canales de Notificación Activos (Pattern Observer):</h6>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" checked id="canalEmail">
                                            <label class="form-check-label" for="canalEmail">Correo Electrónico (NotificacionEmailMock)</label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" checked id="canalConsola">
                                            <label class="form-check-label" for="canalConsola">Consola de Registro (NotificacionConsola)</label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" checked id="canalApp">
                                            <label class="form-check-label" for="canalApp">Alertas Emergentes SweetAlert2 (NotificacionEnApp)</label>
                                        </div>
                                    </div>

                                    <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN'}">
                                        <hr>
                                        <div class="mb-3">
                                            <h6 class="fw-bold"><i class="fas fa-sitemap me-2 text-warning"></i>Estrategia de Asignación (Admin):</h6>
                                            <select class="form-select">
                                                <option value="categoria" selected>Especialidad por Categoría (AsignacionPorCategoria)</option>
                                                <option value="rotativo">Turno Rotativo (AsignacionTurnoRotativo)</option>
                                                <option value="carga">Menor Carga de Trabajo (AsignacionMenorCarga)</option>
                                            </select>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="mt-4 text-end">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="fas fa-save me-1"></i>Guardar Cambios de Configuración
                        </button>
                    </div>
                </form>

                <footer class="mt-5 py-4 border-top">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-md-4 text-center text-md-start mb-2 mb-md-0">
                                <span class="fw-bold"><i class="fas fa-headset me-2 text-primary"></i>Mesa de Ayuda CIMM</span>
                            </div>
                            <div class="col-md-4 text-center mb-2 mb-md-0 text-muted small">
                                © 2026 Centro Industrial y del Desarrollo Tecnológico - SENA
                            </div>
                            <div class="col-md-4 text-center text-md-end text-muted small">
                                <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-3 py-2 rounded-pill">
                                    <i class="fas fa-shield-halved me-1"></i>Gestión de Soporte ADSO
                                </span>
                            </div>
                        </div>
                    </div>
                </footer>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
