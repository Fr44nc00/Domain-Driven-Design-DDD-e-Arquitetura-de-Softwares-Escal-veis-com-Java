package br.com.freela.contrato.domain.event;

import br.com.freela.contrato.domain.model.Contrato;

public class ContratoConcluidoEvent extends ContratoEvent {
    private final String titulo;

    public ContratoConcluidoEvent(Contrato contrato, String correlationId) {
        super(contrato.id(), "ContratoConcluido", correlationId);
        this.titulo = contrato.titulo();
    }

    public String titulo() { return titulo; }
}