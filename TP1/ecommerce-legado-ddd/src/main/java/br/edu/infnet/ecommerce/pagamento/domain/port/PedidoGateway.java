package br.edu.infnet.ecommerce.pagamento.domain.port;

import br.edu.infnet.ecommerce.pagamento.domain.model.StatusPagamento;

public interface PedidoGateway {

    void atualizarStatusPagamento(
            Long pedidoId,
            StatusPagamento status
    );
}