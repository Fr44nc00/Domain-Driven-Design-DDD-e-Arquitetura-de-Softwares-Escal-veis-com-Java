# Domain-Driven-Design-DDD-e-Arquitetura-de-Softwares-Escal-veis-com-Java  --- AT

---

# 1. Comunicação baseada em eventos – Contrato Service

## Tópicos Kafka definidos

contrato-criado

contrato-concluido

contrato-cancelado

Cada tópico corresponde a um evento relevante no ciclo de vida de um contrato.

---

## Produtor

Serviço: contrato-service

Responsabilidade: publicar eventos de domínio sempre que um contrato é criado, concluído ou cancelado.

Chave de particionamento: contratoId  
→ garante que todos os eventos relacionados ao mesmo contrato sejam roteados para a mesma partição.

---

## Consumidores

### notificacao-service

Consome contrato-criado e contrato-concluido

Responsável por enviar notificações a clientes e freelancers.

### reputacao-service

Consome contrato-concluido

Atualiza a reputação do freelancer com base em contratos finalizados.

### auditoria-service

Consome todos os eventos (contrato-criado, contrato-concluido, contrato-cancelado)

Mantém trilha de auditoria para rastreabilidade e compliance.

---

## Estrutura da mensagem

### Formato JSON serializado pelo KafkaJsonSerializer:

```json
{
  "eventId": "UUID",
  "occurredAt": "2026-10-10T19:33:00Z",
  "eventType": "CONTRATO_CRIADO",
  "contratoId": "UUID",
  "data": {
    "clienteId": "UUID",
    "freelancerId": "UUID",
    "titulo": "string",
    "valor": 1000.00
  },
  "correlationId": "UUID"
}
```

### Campos obrigatórios

eventId → identificador único do evento

occurredAt → data/hora da ocorrência

eventType → tipo do evento (CONTRATO_CRIADO, CONTRATO_CONCLUIDO, CONTRATO_CANCELADO)

contratoId → contrato relacionado

data → informações adicionais necessárias para processamento

correlationId → rastreamento da operação

---

## Estratégia de publicação

Eventos são criados no aggregate Contrato.

O ContratoApplicationService coleta os eventos via contrato.pullDomainEvents() e publica no Kafka.

Publicação é feita de forma transacional: primeiro persiste no banco, depois envia ao Kafka.

---

# 2. Integração dos serviços – Consumidores Kafka

## notificacao-service

Tópicos consumidos: contrato-criado, contrato-concluido
Responsabilidade: registrar notificações para clientes e freelancers.

### Fluxo:

O NotificacaoConsumer recebe o evento via @KafkaListener.

O evento é transformado em ContratoEvent (DTO local).

O NotificacaoService persiste a notificação no banco.

### Logs registrados:

Evento recebido

Contrato associado

Destinatário

Resultado do processamento

---

## reputacao-service

Tópico consumido: contrato-concluido
Responsabilidade: atualizar reputação do freelancer com base em contratos finalizados.

### Fluxo:

O ReputacaoConsumer recebe o evento via @KafkaListener.

O evento é transformado em ContratoEvent.

O ReputacaoService atualiza ou cria o registro de reputação do freelancer.

### Logs registrados:

Evento recebido

Contrato associado

Freelancer

Resultado da atualização

---

## auditoria-service

Tópicos consumidos: contrato-criado, contrato-concluido, contrato-cancelado
Responsabilidade: manter trilha de auditoria completa para rastreabilidade e compliance.

### Fluxo:

O AuditoriaConsumer recebe o evento via @KafkaListener.

O evento é transformado em ContratoEvent.

O AuditoriaService persiste os dados no banco (EventoAuditoria).

### Logs registrados:

Identificador do evento

Tipo do evento

Contrato relacionado

Data e hora

CorrelationId

Conteúdo relevante da mensagem

---

## Estrutura comum dos eventos (ContratoEvent)

Cada serviço define localmente um DTO ContratoEvent que reflete o JSON publicado pelo contrato-service:

```json
{
  "eventId": "UUID",
  "occurredAt": "2026-10-10T19:33:00Z",
  "eventType": "CONTRATO_CONCLUIDO",
  "contratoId": "UUID",
  "data": {
    "clienteId": "UUID",
    "freelancerId": "UUID",
    "titulo": "string",
    "valor": 1000.00
  },
  "correlationId": "UUID"
}
```

---

