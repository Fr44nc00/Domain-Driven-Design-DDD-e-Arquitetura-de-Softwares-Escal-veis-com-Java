package br.edu.infnet.ecommerce.pagamento.infrastructure.integration;

import br.edu.infnet.ecommerce.pagamento.domain.model.StatusPagamento;
import br.edu.infnet.ecommerce.pagamento.domain.port.PedidoGateway;
import br.edu.infnet.ecommerce.pedido.domain.Pedido;
import br.edu.infnet.ecommerce.pedido.infrastructure.PedidoRepository;
import org.springframework.stereotype.Component;

@Component
public class PedidoGatewayImpl implements PedidoGateway {

    private final PedidoRepository pedidoRepository;

    public PedidoGatewayImpl(
            PedidoRepository pedidoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void atualizarStatusPagamento(
            Long pedidoId,
            StatusPagamento status
    ) {

        Pedido pedido = pedidoRepository
                .findById(pedidoId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Pedido não encontrado"
                        )
                );

        if (status == StatusPagamento.APROVADO) {
            pedido.setStatus("PAGO");
        } else {
            pedido.setStatus("PAGAMENTO_RECUSADO");
        }

        pedidoRepository.save(pedido);
    }
}