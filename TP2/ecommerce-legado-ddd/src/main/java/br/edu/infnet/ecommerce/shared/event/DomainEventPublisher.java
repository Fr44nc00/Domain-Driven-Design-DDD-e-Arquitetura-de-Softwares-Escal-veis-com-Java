package br.edu.infnet.ecommerce.shared.event;

import br.edu.infnet.ecommerce.shared.message.PedidoPagoMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DomainEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPedidoPago(PedidoPagoEvent event) {
        PedidoPagoMessage message = new PedidoPagoMessage(event.getPedidoId());
        kafkaTemplate.send("pedido-pago", message);
    }
}
