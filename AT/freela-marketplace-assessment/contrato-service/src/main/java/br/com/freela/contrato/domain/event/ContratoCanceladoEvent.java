package br.com.freela.contrato.domain.event;

import br.com.freela.contrato.domain.model.Contrato;

public class ContratoCanceladoEvent extends ContratoEvent {
    private final String motivo;

    public ContratoCanceladoEvent(Contrato contrato, String correlationId, String motivo) {
        super(contrato.id(), "ContratoCancelado", correlationId);
        this.motivo = motivo;
    }

    public String motivo() { return motivo; }
}
