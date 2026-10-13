package br.com.freela.notificacao.application;
import br.com.freela.notificacao.domain.model.Notificacao;
import br.com.freela.notificacao.domain.repository.NotificacaoRepository;
import br.com.freela.notificacao.infrastructure.messaging.ContratoEventDTO;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacaoService {
 private static final Logger log = LoggerFactory.getLogger(NotificacaoService.class);
 private final NotificacaoRepository repository;

 public NotificacaoService(NotificacaoRepository repository) {
  this.repository = repository;
 }

 @Transactional
 public void registrar(ContratoEventDTO evento) {
  log.info("notificacao.registro.inicio contratoId={} freelancerId={} tipo={}",
          evento.getContratoId(), evento.getFreelancerId(), evento.getEventType());

  var notificacao = new Notificacao(
          evento.getContratoId(),
          evento.getClienteId(),
          evento.getEventType(),
          "Contrato " + evento.getTitulo() + " - evento " + evento.getEventType()
  );

  var salvo = repository.save(notificacao);

  log.info("notificacao.registro.sucesso notificacaoId={} contratoId={} destinatarioId={}",
          salvo.getId(), evento.getContratoId(), evento.getClienteId());
 }
}

