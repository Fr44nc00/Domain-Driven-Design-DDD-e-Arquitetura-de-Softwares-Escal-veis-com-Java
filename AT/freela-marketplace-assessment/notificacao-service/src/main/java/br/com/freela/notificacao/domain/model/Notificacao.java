package br.com.freela.notificacao.domain.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notificacoes")
public class Notificacao {
 @Id
 private UUID id;

 @Column(nullable = false)
 private UUID contratoId;

 @Column(nullable = false)
 private UUID destinatarioId;

 @Column(nullable = false)
 private String tipo;

 @Column(length = 500, nullable = false)
 private String mensagem;

 @Column(nullable = false)
 private Instant criadaEm;

 protected Notificacao() {}

 public Notificacao(UUID contratoId, UUID destinatarioId, String tipo, String mensagem) {
  this.id = UUID.randomUUID();
  this.contratoId = contratoId;
  this.destinatarioId = destinatarioId;
  this.tipo = tipo;
  this.mensagem = mensagem;
  this.criadaEm = Instant.now();
 }

 public UUID getId() { return id; }
 public UUID getContratoId() { return contratoId; }
 public UUID getDestinatarioId() { return destinatarioId; }
 public String getTipo() { return tipo; }
 public String getMensagem() { return mensagem; }
 public Instant getCriadaEm() { return criadaEm; }
}
