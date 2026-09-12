package br.edu.infnet.ecommerce.pagamento.domain.valueobject;

public record NumeroCartao(String valor) {

    public NumeroCartao {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Número do cartão inválido");
        }

        if (valor.length() < 4) {
            throw new IllegalArgumentException("Número do cartão inválido");
        }
    }

    public boolean bloqueado() {
        return valor.endsWith("0000");
    }

    public String mascarado() {
        if (valor.length() < 4) {
            return "****";
        }

        String ultimos4 = valor.substring(valor.length() - 4);

        return "****-****-****-" + ultimos4;
    }
}