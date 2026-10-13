package br.com.freela.reputacao.infrastructure.web;
import br.com.freela.reputacao.domain.model.ReputacaoFreelancer;
import br.com.freela.reputacao.domain.repository.ReputacaoRepository;
import org.slf4j.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/reputacoes")
public class ReputacaoController {
 private static final Logger log=LoggerFactory.getLogger(ReputacaoController.class); private final ReputacaoRepository repository;
 ReputacaoController(ReputacaoRepository r){this.repository=r;}
 @GetMapping public List<ReputacaoFreelancer> listar(@RequestHeader(value="X-Correlation-Id",required=false) String correlationId){log.info("http.reputacao.listar correlationId={}",correlationId);return repository.findAll();}
}
