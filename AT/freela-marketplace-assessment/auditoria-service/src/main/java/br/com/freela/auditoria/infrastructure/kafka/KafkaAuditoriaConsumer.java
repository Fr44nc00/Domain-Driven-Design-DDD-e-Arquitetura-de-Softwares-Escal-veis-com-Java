package br.com.freela.auditoria.infrastructure.kafka;

import br.com.freela.auditoria.application.AuditoriaService;
import br.com.freela.auditoria.infrastructure.messaging.ContratoEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class KafkaAuditoriaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaAuditoriaConsumer.class);
    private final ObjectMapper objectMapper;
    private final AuditoriaService auditoriaService;

    public KafkaAuditoriaConsumer(ObjectMapper objectMapper, AuditoriaService auditoriaService) {
        this.objectMapper = objectMapper;
        this.auditoriaService = auditoriaService;
    }

    @KafkaListener(topics = "contratos-events", groupId = "auditoria-service")
    public void consumir(String payload) throws Exception {
        log.info("Payload bruto recebido do Kafka (auditoria): {}", payload);

        try {
            String json = objectMapper.readValue(payload, String.class);
            ContratoEventDTO evento = objectMapper.readValue(json, ContratoEventDTO.class);

            MDC.put("contratoId", evento.getContratoId().toString());
            MDC.put("eventId", evento.getEventId().toString());
            MDC.put("correlationId", evento.getCorrelationId());

            auditoriaService.registrar(
                    evento.getEventId(),
                    evento.getContratoId(),
                    evento.getEventType(),
                    evento.getCorrelationId(),
                    json
            );

            log.info("Evento de auditoria registrado: tipo={}", evento.getEventType());
        } catch (Exception e) {
            log.error("Erro ao processar evento no auditoria-service. correlationId={} contratoId={} eventId={} Erro={}",
                    MDC.get("correlationId"), MDC.get("contratoId"), MDC.get("eventId"), e.getMessage(), e);

            throw e;
        } finally {
            MDC.clear();
        }
    }
}