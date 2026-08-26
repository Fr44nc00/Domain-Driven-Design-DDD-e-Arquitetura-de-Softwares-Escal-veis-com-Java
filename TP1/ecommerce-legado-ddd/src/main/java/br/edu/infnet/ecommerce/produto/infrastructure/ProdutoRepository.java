package br.edu.infnet.ecommerce.produto.infrastructure;

import br.edu.infnet.ecommerce.produto.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
