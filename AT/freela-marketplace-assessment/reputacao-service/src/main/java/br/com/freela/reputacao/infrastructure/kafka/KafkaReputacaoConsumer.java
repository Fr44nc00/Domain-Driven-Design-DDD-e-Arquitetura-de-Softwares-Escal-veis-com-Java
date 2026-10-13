package br.com.freela.reputacao.infrastructure.kafka;

import br.com.freela.reputacao.application.ReputacaoService;
import br.com.freela.reputacao.infrastructure.messaging.ContratoEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class KafkaReputacaoConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaReputacaoConsumer.class);
    private final ObjectMapper objectMapper;
    private final ReputacaoService reputacaoService;

    public KafkaReputacaoConsumer(ObjectMapper objectMapper, ReputacaoService reputacaoService) {
        this.objectMapper = objectMapper;
        this.reputacaoService = reputacaoService;
    }

    @KafkaListener(topics = "contratos-events", groupId = "reputacao-service")
    public void consumir(String payload) throws Exception {
        log.info("Payload bruto recebido do Kafka (reputação): {}", payload);

        try {
            String json = objectMapper.readValue(payload, String.class);
            ContratoEventDTO evento = objectMapper.readValue(json, ContratoEventDTO.class);

            MDC.put("contratoId", evento.getContratoId().toString());
            MDC.put("eventId", evento.getEventId().toString());
            MDC.put("correlationId", evento.getCorrelationId());

            if ("ContratoConcluido".equals(evento.getEventType())) {
                reputacaoService.registrarContratoConcluido(
                        evento.getContratoId(),
                        evento.getFreelancerId(),
                        evento.getValor()
                );
                log.info("Reputação atualizada com sucesso para freelancerId={}", evento.getFreelancerId());
            } else {
                log.debug("Evento ignorado pelo reputacao-service: tipo={}", evento.getEventType());
            }
        } catch (Exception e) {
            log.error("Erro ao processar evento no reputacao-service. correlationId={} contratoId={} eventId={} Erro={}",
                    MDC.get("correlationId"), MDC.get("contratoId"), MDC.get("eventId"), e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}