package br.edu.infnet.ecommerce.pagamento.domain.model;

public record ResultadoProcessamento(
        boolean aprovado,
        StatusPagamento status,
        String motivo,
        String codigoAutorizacao
) {

    public static ResultadoProcessamento aprovado(
            String codigoAutorizacao) {

        return new ResultadoProcessamento(
                true,
                StatusPagamento.APROVADO,
                null,
                codigoAutorizacao
        );
    }

    public static ResultadoProcessamento recusado(
            String motivo) {

        return new ResultadoProcessamento(
                false,
                StatusPagamento.RECUSADO,
                motivo,
                null
        );
    }

    public static ResultadoProcessamento bloqueado(
            String motivo) {

        return new ResultadoProcessamento(
                false,
                StatusPagamento.BLOQUEADO,
                motivo,
                null
        );
    }
}