package com.edu.sena.mesadeayuda.dto;

public class NotificacionDTO {
    private Long id;
    private Long ticketId;
    private String tituloModal;
    private String mensaje;
    private String tipoIcono; // "success", "info", "warning", "error"

    public NotificacionDTO() {}

    public NotificacionDTO(Long id, Long ticketId, String tituloModal, String mensaje, String tipoIcono) {
        this.id = id;
        this.ticketId = ticketId;
        this.tituloModal = tituloModal != null ? tituloModal : "Notificación de Ticket";
        this.mensaje = mensaje;
        this.tipoIcono = tipoIcono != null ? tipoIcono : "info";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTituloModal() {
        return tituloModal;
    }

    public void setTituloModal(String tituloModal) {
        this.tituloModal = tituloModal;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipoIcono() {
        return tipoIcono;
    }

    public void setTipoIcono(String tipoIcono) {
        this.tipoIcono = tipoIcono;
    }
}
