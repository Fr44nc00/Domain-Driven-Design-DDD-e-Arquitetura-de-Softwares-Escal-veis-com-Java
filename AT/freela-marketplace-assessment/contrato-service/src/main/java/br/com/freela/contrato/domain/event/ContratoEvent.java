package br.com.freela.contrato.domain.event;

import br.com.freela.contrato.domain.shared.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public abstract class ContratoEvent implements DomainEvent {
    private final UUID eventId;
    private final Instant occurredAt;
    private final String eventType;
    private final UUID contratoId;
    private final String correlationId;

    protected ContratoEvent(UUID contratoId, String eventType, String correlationId) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.eventType = eventType;
        this.contratoId = contratoId;
        this.correlationId = correlationId;
    }

    @Override public UUID eventId() { return eventId; }
    @Override public Instant occurredAt() { return occurredAt; }
    @Override public String eventType() { return eventType; }

    public UUID contratoId() { return contratoId; }
    public String correlationId() { return correlationId; }
}
