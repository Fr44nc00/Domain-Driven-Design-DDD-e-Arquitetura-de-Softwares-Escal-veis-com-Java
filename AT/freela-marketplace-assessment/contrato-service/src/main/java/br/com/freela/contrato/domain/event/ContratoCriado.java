package br.com.freela.contrato.domain.event;

import br.com.freela.contrato.domain.model.Contrato;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ContratoCriado extends ContratoEvent {
    private final UUID clienteId;
    private final UUID freelancerId;
    private final String titulo;
    private final BigDecimal valor;
    private final String status;
    private final Instant criadoEm;

    public ContratoCriado(Contrato contrato, String correlationId) {
        super(contrato.id(), "ContratoCriado", correlationId);
        this.clienteId = contrato.clienteId();
        this.freelancerId = contrato.freelancerId();
        this.titulo = contrato.titulo();
        this.valor = contrato.valor();
        this.status = contrato.status().name();
        this.criadoEm = Instant.now();
    }

    public UUID clienteId() { return clienteId; }
    public UUID freelancerId() { return freelancerId; }
    public String titulo() { return titulo; }
    public BigDecimal valor() { return valor; }
    public String status() { return status; }
    public Instant criadoEm() { return criadoEm; }
}
