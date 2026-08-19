<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="${sessionScope.temaModo == 'oscuro' ? 'dark' : 'light'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM - Crear Nuevo Ticket</title>
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
            <nav class="col-md-2 d-md-block sidebar p-3 border-end">
                <ul class="nav flex-column">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/DashboardServlet"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/TicketServlet?action=list"><i class="fas fa-list me-2"></i>Mis Tickets</a></li>
                    <c:if test="${sessionScope.usuarioLogueado.rol == 'SOLICITANTE'}">
                        <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/TicketServlet?action=create"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                    </c:if>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ConfiguracionServlet"><i class="fas fa-gear me-2"></i>Configuración</a></li>
                </ul>
            </nav>

            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-plus-circle me-2 text-primary"></i>Crear Nueva Solicitud de Soporte</h1>
                    <a href="${pageContext.request.contextPath}/TicketServlet?action=list" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left me-1"></i>Cancelar
                    </a>
                </div>

                <div class="card shadow-sm">
                    <div class="card-header bg-dark text-white fw-bold">
                        <i class="fas fa-ticket me-2"></i>Formulario de Registro de Incidencia
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/TicketServlet" method="post">
                            <input type="hidden" name="action" value="insert">

                            <div class="mb-3">
                                <label for="titulo" class="form-label fw-semibold">Asunto / Título de la Solicitud</label>
                                <input type="text" class="form-control" id="titulo" name="titulo" placeholder="Ej. Falla crítica en servidor o Teclado no responde" required>
                                <div class="form-text">Si incluyes términos como "crítico" o "bloqueado", el sistema priorizará automáticamente tu solicitud.</div>
                            </div>

                            <div class="mb-3">
                                <label for="categoriaId" class="form-label fw-semibold">Categoría del Servicio</label>
                                <select class="form-select" id="categoriaId" name="categoriaId" required>
                                    <option value="">-- Seleccione una categoría --</option>
                                    <c:forEach var="cat" items="${categorias}">
                                        <option value="${cat.id}">${cat.nombre} - ${cat.descripcion}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="descripcion" class="form-label fw-semibold">Descripción Detallada del Inconveniente</label>
                                <textarea class="form-control" id="descripcion" name="descripcion" rows="5" placeholder="Explica claramente lo sucedido, mensajes de error presentados o el equipo afectado..." required></textarea>
                            </div>

                            <div class="d-flex justify-content-end gap-2">
                                <a href="${pageContext.request.contextPath}/TicketServlet?action=list" class="btn btn-secondary">Cancelar</a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-paper-plane me-1"></i>Enviar Solicitud
                                </button>
                            </div>
                        </form>
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
</body>
</html>
