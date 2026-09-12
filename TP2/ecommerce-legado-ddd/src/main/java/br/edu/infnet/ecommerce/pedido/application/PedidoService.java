package br.edu.infnet.ecommerce.pedido.application;

import br.edu.infnet.ecommerce.estoque.domain.Estoque;
import br.edu.infnet.ecommerce.estoque.infrastructure.EstoqueRepository;
import br.edu.infnet.ecommerce.pagamento.domain.model.FormaPagamento;
import br.edu.infnet.ecommerce.pagamento.domain.model.Pagamento;
import br.edu.infnet.ecommerce.pagamento.domain.model.StatusPagamento;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.NumeroCartao;
import br.edu.infnet.ecommerce.pedido.domain.ItemPedido;
import br.edu.infnet.ecommerce.pedido.domain.Pedido;
import br.edu.infnet.ecommerce.pedido.infrastructure.PedidoRepository;
import br.edu.infnet.ecommerce.produto.domain.Produto;
import br.edu.infnet.ecommerce.produto.infrastructure.ProdutoRepository;
import br.edu.infnet.ecommerce.pagamento.application.PagamentoService;
import br.edu.infnet.ecommerce.shared.event.KafkaEventPublisher;
import br.edu.infnet.ecommerce.shared.exception.EstoqueInsuficienteException;
import br.edu.infnet.ecommerce.shared.exception.PagamentoRecusadoException;
import br.edu.infnet.ecommerce.shared.exception.RecursoNaoEncontradoException;
import br.edu.infnet.ecommerce.pedido.api.CriarPedidoRequest;
import br.edu.infnet.ecommerce.pedido.api.ItemPedidoRequest;
import br.edu.infnet.ecommerce.usuario.domain.Usuario;
import br.edu.infnet.ecommerce.usuario.infrastructure.UsuarioRepository;
import br.edu.infnet.ecommerce.shared.event.PedidoPagoEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    /*
     * Classe central da atividade.
     *
     * Ela acessa diretamente repositórios de Usuário, Produto, Estoque,
     * Pedido e Pagamento, além de chamar PagamentoService.
     * Essa mistura é proposital.
     */
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;
    private final PedidoRepository pedidoRepository;
    private final PagamentoService pagamentoService;
    private final KafkaEventPublisher publisher;

    public PedidoService(
            UsuarioRepository usuarioRepository,
            ProdutoRepository produtoRepository,
            EstoqueRepository estoqueRepository,
            PedidoRepository pedidoRepository,
            PagamentoService pagamentoService,
            KafkaEventPublisher publisher
    ) {
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
        this.pedidoRepository = pedidoRepository;
        this.pagamentoService = pagamentoService;
        this.publisher = publisher;
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public Pedido buscar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido não encontrado: " + id
                ));
    }

    /*
     * Uma única transação envolve usuário, produto, estoque,
     * pedido e pagamento.
     */
    @Transactional
    public Pedido criar(CriarPedidoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado: " + request.usuarioId()
                ));

        if (!usuario.isAtivo()) {
            throw new IllegalArgumentException("Usuário inativo");
        }

        Pedido pedido = new Pedido(usuario);
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest itemRequest : request.itens()) {
            Produto produto = produtoRepository.findById(itemRequest.produtoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Produto não encontrado: " + itemRequest.produtoId()
                    ));

            if (!produto.isAtivo()) {
                throw new IllegalArgumentException(
                        "Produto inativo: " + produto.getNome()
                );
            }

            Estoque estoque = estoqueRepository.findByProdutoId(produto.getId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Estoque não encontrado para o produto: " + produto.getId()
                    ));

            if (estoque.getQuantidade() < itemRequest.quantidade()) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o produto: " + produto.getNome()
                );
            }

            // A baixa ocorre antes do pagamento.
            estoque.setQuantidade(
                    estoque.getQuantidade() - itemRequest.quantidade()
            );
            estoqueRepository.save(estoque);

            ItemPedido item = new ItemPedido(
                    produto,
                    itemRequest.quantidade(),
                    produto.getPreco()
            );

            pedido.adicionarItem(item);
            total = total.add(item.getSubtotal());
        }

        pedido.setValorTotal(total);
        pedido.setStatus("AGUARDANDO_PAGAMENTO");
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        Dinheiro valorPagamento = new Dinheiro(total);

        NumeroCartao numeroCartao =
                new NumeroCartao(
                        request.numeroCartao()
                );

        FormaPagamento formaPagamento =
                FormaPagamento.valueOf(
                        request.formaPagamento()
                );

        Pagamento pagamento =
                pagamentoService.processar(
                        pedidoSalvo.getId(),
                        usuario.getId(),
                        valorPagamento,
                        formaPagamento,
                        numeroCartao
                );

        if (pagamento.getStatus() != StatusPagamento.APROVADO) {

            pedidoSalvo.setStatus("PAGAMENTO_RECUSADO");
            pedidoRepository.save(pedidoSalvo);

            throw new PagamentoRecusadoException(
                    "Pagamento recusado: " + pagamento.getMotivo()
            );
        }

        pedidoSalvo.setStatus("PAGO");
        Pedido pedidoFinal = pedidoRepository.save(pedidoSalvo);

        PedidoPagoEvent event = new PedidoPagoEvent(pedidoFinal.getId());
        publisher.publishPedidoPago(event);

        return pedidoFinal;
    }
}
