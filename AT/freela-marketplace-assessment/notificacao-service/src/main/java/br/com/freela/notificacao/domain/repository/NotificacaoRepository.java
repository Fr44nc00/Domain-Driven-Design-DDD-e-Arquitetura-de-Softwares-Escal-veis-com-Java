package br.com.freela.notificacao.domain.repository;
import br.com.freela.notificacao.domain.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.UUID;
public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID>{}
