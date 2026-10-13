package br.com.freela.auditoria.infrastructure.web;
import br.com.freela.auditoria.domain.model.EventoAuditoria;
import br.com.freela.auditoria.domain.repository.EventoAuditoriaRepository;
import org.slf4j.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/auditoria")
public class AuditoriaController {
 private static final Logger log=LoggerFactory.getLogger(AuditoriaController.class); private final EventoAuditoriaRepository repository;
 AuditoriaController(EventoAuditoriaRepository r){this.repository=r;}
 @GetMapping public List<EventoAuditoria> listar(@RequestHeader(value="X-Correlation-Id",required=false) String correlationId){log.info("http.auditoria.listar correlationId={}",correlationId);return repository.findAll();}
}
