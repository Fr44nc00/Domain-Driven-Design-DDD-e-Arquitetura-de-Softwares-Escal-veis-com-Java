package br.edu.infnet.ecommerce.pagamento.infrastructure.payment;

import br.edu.infnet.ecommerce.pagamento.domain.model.FormaPagamento;
import br.edu.infnet.ecommerce.pagamento.domain.model.ResultadoProcessamento;
import br.edu.infnet.ecommerce.pagamento.domain.port.ProcessadorCartao;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.NumeroCartao;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProcessadorPagamento implements ProcessadorCartao {

    public ResultadoProcessamento processar(
            Dinheiro valor,
            FormaPagamento formaPagamento,
            NumeroCartao numeroCartao
    ) {

        if (valor == null || valor.menorOuIgualZero()) {
            return ResultadoProcessamento.recusado(
                    "VALOR_INVALIDO"
            );
        } else if (formaPagamento != FormaPagamento.CARTAO) {
            return ResultadoProcessamento.recusado(
                    "FORMA_PAGAMENTO_NAO_SUPORTADA"
            );
        } else if (valor.excedeLimitePagamento()) {
            return ResultadoProcessamento.recusado(
                    "LIMITE_EXCEDIDO"
            );
        } else if (numeroCartao.bloqueado()) {
            return ResultadoProcessamento.bloqueado(
                    "CARTAO_BLOQUEADO"
            );
        } else {
            return ResultadoProcessamento.aprovado(
                    gerarCodigoAutorizacao()
            );
        }
    }

    private String gerarCodigoAutorizacao() {
        return UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}