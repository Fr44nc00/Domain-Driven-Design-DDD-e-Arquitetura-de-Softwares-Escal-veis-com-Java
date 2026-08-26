package br.edu.infnet.ecommerce.pagamento.domain.valueobject;

public record PagamentoId(Long valor) {

    public PagamentoId {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("Id do pagamento inválido");
        }
    }
}