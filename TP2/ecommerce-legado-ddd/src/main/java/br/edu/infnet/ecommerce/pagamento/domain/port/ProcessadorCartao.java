package br.edu.infnet.ecommerce.pagamento.domain.port;

import br.edu.infnet.ecommerce.pagamento.domain.model.FormaPagamento;
import br.edu.infnet.ecommerce.pagamento.domain.model.ResultadoProcessamento;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.NumeroCartao;

public interface ProcessadorCartao {

    ResultadoProcessamento processar(
            Dinheiro valor,
            FormaPagamento formaPagamento,
            NumeroCartao numeroCartao
    );
}