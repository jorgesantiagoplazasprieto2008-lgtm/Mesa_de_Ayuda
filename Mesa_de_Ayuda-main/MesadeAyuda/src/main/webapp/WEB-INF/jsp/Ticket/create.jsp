<%-- 
    Document   : create
    Created on : 13/08/2026, 1:43:06â€¯p.Â m.
    Author     : Dissmax
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo Ticket - Mesa de Ayuda</title>
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
                    <li class="nav-item"><a class="nav-link" href="list.jsp"><i class="fas fa-ticket-alt me-1"></i>Tickets</a></li>
                    <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-plus-circle me-1"></i>Nuevo Ticket</a></li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-1"></i>MarÃ­a G.
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
            <!-- Sidebar -->
            <nav class="col-md-2 d-md-block bg-light sidebar vh-100 p-3">
                <div class="position-sticky">
                    <ul class="nav flex-column">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/DashboardServlet"><i class="fas fa-chart-pie me-2"></i>Resumen</a></li>
                        <li class="nav-item"><a class="nav-link" href="list.jsp"><i class="fas fa-list me-2"></i>Mis Tickets</a></li>
                        <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-bell me-2"></i>Notificaciones <span class="badge bg-danger rounded-pill">3</span></a></li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <h1 class="h2"><i class="fas fa-plus-circle me-2 text-primary"></i>Nuevo Ticket de Soporte</h1>
                </div>

                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/TicketServlet?action=insert" method="post">
                            <!-- TÃ­tulo -->
                            <div class="mb-3">
                                <label for="titulo" class="form-label fw-bold"><i class="fas fa-heading me-1"></i>TÃ­tulo del problema</label>
                                <input type="text" class="form-control form-control-lg" id="titulo" name="titulo" placeholder="Resumen claro del problema (ej: Equipo no enciende)" required>
                            </div>

                            <div class="row g-3">
                                <!-- CategorÃ­a -->
                                <div class="col-md-6">
                                    <label for="categoria" class="form-label fw-bold"><i class="fas fa-tag me-1"></i>CategorÃ­a</label>
                                    <select class="form-select" id="categoriaId" name="categoriaId" required>
                                        <option value="">Seleccionar categorÃ­a...</option>
                                        <option value="1">ðŸŒ Red</option>
                                        <option value="2">ðŸ–¥ï¸ Hardware</option>
                                        <option value="3">ðŸ’» Software</option>
                                        <option value="4">ðŸ”§ Mantenimiento</option>
                                        <option value="5">ðŸ“Œ Otro</option>
                                    </select>
                                </div>
                                <!-- Prioridad (se asignarÃ¡ automÃ¡ticamente, pero se muestra) -->
                                <div class="col-md-6">
                                    <label class="form-label fw-bold"><i class="fas fa-flag me-1"></i>Prioridad sugerida</label>
                                    <div class="mt-2">
                                        <span class="badge bg-secondary" id="prioridadSugerida">SegÃºn categorÃ­a</span>
                                        <small class="text-muted d-block">La prioridad se asignarÃ¡ automÃ¡ticamente</small>
                                    </div>
                                </div>
                            </div>

                            <!-- DescripciÃ³n -->
                            <div class="mb-3 mt-3">
                                <label for="descripcion" class="form-label fw-bold"><i class="fas fa-align-left me-1"></i>DescripciÃ³n detallada</label>
                                <textarea class="form-control" id="descripcion" name="descripcion" rows="5" placeholder="Describe tu problema con el mayor detalle posible..." required></textarea>
                            </div>

                            <!-- Archivos adjuntos -->
                            <div class="mb-3">
                                <label class="form-label fw-bold"><i class="fas fa-paperclip me-1"></i>Archivos adjuntos (opcional)</label>
                                <input class="form-control" type="file" multiple>
                                <small class="text-muted">Puedes adjuntar imÃ¡genes, capturas de pantalla o documentos (mÃ¡x. 5MB)</small>
                            </div>

                            <!-- Botones -->
                            <div class="d-flex gap-2 mt-4">
                                <button type="submit" class="btn btn-primary btn-lg flex-grow-1">
                                    <i class="fas fa-paper-plane me-2"></i>Crear Ticket
                                </button>
                                <button type="reset" class="btn btn-outline-secondary btn-lg">
                                    <i class="fas fa-undo me-1"></i>Limpiar
                                </button>
                                <a href="list.jsp" class="btn btn-outline-secondary btn-lg">
                                    <i class="fas fa-times me-1"></i>Cancelar
                                </a>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Info adicional -->
                <div class="alert alert-info mt-4">
                    <i class="fas fa-info-circle me-2"></i>
                    <strong>Â¿SabÃ­as que...?</strong> El sistema asignarÃ¡ automÃ¡ticamente un agente segÃºn la categorÃ­a y prioridad de tu ticket. RecibirÃ¡s notificaciones en cada cambio de estado.
                </div>

                <footer class="mt-5 pt-3 border-top text-muted">
                    <div class="d-flex justify-content-between align-items-center">
                        <small>Â© 2026 Mesa de Ayuda CIMM - Taller ADSO</small>
                        <small><i class="fas fa-code me-1"></i>Desarrollado con Java + Servlets</small>
                    </div>
                </footer>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // SimulaciÃ³n: cambiar prioridad sugerida segÃºn categorÃ­a
        document.getElementById('categoria').addEventListener('change', function() {
            const prioridadSpan = document.getElementById('prioridadSugerida');
            const map = {
                'Red': 'âš ï¸ Media',
                'Hardware': 'ðŸ”´ Alta',
                'Software': 'ðŸŸ¢ Baja',
                'Mantenimiento': 'ðŸŸ¡ Media',
                'Otro': 'ðŸ”µ Baja'
            };
            prioridadSpan.textContent = map[this.value] || 'SegÃºn categorÃ­a';
            prioridadSpan.className = this.value ? 'badge bg-info' : 'badge bg-secondary';
        });
    </script>
</body>
</html>





