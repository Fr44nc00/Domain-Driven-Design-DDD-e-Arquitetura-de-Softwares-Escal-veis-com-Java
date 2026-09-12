package br.edu.infnet.ecommerce.pagamento.domain.port;

import br.edu.infnet.ecommerce.pagamento.domain.model.Pagamento;

import java.util.Optional;

public interface PagamentoRepository {

    Pagamento save(Pagamento pagamento);

    Optional<Pagamento> findByPedidoId(Long pedidoId);
}