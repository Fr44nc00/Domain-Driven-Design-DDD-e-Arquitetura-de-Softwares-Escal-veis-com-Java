package br.com.freela.contrato.infrastructure.web;

import br.com.freela.contrato.application.ContratoApplicationService;
import br.com.freela.contrato.application.CriarContratoCommand;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {
    private static final Logger log = LoggerFactory.getLogger(ContratoController.class);
    private final ContratoApplicationService service;
    public ContratoController(ContratoApplicationService service){this.service=service;}

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ContratoResponse criar(@RequestHeader(value="X-Correlation-Id", required=false) String correlationId,
                                   @Valid @RequestBody CriarContratoRequest request) {
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.criar correlationId={} clienteId={} freelancerId={}", correlationId, request.clienteId(), request.freelancerId());
        MDC.clear();
        var c = service.criar(new CriarContratoCommand(
                request.clienteId(),
                request.freelancerId(),
                request.titulo(),
                request.valor(),
                correlationId
        ));
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.criar.response correlationId={} contratoId={} status={}", correlationId, c.id(), c.status());
        MDC.clear();
        return ContratoResponse.from(c);
    }
    @GetMapping("/{id}")
    public ContratoResponse buscar(@RequestHeader(value="X-Correlation-Id", required=false) String correlationId, @PathVariable UUID id) {
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.buscar correlationId={} contratoId={}", correlationId, id);
        MDC.clear();
        return ContratoResponse.from(service.buscar(id));
    }
    @GetMapping
    public List<ContratoResponse> listar(@RequestHeader(value="X-Correlation-Id", required=false) String correlationId) {
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.listar correlationId={}", correlationId);
        MDC.clear();
        return service.listar().stream().map(ContratoResponse::from).toList();
    }

    @PatchMapping("/{id}/entrega")
    @ResponseStatus(HttpStatus.OK)
    public ContratoResponse registrarEntrega(
            @RequestHeader(value="X-Correlation-Id", required=false) String correlationId,
            @PathVariable("id") UUID id) {
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.entrega correlationId={} contratoId={}", correlationId, id);
        MDC.clear();
        var contrato = service.registrarEntrega(id, correlationId);
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.entrega.response correlationId={} contratoId={} status={}", correlationId, contrato.id(), contrato.status());
        MDC.clear();
        return ContratoResponse.from(contrato);
    }

    @PatchMapping("/{id}/concluir")
    @ResponseStatus(HttpStatus.OK)
    public ContratoResponse concluir(
            @RequestHeader(value="X-Correlation-Id", required=false) String correlationId,
            @PathVariable("id") UUID id) {
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.concluir correlationId={} contratoId={}", correlationId, id);
        MDC.clear();
        var contrato = service.concluir(id, correlationId);
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.concluir.response correlationId={} contratoId={} status={}", correlationId, contrato.id(), contrato.status());
        MDC.clear();
        return ContratoResponse.from(contrato);
    }

    @PatchMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.OK)
    public ContratoResponse cancelar(
            @RequestHeader(value="X-Correlation-Id", required=false) String correlationId,
            @PathVariable("id") UUID id,
            @RequestParam(required=false) String motivo) {
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.cancelar correlationId={} contratoId={} motivo={}", correlationId, id, motivo);
        MDC.clear();
        var contrato = service.cancelar(id, correlationId, motivo);
        MDC.put("correlationId", correlationId);
        log.info("http.contrato.cancelar.response correlationId={} contratoId={} status={}", correlationId, contrato.id(), contrato.status());
        MDC.clear();
        return ContratoResponse.from(contrato);
    }

}
