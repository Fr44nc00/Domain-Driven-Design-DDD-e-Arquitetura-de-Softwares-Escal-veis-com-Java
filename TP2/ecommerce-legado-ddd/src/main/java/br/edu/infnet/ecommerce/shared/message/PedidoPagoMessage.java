package br.edu.infnet.ecommerce.shared.message;

public class PedidoPagoMessage {
    private Long pedidoId;

    public PedidoPagoMessage(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }
}
