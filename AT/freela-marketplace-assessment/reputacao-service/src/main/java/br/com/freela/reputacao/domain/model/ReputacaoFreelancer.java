package br.com.freela.reputacao.domain.model;
import jakarta.persistence.*; import java.math.BigDecimal; import java.util.UUID;
@Entity @Table(name="reputacoes")
public class ReputacaoFreelancer {
 @Id UUID freelancerId; public int contratosConcluidos; public BigDecimal valorTotal=BigDecimal.ZERO;
 protected ReputacaoFreelancer(){} public ReputacaoFreelancer(UUID id){this.freelancerId=id;}
 public void registrarContrato(BigDecimal valor){contratosConcluidos++; valorTotal=valorTotal.add(valor);}
}
