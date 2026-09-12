package br.edu.infnet.ecommerce.estoque.controller;

import br.edu.infnet.ecommerce.estoque.domain.Estoque;
import br.edu.infnet.ecommerce.estoque.api.AjusteEstoqueRequest;
import br.edu.infnet.ecommerce.estoque.application.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public List<Estoque> listar() {
        return estoqueService.listar();
    }

    @GetMapping("/produto/{produtoId}")
    public Estoque buscarPorProduto(@PathVariable Long produtoId) {
        return estoqueService.buscarPorProduto(produtoId);
    }

    @PutMapping("/produto/{produtoId}")
    public Estoque definirQuantidade(
            @PathVariable Long produtoId,
            @Valid @RequestBody AjusteEstoqueRequest request
    ) {
        return estoqueService.definirQuantidade(
                produtoId,
                request.quantidade()
        );
    }
}
