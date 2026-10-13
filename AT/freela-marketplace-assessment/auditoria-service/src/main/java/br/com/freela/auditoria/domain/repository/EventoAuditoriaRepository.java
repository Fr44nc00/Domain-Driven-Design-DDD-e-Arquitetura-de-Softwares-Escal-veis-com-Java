package br.com.freela.auditoria.domain.repository;
import br.com.freela.auditoria.domain.model.EventoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.UUID;
public interface EventoAuditoriaRepository extends JpaRepository<EventoAuditoria, UUID>{}
