<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="${sessionScope.temaModo == 'oscuro' ? 'dark' : 'light'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM - Panel Principal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
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
            <nav class="col-md-2 d-md-block sidebar p-3 border-end">
                <ul class="nav flex-column">
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/DashboardServlet"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/TicketServlet?action=list"><i class="fas fa-list me-2"></i>Mis Tickets</a></li>
                    <c:if test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/TicketServlet?action=create"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                    </c:if>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ChatServlet"><i class="fas fa-comments me-2"></i>Chat en Vivo</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ConfiguracionServlet"><i class="fas fa-gear me-2"></i>Configuración</a></li>
                </ul>
            </nav>

            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-chart-line me-2 text-primary"></i>Panel Principal - Rol: ${sessionScope.usuarioLogueado.rol}</h1>
                    <c:if test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                        <a href="${pageContext.request.contextPath}/TicketServlet?action=create" class="btn btn-primary">
                            <i class="fas fa-plus me-1"></i>Nuevo Ticket
                        </a>
                    </c:if>
                </div>

                <!-- Tarjetas de Estadísticas Dinámicas -->
                <div class="row g-3 mb-4">
                    <div class="col-md-3">
                        <div class="card text-white bg-primary h-100 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">${stat1_label}</h6>
                                        <h2 class="display-6 fw-bold mb-0">${stat1_val}</h2>
                                    </div>
                                    <i class="fas fa-ticket fa-2x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-warning h-100 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">${stat2_label}</h6>
                                        <h2 class="display-6 fw-bold mb-0">${stat2_val}</h2>
                                    </div>
                                    <i class="fas fa-spinner fa-2x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-info h-100 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">${stat3_label}</h6>
                                        <h2 class="display-6 fw-bold mb-0">${stat3_val}</h2>
                                    </div>
                                    <i class="fas fa-tasks fa-2x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-success h-100 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">${stat4_label}</h6>
                                        <h2 class="display-6 fw-bold mb-0">${stat4_val}</h2>
                                    </div>
                                    <i class="fas fa-check-circle fa-2x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Sección Especial para Rol Administrador: Carga de Agentes -->
                <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN'}">
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-dark text-white fw-bold">
                            <i class="fas fa-users-gear me-2"></i>Monitoreo de Carga de Trabajo por Agente
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <c:forEach var="entry" items="${cargaAgentes}">
                                    <div class="col-md-4">
                                        <div class="p-3 border rounded d-flex justify-content-between align-items-center">
                                            <div>
                                                <strong class="fw-bold fs-6"><i class="fas fa-user-tie me-2 text-primary"></i>${entry.key}</strong>
                                                <small class="d-block text-muted">Agente de Soporte</small>
                                            </div>
                                            <span class="badge bg-primary fs-6">${entry.value} activos</span>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Tabla de Tickets Recientes -->
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="fas fa-clock-rotate-left me-2"></i>Tickets Recientes</h5>
                        <a href="${pageContext.request.contextPath}/TicketServlet?action=list" class="btn btn-sm btn-outline-light">
                            Ver todos <i class="fas fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-dark">
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
                                    <c:forEach var="ticket" items="${ticketsRecientes}">
                                        <tr>
                                            <td><span class="badge bg-secondary">#T-${ticket.id}</span></td>
                                            <td><a href="${pageContext.request.contextPath}/TicketServlet?action=detail&id=${ticket.id}" class="text-decoration-none fw-semibold">${ticket.titulo}</a></td>
                                            <td><span class="badge bg-info text-dark">${ticket.categoriaNombre}</span></td>
                                            <td><span class="badge bg-primary">${ticket.prioridad}</span></td>
                                            <td><span class="badge bg-warning text-dark">${ticket.estadoNombre}</span></td>
                                            <td><i class="fas fa-user-tie me-1"></i>${not empty ticket.agenteNombre ? ticket.agenteNombre : 'Sin asignar'}</td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/TicketServlet?action=detail&id=${ticket.id}" class="btn btn-sm btn-outline-primary">
                                                    <i class="fas fa-eye me-1"></i>Ver
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty ticketsRecientes}">
                                        <tr><td colspan="7" class="text-center py-3 text-muted">No hay tickets registrados en este panel.</td></tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

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
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <c:if test="${not empty notificacionesAlert}">
        <script>
            document.addEventListener("DOMContentLoaded", function() {
                const notifs = [
                    <c:forEach var="notif" items="${notificacionesAlert}" varStatus="status">
                        {
                            title: "${notif.tituloModal}",
                            text: "${notif.mensaje}",
                            icon: "${notif.tipoIcono}"
                        }${not status.last ? ',' : ''}
                    </c:forEach>
                ];

                async function mostrarNotificaciones() {
                    for (const n of notifs) {
                        await Swal.fire({
                            title: n.title,
                            text: n.text,
                            icon: n.icon,
                            confirmButtonText: 'Entendido',
                            confirmButtonColor: '#0a2b4e',
                            timer: 10000,
                            timerProgressBar: true
                        });
                    }
                }

                if (notifs.length > 0) {
                    mostrarNotificaciones();
                }
            });
        </script>
    </c:if>
</body>
</html>
