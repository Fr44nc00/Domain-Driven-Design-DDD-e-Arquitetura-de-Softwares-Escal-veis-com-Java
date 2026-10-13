package br.com.freela.contrato.application;

import br.com.freela.contrato.domain.event.ContratoEvent;
import br.com.freela.contrato.domain.model.Contrato;
import br.com.freela.contrato.domain.repository.ContratoRepository;
import br.com.freela.contrato.infrastructure.messaging.KafkaContratoProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ContratoApplicationService {
    private static final Logger log = LoggerFactory.getLogger(ContratoApplicationService.class);
    private final ContratoRepository repository;
    private final KafkaContratoProducer kafkaProducer;

    public ContratoApplicationService(ContratoRepository repository, KafkaContratoProducer kafkaProducer) {
        this.repository = repository;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public Contrato criar(CriarContratoCommand cmd) {
        log.info("contrato.criacao.inicio correlationId={} clienteId={} freelancerId={} titulo={} valor={}",
                cmd.correlationId(), cmd.clienteId(), cmd.freelancerId(), cmd.titulo(), cmd.valor());

        Contrato contrato = Contrato.criar(
                cmd.clienteId(),
                cmd.freelancerId(),
                cmd.titulo(),
                cmd.valor(),
                cmd.correlationId()
        );

        Contrato salvo = repository.salvar(contrato);

        for (var event : contrato.pullDomainEvents()) {
            MDC.put("contratoId", contrato.id().toString());
            MDC.put("eventId", event.eventId().toString());
            log.info("contrato.evento.publicacao contratoId={} eventId={} eventType={}", contrato.id(), event.eventId(), event.eventType());
            MDC.clear();

            if (event instanceof ContratoEvent contratoEvent) {
                kafkaProducer.publish(contratoEvent);
            }
        }

        log.info("contrato.criacao.sucesso correlationId={} contratoId={} clienteId={} freelancerId={} status={}",
                cmd.correlationId(), salvo.id(), salvo.clienteId(), salvo.freelancerId(), salvo.status());

        return salvo;
    }

    @Transactional(readOnly = true)
    public Contrato buscar(UUID id) {
        log.info("contrato.busca.inicio contratoId={}", id);
        var contrato = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado: " + id));
        log.info("contrato.busca.sucesso contratoId={} status={}", id, contrato.status());
        return contrato;
    }

    @Transactional(readOnly = true)
    public List<Contrato> listar() {
        log.info("contrato.listagem.inicio");
        var contratos = repository.listar();
        log.info("contrato.listagem.sucesso quantidade={}", contratos.size());
        return contratos;
    }

    @Transactional
    public Contrato registrarEntrega(UUID id, String correlationId) {
        log.info("contrato.entrega.inicio correlationId={} contratoId={}", correlationId, id);

        var contrato = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado: " + id));

        contrato.registrarEntrega(correlationId);
        var salvo = repository.salvar(contrato);

        for (var event : contrato.pullDomainEvents()) {
            log.info("contrato.evento.publicacao correlationId={} contratoId={} eventId={} eventType={}",
                    correlationId, contrato.id(), event.eventId(), event.eventType());
            if (event instanceof ContratoEvent contratoEvent) {
                kafkaProducer.publish(contratoEvent);
            }
        }

        log.info("contrato.entrega.sucesso correlationId={} contratoId={} status={}", correlationId, salvo.id(), salvo.status());
        return salvo;
    }

    @Transactional
    public Contrato concluir(UUID id, String correlationId) {
        log.info("contrato.concluir.inicio correlationId={} contratoId={}", correlationId, id);

        var contrato = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado: " + id));

        contrato.concluir(correlationId);
        var salvo = repository.salvar(contrato);

        for (var event : contrato.pullDomainEvents()) {
            log.info("contrato.evento.publicacao correlationId={} contratoId={} eventId={} eventType={}",
                    correlationId, contrato.id(), event.eventId(), event.eventType());
            if (event instanceof ContratoEvent contratoEvent) {
                kafkaProducer.publish(contratoEvent);
            }
        }

        log.info("contrato.concluir.sucesso correlationId={} contratoId={} status={}", correlationId, salvo.id(), salvo.status());
        return salvo;
    }

    @Transactional
    public Contrato cancelar(UUID id, String correlationId, String motivo) {
        log.info("contrato.cancelar.inicio correlationId={} contratoId={} motivo={}", correlationId, id, motivo);

        var contrato = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado: " + id));

        contrato.cancelar(correlationId, motivo);
        var salvo = repository.salvar(contrato);

        for (var event : contrato.pullDomainEvents()) {
            log.info("contrato.evento.publicacao correlationId={} contratoId={} eventId={} eventType={}",
                    correlationId, contrato.id(), event.eventId(), event.eventType());
            if (event instanceof ContratoEvent contratoEvent) {
                kafkaProducer.publish(contratoEvent);
            }
        }

        log.info("contrato.cancelar.sucesso correlationId={} contratoId={} status={}", correlationId, salvo.id(), salvo.status());
        return salvo;
    }

}