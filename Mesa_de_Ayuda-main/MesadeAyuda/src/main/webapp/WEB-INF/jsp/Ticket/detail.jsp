<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="${sessionScope.temaModo == 'oscuro' ? 'dark' : 'light'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM - Detalle del Ticket #${ticket.id}</title>
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
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ChatServlet"><i class="fas fa-comments me-2"></i>Chat en Vivo</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ConfiguracionServlet"><i class="fas fa-gear me-2"></i>Configuración</a></li>
                </ul>
            </nav>

            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2">Ticket <span class="text-primary">#T-${ticket.id}</span></h1>
                    <div class="d-flex gap-2">
                        <c:if test="${not empty ticketDominio.agente}">
                            <a href="${pageContext.request.contextPath}/ChatServlet?ticketId=${ticket.id}" class="btn btn-success">
                                <i class="fas fa-comments me-1"></i>Abrir Chat en Vivo
                            </a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/TicketServlet?action=list" class="btn btn-outline-secondary">
                            <i class="fas fa-arrow-left me-1"></i>Volver a la lista
                        </a>
                    </div>
                </div>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-triangle me-2"></i>${param.error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <div class="row g-4">
                    <!-- Información principal del ticket -->
                    <div class="col-lg-8">
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                                <h5 class="mb-0">${ticket.titulo}</h5>
                                <span class="badge bg-warning text-dark fs-6">${ticket.estadoNombre}</span>
                            </div>
                            <div class="card-body">
                                <h6 class="fw-bold text-muted mb-2">Descripción del problema:</h6>
                                <p class="card-text fs-6" style="white-space: pre-wrap;">${ticket.descripcion}</p>

                                <hr>

                                <!-- Botones de Acciones del Patrón State -->
                                <div class="d-flex flex-wrap gap-2 align-items-center">
                                    <span class="fw-bold me-2">Acciones de Ticket:</span>

                                    <!-- Agente / Admin: Atender ticket si está ASIGNADO -->
                                    <c:if test="${ticket.estadoNombre == 'ASIGNADO' && (sessionScope.usuarioLogueado.rol == 'AGENTE' || sessionScope.usuarioLogueado.rol == 'ADMIN')}">
                                        <form action="${pageContext.request.contextPath}/TicketServlet" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="atender">
                                            <input type="hidden" name="id" value="${ticket.id}">
                                            <button type="submit" class="btn btn-warning">
                                                <i class="fas fa-play me-1"></i>Iniciar Atención (EN PROCESO)
                                            </button>
                                        </form>
                                    </c:if>

                                    <!-- Agente / Admin: Resolver ticket si está EN_PROCESO -->
                                    <c:if test="${ticket.estadoNombre == 'EN_PROCESO' && (sessionScope.usuarioLogueado.rol == 'AGENTE' || sessionScope.usuarioLogueado.rol == 'ADMIN')}">
                                        <form action="${pageContext.request.contextPath}/TicketServlet" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="resolver">
                                            <input type="hidden" name="id" value="${ticket.id}">
                                            <button type="submit" class="btn btn-success">
                                                <i class="fas fa-check-circle me-1"></i>Resolver Ticket
                                            </button>
                                        </form>
                                    </c:if>

                                    <!-- Solicitante / Admin: Confirmar Cierre si está RESUELTO -->
                                    <c:if test="${ticket.estadoNombre == 'RESUELTO' && (sessionScope.usuarioLogueado.rol == 'SOLICITANTE' || sessionScope.usuarioLogueado.rol == 'ADMIN')}">
                                        <form action="${pageContext.request.contextPath}/TicketServlet" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="cerrar">
                                            <input type="hidden" name="id" value="${ticket.id}">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fas fa-lock me-1"></i>Confirmar y Cerrar
                                            </button>
                                        </form>
                                    </c:if>

                                    <!-- Solicitante / Admin: Reabrir ticket si está RESUELTO o CERRADO -->
                                    <c:if test="${(ticket.estadoNombre == 'RESUELTO' || ticket.estadoNombre == 'CERRADO') && (sessionScope.usuarioLogueado.rol == 'SOLICITANTE' || sessionScope.usuarioLogueado.rol == 'ADMIN')}">
                                        <form action="${pageContext.request.contextPath}/TicketServlet" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="reabrir">
                                            <input type="hidden" name="id" value="${ticket.id}">
                                            <button type="submit" class="btn btn-warning">
                                                <i class="fas fa-rotate-left me-1"></i>Reabrir Ticket
                                            </button>
                                        </form>
                                    </c:if>

                                    <!-- Admin: Cancelar ticket o Reasignar agente -->
                                    <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN' && ticket.estadoNombre != 'CERRADO' && ticket.estadoNombre != 'CANCELADO'}">
                                        <button class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#reassignModal">
                                            <i class="fas fa-user-gear me-1"></i>Reasignar Agente
                                        </button>
                                        <form action="${pageContext.request.contextPath}/TicketServlet" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="cancelar">
                                            <input type="hidden" name="id" value="${ticket.id}">
                                            <button type="submit" class="btn btn-outline-danger" onclick="return confirm('¿Seguro que deseas cancelar este ticket?');">
                                                <i class="fas fa-ban me-1"></i>Cancelar Ticket
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <!-- Sección de Comentarios (RF-07) -->
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                                <h5 class="mb-0"><i class="fas fa-comments me-2"></i>Historial de Comentarios del Ticket</h5>
                                <span class="badge bg-primary fs-6">${ticket.comentarios.size()} comentarios</span>
                            </div>
                            <div class="card-body">
                                <c:forEach var="com" items="${ticket.comentarios}">
                                    <div class="card mb-3 border-start border-4 border-primary shadow-sm">
                                        <div class="card-body py-2 px-3">
                                            <div class="d-flex justify-content-between align-items-center mb-1">
                                                <span class="fw-bold"><i class="fas fa-user-circle me-1 text-primary"></i>${com.autorNombre}</span>
                                                <small class="text-muted"><i class="fas fa-clock me-1"></i>${com.fechaFormateada}</small>
                                            </div>
                                            <p class="mb-0 fs-6" style="white-space: pre-wrap;">${com.texto}</p>
                                        </div>
                                    </div>
                                </c:forEach>
                                <c:if test="${empty ticket.comentarios}">
                                    <div class="alert alert-light text-center py-4 border text-muted">
                                        <i class="fas fa-comment-slash fa-2x mb-2 d-block text-secondary"></i>
                                        No hay comentarios registrados en este ticket aún. ¡Sé el primero en comentar!
                                    </div>
                                </c:if>

                                <hr class="my-4">

                                <!-- Formulario para Agregar Comentario -->
                                <form action="${pageContext.request.contextPath}/TicketServlet" method="post">
                                    <input type="hidden" name="action" value="comentar">
                                    <input type="hidden" name="id" value="${ticket.id}">
                                    <div class="mb-3">
                                        <label for="textoComentario" class="form-label fw-bold"><i class="fas fa-pen me-1"></i>Agregar Comentario / Observación:</label>
                                        <textarea class="form-control" id="textoComentario" name="textoComentario" rows="3" placeholder="Escribe aquí tu comentario para el ticket..." required></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-paper-plane me-1"></i>Publicar Comentario
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Panel lateral de metadatos -->
                    <div class="col-lg-4">
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-dark text-white fw-bold">
                                <i class="fas fa-info-circle me-2"></i>Detalles Técnicos y SLA
                            </div>
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>Categoría:</span>
                                    <span class="badge bg-info text-dark">${ticket.categoriaNombre}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>Prioridad:</span>
                                    <span class="badge bg-primary">${ticket.prioridad}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>Solicitante:</span>
                                    <span>${ticket.solicitanteNombre}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>Agente Asignado:</span>
                                    <span>${not empty ticket.agenteNombre ? ticket.agenteNombre : 'Sin asignar'}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>Fecha de Creación:</span>
                                    <small class="text-muted">${ticket.fechaCreacion}</small>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>Límite SLA (Strategy):</span>
                                    <small class="fw-bold text-danger">${ticket.fechaLimiteSLA}</small>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Modal Reasignar (solo ADMIN) -->
                <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN'}">
                    <div class="modal fade" id="reassignModal" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="${pageContext.request.contextPath}/TicketServlet" method="post">
                                    <input type="hidden" name="action" value="reasignar">
                                    <input type="hidden" name="id" value="${ticket.id}">
                                    <div class="modal-header">
                                        <h5 class="modal-title"><i class="fas fa-user-gear me-2"></i>Reasignar Agente</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <label class="form-label fw-semibold">Seleccionar Nuevo Agente Responsable:</label>
                                        <select name="agenteId" class="form-select" required>
                                            <option value="">-- Seleccionar Agente --</option>
                                            <c:forEach var="ag" items="${agentes}">
                                                <option value="${ag.id}">${ag.nombre} (${ag.correo})</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                        <button type="submit" class="btn btn-primary">Guardar Reasignación</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:if>

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
