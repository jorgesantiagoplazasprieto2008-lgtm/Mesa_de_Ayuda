<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="${sessionScope.temaModo == 'oscuro' ? 'dark' : 'light'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM - Chat en Tiempo Real</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <style>
        .sidebar { min-height: 100vh; }
        .sidebar .nav-link { color: inherit; font-weight: 500; margin-bottom: 0.2rem; border-radius: 0.5rem; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background-color: #0a2b4e; color: #fff; }
        .chat-box { height: 480px; overflow-y: auto; background-color: rgba(0, 0, 0, 0.02); }
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
            <!-- Sidebar Navegación General -->
            <nav class="col-md-2 d-md-block sidebar p-3 border-end">
                <ul class="nav flex-column">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/DashboardServlet"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/TicketServlet?action=list"><i class="fas fa-list me-2"></i>Mis Tickets</a></li>
                    <c:if test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/TicketServlet?action=create"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                    </c:if>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/ChatServlet"><i class="fas fa-comments me-2"></i>Chat en Vivo</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ConfiguracionServlet"><i class="fas fa-gear me-2"></i>Configuración</a></li>
                </ul>
            </nav>

            <!-- Main Content Area: Chat Panel -->
            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-comments me-2 text-primary"></i>Soporte y Atención en Tiempo Real</h1>
                    <span class="badge bg-success px-3 py-2 fs-6">
                        <i class="fas fa-circle me-1 animate-pulse"></i>Servicio Activo
                    </span>
                </div>

                <div class="row g-4">
                    <!-- Lista de Conversaciones por Ticket / Agente -->
                    <div class="col-lg-4">
                        <div class="card shadow-sm h-100">
                            <div class="card-header bg-dark text-white fw-bold d-flex justify-content-between align-items-center">
                                <span><i class="fas fa-headset me-2"></i>Mis Conversaciones</span>
                                <span class="badge bg-primary">${ticketsConAgente.size()} activos</span>
                            </div>
                            <div class="list-group list-group-flush" style="max-height: 540px; overflow-y: auto;">
                                <c:forEach var="t" items="${ticketsConAgente}">
                                    <a href="${pageContext.request.contextPath}/ChatServlet?ticketId=${t.id}" 
                                       class="list-group-item list-group-item-action ${ticketSeleccionado.id == t.id ? 'active fw-bold' : ''}">
                                        <div class="d-flex w-100 justify-content-between align-items-center mb-1">
                                            <h6 class="mb-0 text-truncate" style="max-width: 180px;">#T-${t.id}: ${t.titulo}</h6>
                                            <span class="badge bg-info text-dark">${t.estado.nombre()}</span>
                                        </div>
                                        <small class="d-block text-truncate">
                                            <c:choose>
                                                <c:when test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                                                    <i class="fas fa-user-tie me-1 text-warning"></i>Agente: ${t.agente.nombre}
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fas fa-user me-1 text-info"></i>Solicitante: ${t.solicitante.nombre}
                                                </c:otherwise>
                                            </c:choose>
                                        </small>
                                    </a>
                                </c:forEach>
                                <c:if test="${empty ticketsConAgente}">
                                    <div class="p-4 text-center text-muted">
                                        <i class="fas fa-comment-slash fa-2x mb-2 d-block opacity-50"></i>
                                        No hay tickets asignados con un agente disponible para chat.
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <!-- Panel de Chat Principal -->
                    <div class="col-lg-8">
                        <c:choose>
                            <c:when test="${not empty ticketSeleccionado}">
                                <div class="card shadow-sm">
                                    <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                                        <div>
                                            <h5 class="mb-0">
                                                <i class="fas fa-comment-dots me-2 text-warning"></i>Chat del Ticket #T-${ticketSeleccionado.id}: ${ticketSeleccionado.titulo}
                                            </h5>
                                            <small class="text-white-50">
                                                <c:choose>
                                                    <c:when test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                                                        Atendido por Agente: <strong>${ticketSeleccionado.agente.nombre}</strong> (${ticketSeleccionado.agente.correo})
                                                    </c:when>
                                                    <c:otherwise>
                                                        Solicitante: <strong>${ticketSeleccionado.solicitante.nombre}</strong> (${ticketSeleccionado.solicitante.correo})
                                                    </c:otherwise>
                                                </c:choose>
                                            </small>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/TicketServlet?action=detail&id=${ticketSeleccionado.id}" class="btn btn-outline-light btn-sm">
                                            <i class="fas fa-eye me-1"></i>Ver Ticket
                                        </a>
                                    </div>

                                    <!-- Mensajes del Chat con Scroll Automático -->
                                    <div class="card-body p-3 chat-box" id="chatContainer">
                                        <c:forEach var="m" items="${historialMensajes}">
                                            <div class="d-flex ${m.emisorId == sessionScope.usuarioLogueado.id ? 'justify-content-end' : 'justify-content-start'} mb-3">
                                                <div class="card shadow-sm ${m.emisorId == sessionScope.usuarioLogueado.id ? 'bg-primary text-white' : 'bg-body-tertiary'}" style="max-width: 75%; border-radius: 1rem;">
                                                    <div class="card-body py-2 px-3">
                                                        <div class="d-flex justify-content-between align-items-center mb-1 gap-2">
                                                            <strong class="small ${m.emisorId == sessionScope.usuarioLogueado.id ? 'text-white' : 'text-primary'}">
                                                                <i class="fas fa-user-circle me-1"></i>${m.emisorNombre} (${m.emisorRol})
                                                            </strong>
                                                            <small style="font-size: 0.7rem;" class="${m.emisorId == sessionScope.usuarioLogueado.id ? 'text-white-50' : 'text-muted'}">${m.fechaFormateada}</small>
                                                        </div>
                                                        <p class="mb-0" style="white-space: pre-wrap; font-size: 0.95rem;">${m.texto}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                        <c:if test="${empty historialMensajes}">
                                            <div class="text-center text-muted py-5" id="emptyNotice">
                                                <i class="fas fa-comments fa-3x mb-3 text-secondary opacity-50"></i>
                                                <p class="mb-0 fw-semibold">¡El canal de chat está abierto!</p>
                                                <small>Escribe un mensaje a continuación para conversar en tiempo real.</small>
                                            </div>
                                        </c:if>
                                    </div>

                                    <!-- Formulario para enviar mensajes -->
                                    <div class="card-footer bg-body border-top p-3">
                                        <form id="chatForm" action="${pageContext.request.contextPath}/ChatServlet" method="post" class="d-flex gap-2">
                                            <input type="hidden" name="ticketId" id="ticketIdInput" value="${ticketSeleccionado.id}">
                                            <textarea class="form-control" name="texto" id="mensajeInput" rows="2" placeholder="Escribe tu mensaje para el soporte técnico..." required></textarea>
                                            <button type="submit" class="btn btn-primary px-4 d-flex align-items-center justify-content-center">
                                                <i class="fas fa-paper-plane me-1"></i>Enviar
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="card shadow-sm p-5 text-center text-muted">
                                    <i class="fas fa-headset fa-4x mb-3 text-primary opacity-50"></i>
                                    <h4>No hay conversación seleccionada</h4>
                                    <p>Selecciona un ticket de la lista lateral para iniciar la comunicación en vivo.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
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

    <c:if test="${not empty ticketSeleccionado}">
        <script>
            document.addEventListener("DOMContentLoaded", function() {
                const container = document.getElementById("chatContainer");
                if (container) {
                    container.scrollTop = container.scrollHeight;
                }

                // Refresco automático AJAX cada 3 segundos en tiempo real
                const ticketId = "${ticketSeleccionado.id}";
                setInterval(function() {
                    fetch("${pageContext.request.contextPath}/ChatServlet?ajax=1&ticketId=" + ticketId)
                        .then(response => response.text())
                        .then(html => {
                            if (html && html.trim().length > 0 && container) {
                                const previousScroll = container.scrollHeight - container.scrollTop;
                                container.innerHTML = html;
                                // Auto-scroll si el usuario estaba al final
                                if (previousScroll <= container.clientHeight + 150) {
                                    container.scrollTop = container.scrollHeight;
                                }
                            }
                        })
                        .catch(err => console.log("Polling Chat error: ", err));
                }, 3000);
            });
        </script>
    </c:if>

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
