package br.edu.infnet.ecommerce.estoque.application;

import br.edu.infnet.ecommerce.estoque.domain.Estoque;
import br.edu.infnet.ecommerce.pedido.domain.ItemPedido;
import br.edu.infnet.ecommerce.pedido.domain.Pedido;
import br.edu.infnet.ecommerce.pedido.infrastructure.PedidoRepository;
import br.edu.infnet.ecommerce.produto.domain.Produto;
import br.edu.infnet.ecommerce.shared.event.PedidoPagoEvent;
import br.edu.infnet.ecommerce.shared.exception.RecursoNaoEncontradoException;
import br.edu.infnet.ecommerce.estoque.infrastructure.EstoqueRepository;
import br.edu.infnet.ecommerce.produto.infrastructure.ProdutoRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public EstoqueService(
            EstoqueRepository estoqueRepository,
            ProdutoRepository produtoRepository,
            PedidoRepository pedidoRepository
    ) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Estoque> listar() {
        return estoqueRepository.findAll();
    }

    public Estoque buscarPorProduto(Long produtoId) {
        return estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estoque não encontrado para o produto: " + produtoId
                ));
    }

    @Transactional
    public Estoque definirQuantidade(Long produtoId, int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado: " + produtoId
                ));

        Estoque estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseGet(() -> new Estoque(produto, 0));

        estoque.setQuantidade(quantidade);
        return estoqueRepository.save(estoque);
    }

    @EventListener
    @Transactional
    public void onPedidoPago(PedidoPagoEvent event) {
        System.out.println("Evento recebido: Pedido " + event.getPedidoId() + " foi pago.");

        Pedido pedido = pedidoRepository.findById(event.getPedidoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido não encontrado: " + event.getPedidoId()
                ));

        for (ItemPedido item : pedido.getItens()) {
            Estoque estoque = estoqueRepository.findByProdutoId(item.getProduto().getId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Estoque não encontrado para o produto: " + item.getProduto().getId()
                    ));

            int novaQuantidade = estoque.getQuantidade() - item.getQuantidade();
            if (novaQuantidade < 0) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + item.getProduto().getId());
            }

            estoque.setQuantidade(novaQuantidade);
            estoqueRepository.save(estoque);
        }
    }
}
