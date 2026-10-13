package br.com.freela.auditoria.domain.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auditoria_eventos")
public class EventoAuditoria {

 @Id
 @Column(nullable = false, updatable = false)
 private UUID id;

 @Column(nullable = false)
 private UUID eventId;

 @Column(nullable = false)
 private UUID aggregateId;

 @Column(nullable = false)
 private String eventType;

 private String correlationId;

 @Column(columnDefinition = "text", nullable = false)
 private String payload;

 @Column(nullable = false)
 private Instant recebidoEm;

 protected EventoAuditoria() {}

 public EventoAuditoria(UUID eventId,
                        UUID aggregateId,
                        String eventType,
                        String correlationId,
                        String payload) {
  this.id = UUID.randomUUID();
  this.eventId = eventId;
  this.aggregateId = aggregateId;
  this.eventType = eventType;
  this.correlationId = correlationId;
  this.payload = payload;
  this.recebidoEm = Instant.now();
 }

 public UUID getId() { return id; }
 public UUID getEventId() { return eventId; }
 public UUID getAggregateId() { return aggregateId; }
 public String getEventType() { return eventType; }
 public String getCorrelationId() { return correlationId; }
 public String getPayload() { return payload; }
 public Instant getRecebidoEm() { return recebidoEm; }
}
