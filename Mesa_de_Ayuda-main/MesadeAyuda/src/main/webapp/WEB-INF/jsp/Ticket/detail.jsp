<%-- 
    Document   : detail
    Created on : 13/08/2026, 1:42:42â€¯p.Â m.
    Author     : Dissmax
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle Ticket #T-${ticket.id} - Mesa de Ayuda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <!-- Navbar -->
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
                    <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-eye me-1"></i>Detalle</a></li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-1"></i>Agente PÃ©rez
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
                        <li class="nav-item"><a class="nav-link" href="create.jsp"><i class="fas fa-plus-circle me-2"></i>Crear Ticket</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-bell me-2"></i>Notificaciones <span class="badge bg-danger rounded-pill">3</span></a></li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
                    <div>
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb mb-1">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/DashboardServlet">Dashboard</a></li>
                                <li class="breadcrumb-item"><a href="list.jsp">Tickets</a></li>
                                <li class="breadcrumb-item active" aria-current="page">#T-${ticket.id}</li>
                            </ol>
                        </nav>
                        <h1 class="h2 mt-1"><i class="fas fa-ticket me-2 text-primary"></i>Ticket #T-${ticket.id}</h1>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-outline-secondary" onclick="window.print();">
                            <i class="fas fa-print me-1"></i>Imprimir
                        </button>
                        <button class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#historyModal">
                            <i class="fas fa-history me-1"></i>Historial
                        </button>
                    </div>
                </div>

                <!-- InformaciÃ³n del ticket -->
                <div class="row g-4">
                    <div class="col-lg-8">
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                                <h5 class="mb-0"><i class="fas fa-info-circle me-2"></i>InformaciÃ³n del Ticket</h5>
                                <div>
                                    <span class="badge bg-warning text-dark me-2"><i class="fas fa-clock me-1"></i>SLA: 2h 30m</span>
                                    <span class="badge bg-danger"><i class="fas fa-flag me-1"></i>${ticket.prioridad}</span>
                                </div>
                            </div>
                            <div class="card-body">
                                <h4 class="card-title">${ticket.titulo}</h4>
                                <div class="row mt-3">
                                    <div class="col-md-6">
                                        <p><strong><i class="fas fa-tag me-1"></i>CategorÃ­a:</strong> ${ticket.categoria.nombre}</p>
                                        <p><strong><i class="fas fa-user me-1"></i>Solicitante:</strong> ${ticket.solicitante.nombre}</p>
                                        <p><strong><i class="fas fa-calendar me-1"></i>Fecha creaciÃ³n:</strong> 10/08/2026 09:30</p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong><i class="fas fa-user-tie me-1"></i>Asignado a:</strong> <c:out value="${ticket.agente != null ? ticket.agente.nombre : 'Sin asignar'}"/></p>
                                        <p><strong><i class="fas fa-circle me-1"></i>Estado:</strong> <span class="badge bg-warning text-dark">${ticket.estado.nombre()}</span></p>
                                        <p><strong><i class="fas fa-clock me-1"></i>Ãšltima actualizaciÃ³n:</strong> 10/08/2026 11:45</p>
                                    </div>
                                </div>
                                <hr>
                                <h6><i class="fas fa-align-left me-2"></i>DescripciÃ³n</h6>
                                <p class="text-muted">${ticket.descripcion}</p>
                            </div>
                        </div>

                        <!-- Acciones segÃºn estado (Botones) -->
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-white">
                                <h5 class="mb-0"><i class="fas fa-arrows-spin me-2"></i>Acciones</h5>
                            </div>
                            <div class="card-body">
                                <div class="d-flex flex-wrap gap-2">
                                    <c:if test="${usuarioLogueado.rol == 'AGENTE' || usuarioLogueado.rol == 'ADMIN'}">
                                        <form action="${pageContext.request.contextPath}/TicketServlet" method="post">
                                            <input type="hidden" name="id" value="${ticket.id}">
                                            <button type="submit" name="action" value="atender" class="btn btn-warning">
                                                <i class="fas fa-forward me-1"></i>Atender (En Proceso)
                                            </button>
                                            <button type="submit" name="action" value="resolver" class="btn btn-success">
                                                <i class="fas fa-check me-1"></i>Resolver
                                            </button>
                                        </form>
                                    </c:if>
                                    
                                    <c:if test="${usuarioLogueado.rol == 'ADMIN'}">
                                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#reassignModal">
                                            <i class="fas fa-user-check me-1"></i>Reasignar
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <!-- Modal Reasignar (solo ADMIN) -->
                        <div class="modal fade" id="reassignModal" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form action="${pageContext.request.contextPath}/TicketServlet" method="post">
                                        <input type="hidden" name="action" value="reasignar">
                                        <input type="hidden" name="id" value="${ticket.id}">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Reasignar Ticket</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <div class="modal-body">
                                            <label class="form-label">Seleccionar Nuevo Agente</label>
                                            <select name="agenteId" class="form-select" required>
                                                <option value="">-- Seleccionar --</option>
                                                <c:forEach var="ag" items="${agentes}">
                                                    <option value="${ag.id}">${ag.nombre}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                            <button type="submit" class="btn btn-primary">Guardar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Comentarios -->
                            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                                <h5 class="mb-0"><i class="fas fa-comments me-2"></i>Comentarios (4)</h5>
                                <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#commentModal">
                                    <i class="fas fa-plus me-1"></i>Agregar comentario
                                </button>
                            </div>
                            <div class="card-body">
                                <div class="d-flex gap-3 mb-3 pb-3 border-bottom">
                                    <div class="flex-shrink-0">
                                        <div class="bg-primary rounded-circle text-white d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                                            <i class="fas fa-user"></i>
                                        </div>
                                    </div>
                                    <div>
                                        <h6 class="mb-0"><c:out value="${ticket.agente != null ? ticket.agente.nombre : 'Sin asignar'}"/> <small class="text-muted">(Agente)</small></h6>
                                        <small class="text-muted">10/08/2026 10:15</small>
                                        <p class="mt-1 mb-0">He revisado la fuente de poder y parece daÃ±ada. Solicito autorizaciÃ³n para cambiar la fuente de una PC en desuso.</p>
                                    </div>
                                </div>
                                <div class="d-flex gap-3 mb-3 pb-3 border-bottom">
                                    <div class="flex-shrink-0">
                                        <div class="bg-secondary rounded-circle text-white d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                                            <i class="fas fa-user"></i>
                                        </div>
                                    </div>
                                    <div>
                                        <h6 class="mb-0">${ticket.solicitante.nombre} <small class="text-muted">(Solicitante)</small></h6>
                                        <small class="text-muted">10/08/2026 10:45</small>
                                        <p class="mt-1 mb-0">Autorizado. Por favor, hazlo lo mÃ¡s rÃ¡pido posible, tenemos clase a las 2:00 PM.</p>
                                    </div>
                                </div>
                                <div class="d-flex gap-3">
                                    <div class="flex-shrink-0">
                                        <div class="bg-success rounded-circle text-white d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                                            <i class="fas fa-user"></i>
                                        </div>
                                    </div>
                                    <div>
                                        <h6 class="mb-0"><c:out value="${ticket.agente != null ? ticket.agente.nombre : 'Sin asignar'}"/> <small class="text-muted">(Agente)</small></h6>
                                        <small class="text-muted">10/08/2026 11:30</small>
                                        <p class="mt-1 mb-0">Fuente de poder reemplazada. El equipo ya enciende correctamente. Estoy en pruebas finales.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Sidebar derecha (detalles extra) -->
                    <div class="col-lg-4">
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-white">
                                <h5 class="mb-0"><i class="fas fa-arrows-spin me-2"></i>Acciones</h5>
                            </div>
                            <div class="card-body">
                                <div class="d-flex flex-wrap gap-2">
                                    <c:if test="${usuarioLogueado.rol == 'AGENTE' || usuarioLogueado.rol == 'ADMIN'}">
                                        <form action="${pageContext.request.contextPath}/TicketServlet" method="post">
                                            <input type="hidden" name="id" value="${ticket.id}">
                                            <button type="submit" name="action" value="atender" class="btn btn-warning">
                                                <i class="fas fa-forward me-1"></i>Atender (En Proceso)
                                            </button>
                                            <button type="submit" name="action" value="resolver" class="btn btn-success">
                                                <i class="fas fa-check me-1"></i>Resolver
                                            </button>
                                        </form>
                                    </c:if>
                                    
                                    <c:if test="${usuarioLogueado.rol == 'ADMIN'}">
                                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#reassignModal">
                                            <i class="fas fa-user-check me-1"></i>Reasignar
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <!-- Modal Reasignar (solo ADMIN) -->
                        <div class="modal fade" id="reassignModal" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form action="${pageContext.request.contextPath}/TicketServlet" method="post">
                                        <input type="hidden" name="action" value="reasignar">
                                        <input type="hidden" name="id" value="${ticket.id}">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Reasignar Ticket</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <div class="modal-body">
                                            <label class="form-label">Seleccionar Nuevo Agente</label>
                                            <select name="agenteId" class="form-select" required>
                                                <option value="">-- Seleccionar --</option>
                                                <c:forEach var="ag" items="${agentes}">
                                                    <option value="${ag.id}">${ag.nombre}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                            <button type="submit" class="btn btn-primary">Guardar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Comentarios -->
                            <div class="card-header bg-white">
                                <h5 class="mb-0"><i class="fas fa-paperclip me-2"></i>Archivos adjuntos</h5>
                            </div>
                            <div class="card-body">
                                <div class="d-flex align-items-center gap-2 mb-2">
                                    <i class="fas fa-file-pdf text-danger fa-lg"></i>
                                    <div>
                                        <div class="fw-semibold">diagnostico_inicial.pdf</div>
                                        <small class="text-muted">1.2 MB</small>
                                    </div>
                                    <a href="#" class="ms-auto"><i class="fas fa-download"></i></a>
                                </div>
                                <div class="d-flex align-items-center gap-2">
                                    <i class="fas fa-file-image text-success fa-lg"></i>
                                    <div>
                                        <div class="fw-semibold">foto_fuente_danada.jpg</div>
                                        <small class="text-muted">2.5 MB</small>
                                    </div>
                                    <a href="#" class="ms-auto"><i class="fas fa-download"></i></a>
                                </div>
                            </div>
                        </div>
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

    <!-- Modal Cambiar Estado -->
    <div class="modal fade" id="changeStateModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-arrows-spin me-2"></i>Cambiar Estado</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Ticket: <strong>#T-${ticket.id}</strong></p>
                    <p>Estado actual: <span class="badge bg-warning text-dark">${ticket.estado.nombre()}</span></p>
                    <div class="mb-3">
                        <label class="form-label">Nuevo estado</label>
                        <select class="form-select">
                            <option value="RESUELTO">âœ… Resuelto</option>
                            <option value="CERRADO">ðŸ”’ Cerrar (solo solicitante)</option>
                            <option value="CANCELADO">ðŸš« Cancelar</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Comentario (opcional)</label>
                        <textarea class="form-control" rows="2" placeholder="Describe el cambio..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary">Actualizar estado</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Agregar Comentario -->
    <div class="modal fade" id="commentModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-comment me-2"></i>Agregar Comentario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Comentario</label>
                        <textarea class="form-control" rows="4" placeholder="Escribe tu comentario..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary">Publicar</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>





