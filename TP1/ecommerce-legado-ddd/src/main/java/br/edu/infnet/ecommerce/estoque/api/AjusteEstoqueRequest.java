package br.edu.infnet.ecommerce.estoque.api;

import jakarta.validation.constraints.NotNull;

public record AjusteEstoqueRequest(
        @NotNull Integer quantidade
) {
}
