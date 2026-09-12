package br.edu.infnet.ecommerce.shared.event;

public class PedidoPagoEvent extends DomainEvent {
    private final Long pedidoId;

    public PedidoPagoEvent(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }
}
