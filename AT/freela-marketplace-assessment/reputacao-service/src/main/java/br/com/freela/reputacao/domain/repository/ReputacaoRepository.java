package br.com.freela.reputacao.domain.repository;
import br.com.freela.reputacao.domain.model.ReputacaoFreelancer;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.UUID;
public interface ReputacaoRepository extends JpaRepository<ReputacaoFreelancer, UUID>{}
