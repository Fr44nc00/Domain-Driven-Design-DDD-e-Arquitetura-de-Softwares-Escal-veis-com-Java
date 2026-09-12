package br.edu.infnet.ecommerce.shared.infrastructure;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic pedidoPagoTopic() {
        return new NewTopic("pedido-pago", 1, (short) 1);
    }
}
