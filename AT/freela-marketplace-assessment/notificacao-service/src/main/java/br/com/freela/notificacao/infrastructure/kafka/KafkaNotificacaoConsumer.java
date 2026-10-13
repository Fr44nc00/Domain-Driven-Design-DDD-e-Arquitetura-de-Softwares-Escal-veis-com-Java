package br.com.freela.notificacao.infrastructure.kafka;

import br.com.freela.notificacao.application.NotificacaoService;
import br.com.freela.notificacao.infrastructure.messaging.ContratoEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class KafkaNotificacaoConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaNotificacaoConsumer.class);
    private final ObjectMapper objectMapper;
    private final NotificacaoService notificacaoService;

    public KafkaNotificacaoConsumer(ObjectMapper objectMapper, NotificacaoService notificacaoService) {
        this.objectMapper = objectMapper;
        this.notificacaoService = notificacaoService;
    }

    @KafkaListener(
            topics = "contratos-events",
            groupId = "notificacao-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumir(String payload) throws Exception {
        log.info("Payload bruto recebido do Kafka (notificação): {}", payload);

        try {
            String json = objectMapper.readValue(payload, String.class);
            ContratoEventDTO evento = objectMapper.readValue(json, ContratoEventDTO.class);

            MDC.put("contratoId", evento.getContratoId().toString());
            MDC.put("eventId", evento.getEventId().toString());
            MDC.put("correlationId", evento.getCorrelationId());

            notificacaoService.registrar(evento);
            log.info("Evento processado com sucesso: tipo={}", evento.getEventType());
        } catch (Exception e) {
            log.error("Erro ao processar evento no notificacao-service. correlationId={} contratoId={} eventId={} Erro={}",
                    MDC.get("correlationId"), MDC.get("contratoId"), MDC.get("eventId"), e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}