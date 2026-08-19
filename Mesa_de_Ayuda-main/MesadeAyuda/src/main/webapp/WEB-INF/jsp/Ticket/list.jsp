<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="${sessionScope.temaModo == 'oscuro' ? 'dark' : 'light'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM - Lista de Tickets</title>
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
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/DashboardServlet"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/TicketServlet?action=list"><i class="fas fa-list me-2"></i>Mis Tickets</a></li>
                    <c:if test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/TicketServlet?action=create"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                    </c:if>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ConfiguracionServlet"><i class="fas fa-gear me-2"></i>Configuración</a></li>
                </ul>
            </nav>

            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-list me-2 text-primary"></i>Tickets de Soporte</h1>
                    <c:if test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                        <a href="${pageContext.request.contextPath}/TicketServlet?action=create" class="btn btn-primary">
                            <i class="fas fa-plus me-1"></i>Nuevo Ticket
                        </a>
                    </c:if>
                </div>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-triangle me-2"></i>${param.error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Filtros -->
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/TicketServlet" method="get" class="row g-3 align-items-end">
                            <input type="hidden" name="action" value="list">
                            <div class="col-md-3">
                                <label class="form-label fw-semibold">Estado</label>
                                <select name="estado" class="form-select">
                                    <option value="">-- Todos --</option>
                                    <option value="NUEVO" ${estadoFiltro == 'NUEVO' ? 'selected' : ''}>Nuevo</option>
                                    <option value="ASIGNADO" ${estadoFiltro == 'ASIGNADO' ? 'selected' : ''}>Asignado</option>
                                    <option value="EN_PROCESO" ${estadoFiltro == 'EN_PROCESO' ? 'selected' : ''}>En Proceso</option>
                                    <option value="RESUELTO" ${estadoFiltro == 'RESUELTO' ? 'selected' : ''}>Resuelto</option>
                                    <option value="CERRADO" ${estadoFiltro == 'CERRADO' ? 'selected' : ''}>Cerrado</option>
                                    <option value="CANCELADO" ${estadoFiltro == 'CANCELADO' ? 'selected' : ''}>Cancelado</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label fw-semibold">Prioridad</label>
                                <select name="prioridad" class="form-select">
                                    <option value="">-- Todas --</option>
                                    <option value="BAJA" ${prioridadFiltro == 'BAJA' ? 'selected' : ''}>Baja</option>
                                    <option value="MEDIA" ${prioridadFiltro == 'MEDIA' ? 'selected' : ''}>Media</option>
                                    <option value="ALTA" ${prioridadFiltro == 'ALTA' ? 'selected' : ''}>Alta</option>
                                    <option value="CRITICA" ${prioridadFiltro == 'CRITICA' ? 'selected' : ''}>Crítica</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label fw-semibold">Categoría</label>
                                <select name="categoriaId" class="form-select">
                                    <option value="">-- Todas --</option>
                                    <c:forEach var="cat" items="${categorias}">
                                        <option value="${cat.id}" ${categoriaFiltro == cat.id ? 'selected' : ''}>${cat.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-3 d-flex gap-2">
                                <button type="submit" class="btn btn-primary flex-grow-1">
                                    <i class="fas fa-filter me-1"></i>Filtrar
                                </button>
                                <a href="${pageContext.request.contextPath}/TicketServlet?action=list" class="btn btn-outline-secondary">
                                    <i class="fas fa-undo"></i>
                                </a>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Tabla de tickets -->
                <div class="card shadow-sm">
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
                                        <th>SLA Límite</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="ticket" items="${tickets}">
                                    <tr>
                                        <td><span class="badge bg-secondary">#T-${ticket.id}</span></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/TicketServlet?action=detail&id=${ticket.id}" class="text-decoration-none fw-semibold">
                                                ${ticket.titulo}
                                            </a>
                                        </td>
                                        <td><span class="badge bg-info text-dark">${ticket.categoriaNombre}</span></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${ticket.prioridad == 'CRITICA'}"><span class="badge bg-danger">CRÍTICA</span></c:when>
                                                <c:when test="${ticket.prioridad == 'ALTA'}"><span class="badge bg-warning text-dark">ALTA</span></c:when>
                                                <c:when test="${ticket.prioridad == 'MEDIA'}"><span class="badge bg-primary">MEDIA</span></c:when>
                                                <c:otherwise><span class="badge bg-secondary">BAJA</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <span class="badge bg-success">${ticket.estadoNombre}</span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty ticket.agenteNombre}">
                                                    <i class="fas fa-user-tie me-1"></i>${ticket.agenteNombre}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Sin asignar</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <small class="text-muted"><i class="fas fa-clock me-1"></i>${ticket.fechaLimiteSLA}</small>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/TicketServlet?action=detail&id=${ticket.id}" class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-eye me-1"></i>Ver
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty tickets}">
                                    <tr><td colspan="8" class="text-center py-4 text-muted">No se encontraron tickets registrados.</td></tr>
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
