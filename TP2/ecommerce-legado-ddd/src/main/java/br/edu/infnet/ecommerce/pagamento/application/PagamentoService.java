package br.edu.infnet.ecommerce.pagamento.application;

import br.edu.infnet.ecommerce.pagamento.domain.model.FormaPagamento;
import br.edu.infnet.ecommerce.pagamento.domain.model.Pagamento;
import br.edu.infnet.ecommerce.pagamento.domain.model.ResultadoProcessamento;
import br.edu.infnet.ecommerce.pagamento.domain.port.PagamentoRepository;
import br.edu.infnet.ecommerce.pagamento.domain.port.PedidoGateway;
import br.edu.infnet.ecommerce.pagamento.domain.port.ProcessadorCartao;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.NumeroCartao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ProcessadorCartao processadorCartao;
    private final PedidoGateway pedidoGateway;

    public PagamentoService(
            PagamentoRepository pagamentoRepository,
            ProcessadorCartao processadorCartao,
            PedidoGateway pedidoGateway
    ) {
        this.pagamentoRepository = pagamentoRepository;
        this.processadorCartao = processadorCartao;
        this.pedidoGateway = pedidoGateway;
    }

    public Pagamento processar(
            Long pedidoId,
            Long usuarioId,
            Dinheiro valor,
            FormaPagamento formaPagamento,
            NumeroCartao numeroCartao
    ) {

        ResultadoProcessamento resultado =
                processadorCartao.processar(
                        valor,
                        formaPagamento,
                        numeroCartao
                );

        Pagamento pagamento = new Pagamento();

        pagamento.setPedidoId(pedidoId);
        pagamento.setUsuarioId(usuarioId);
        pagamento.setValor(valor);
        pagamento.setFormaPagamento(formaPagamento);

        pagamento.setNumeroCartaoMascarado(numeroCartao.mascarado());

        pagamento.setStatus(resultado.status());
        pagamento.setMotivo(resultado.motivo());
        pagamento.setCodigoAutorizacao(resultado.codigoAutorizacao());
        pagamento.setProcessadoEm(LocalDateTime.now());

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        pedidoGateway.atualizarStatusPagamento(
                pedidoId,
                resultado.status()
        );

        return pagamentoSalvo;
    }
}