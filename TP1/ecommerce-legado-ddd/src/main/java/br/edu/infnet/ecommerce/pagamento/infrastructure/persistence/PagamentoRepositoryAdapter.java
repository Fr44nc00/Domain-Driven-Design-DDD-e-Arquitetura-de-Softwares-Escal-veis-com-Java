package br.edu.infnet.ecommerce.pagamento.infrastructure.persistence;

import br.edu.infnet.ecommerce.pagamento.domain.model.Pagamento;
import br.edu.infnet.ecommerce.pagamento.domain.port.PagamentoRepository;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.PagamentoId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PagamentoRepositoryAdapter
        implements PagamentoRepository {

    private final JpaPagamentoRepository repository;

    public PagamentoRepositoryAdapter(
            JpaPagamentoRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Pagamento save(Pagamento pagamento) {

        PagamentoJpaEntity entity = new PagamentoJpaEntity();

        entity.setPedidoId(pagamento.getPedidoId());
        entity.setUsuarioId(pagamento.getUsuarioId());
        entity.setValor(pagamento.getValor().valor());
        entity.setFormaPagamento(pagamento.getFormaPagamento());
        entity.setStatus(pagamento.getStatus());
        entity.setNumeroCartaoMascarado(pagamento.getNumeroCartaoMascarado());
        entity.setMotivo(pagamento.getMotivo());
        entity.setCodigoAutorizacao(pagamento.getCodigoAutorizacao());
        entity.setProcessadoEm(pagamento.getProcessadoEm());

        entity = repository.save(entity);

        pagamento.setId(new PagamentoId(entity.getId()));

        return pagamento;
    }

    @Override
    public Optional<Pagamento> findByPedidoId(Long pedidoId) {

        return repository.findByPedidoId(pedidoId)
                .map(this::toDomain);
    }

    private Pagamento toDomain(
            PagamentoJpaEntity entity
    ) {

        Pagamento pagamento = new Pagamento();

        pagamento.setId(new PagamentoId(entity.getId()));
        pagamento.setPedidoId(entity.getPedidoId());
        pagamento.setUsuarioId(entity.getUsuarioId());
        pagamento.setValor(new Dinheiro(entity.getValor()));
        pagamento.setFormaPagamento(entity.getFormaPagamento());
        pagamento.setStatus(entity.getStatus());
        pagamento.setNumeroCartaoMascarado(entity.getNumeroCartaoMascarado());
        pagamento.setMotivo(entity.getMotivo());
        pagamento.setCodigoAutorizacao(entity.getCodigoAutorizacao());
        pagamento.setProcessadoEm(entity.getProcessadoEm());

        return pagamento;
    }
}