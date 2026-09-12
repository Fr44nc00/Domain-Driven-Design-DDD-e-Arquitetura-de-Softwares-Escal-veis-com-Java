package br.edu.infnet.ecommerce.pagamento.domain.valueobject;

import java.math.BigDecimal;

public record Dinheiro(BigDecimal valor) {

    private static final BigDecimal LIMITE_PAGAMENTO =
            new BigDecimal("10000.00");

    public Dinheiro {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
    }

    public boolean menorOuIgualZero() {
        return valor.compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean excedeLimitePagamento() {
        return valor.compareTo(LIMITE_PAGAMENTO) > 0;
    }
}