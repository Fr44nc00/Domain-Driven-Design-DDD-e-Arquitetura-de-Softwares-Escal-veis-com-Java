package br.com.freela.contrato.domain.event;

import br.com.freela.contrato.domain.model.Contrato;
import java.util.UUID;

public class EntregaRegistradaEvent extends ContratoEvent {
    private final UUID clienteId;
    private final UUID freelancerId;

    public EntregaRegistradaEvent(Contrato contrato, String correlationId) {
        super(contrato.id(), "EntregaRegistrada", correlationId);
        this.clienteId = contrato.clienteId();
        this.freelancerId = contrato.freelancerId();
    }

    public UUID clienteId() { return clienteId; }
    public UUID freelancerId() { return freelancerId; }
}