package br.edu.infnet.ecommerce.pagamento.infrastructure;

import br.edu.infnet.ecommerce.shared.message.PedidoPagoMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoEventListener {

    @KafkaListener(topics = "pedido-pago", groupId = "pagamento-service")
    public void onPedidoPago(PedidoPagoMessage message) {
        System.out.println("📩 Evento recebido do Kafka: Pedido pago -> " + message.getPedidoId());
    }
}
