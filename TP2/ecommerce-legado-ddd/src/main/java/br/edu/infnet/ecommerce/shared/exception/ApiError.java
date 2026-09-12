package br.edu.infnet.ecommerce.shared.exception;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String erro,
        String mensagem,
        LocalDateTime instante
) {
}
