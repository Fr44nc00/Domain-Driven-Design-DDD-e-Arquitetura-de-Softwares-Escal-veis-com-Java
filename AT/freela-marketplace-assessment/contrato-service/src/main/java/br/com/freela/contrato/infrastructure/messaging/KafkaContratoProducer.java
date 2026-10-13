package br.com.freela.contrato.infrastructure.messaging;

import br.com.freela.contrato.domain.event.ContratoEvent;
import br.com.freela.contrato.domain.event.ContratoCriado;
import br.com.freela.contrato.domain.event.ContratoConcluidoEvent;
import br.com.freela.contrato.domain.event.ContratoCanceladoEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaContratoProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaContratoProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaContratoProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(ContratoEvent event) {
        try {
            ContratoEventMessage message = new ContratoEventMessage();
            message.setEventId(event.eventId());
            message.setEventType(event.eventType());
            message.setContratoId(event.contratoId());
            message.setCorrelationId(event.correlationId());

            if (event instanceof ContratoCriado criado) {
                message.setClienteId(criado.clienteId());
                message.setFreelancerId(criado.freelancerId());
                message.setTitulo(criado.titulo());
                message.setValor(criado.valor());
                message.setStatus(criado.status());
                message.setCriadoEm(criado.criadoEm());
            } else if (event instanceof ContratoConcluidoEvent concluido) {
                message.setTitulo(concluido.titulo());
            } else if (event instanceof ContratoCanceladoEvent cancelado) {
                message.setStatus("Cancelado");
                message.setTitulo("Cancelado: " + cancelado.motivo());
            }

            String payload = objectMapper.writeValueAsString(message);

            MDC.put("contratoId", event.contratoId().toString());
            MDC.put("eventId", event.eventId().toString());
            MDC.put("correlationId", event.correlationId());

            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send("contratos-events", event.contratoId().toString(), payload);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("Mensagem publicada no Kafka. contratoId={} eventId={} correlationId={} offset={}",
                            event.contratoId(), event.eventId(), event.correlationId(), metadata.offset());
                    log.debug("Payload enviado ao Kafka: {}", payload);
                } else {
                    log.error("Falha ao publicar mensagem no Kafka. contratoId={} eventId={} correlationId={}",
                            event.contratoId(), event.eventId(), event.correlationId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Erro ao serializar evento contratoId={} eventType={}", event.contratoId(), event.eventType(), e);
        } finally {
            MDC.clear();
        }
    }
}