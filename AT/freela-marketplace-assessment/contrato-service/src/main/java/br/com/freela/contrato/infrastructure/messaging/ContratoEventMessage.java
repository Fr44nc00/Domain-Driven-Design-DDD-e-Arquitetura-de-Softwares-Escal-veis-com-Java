package br.com.freela.contrato.infrastructure.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ContratoEventMessage {
    private UUID eventId;
    private String eventType;
    private UUID contratoId;
    private UUID clienteId;
    private UUID freelancerId;
    private String titulo;
    private BigDecimal valor;
    private String status;

    @JsonProperty("criadoEm")
    private Instant criadoEm;

    private String correlationId;

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public UUID getContratoId() { return contratoId; }
    public void setContratoId(UUID contratoId) { this.contratoId = contratoId; }

    public UUID getClienteId() { return clienteId; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }

    public UUID getFreelancerId() { return freelancerId; }
    public void setFreelancerId(UUID freelancerId) { this.freelancerId = freelancerId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Instant criadoEm) { this.criadoEm = criadoEm; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
}
