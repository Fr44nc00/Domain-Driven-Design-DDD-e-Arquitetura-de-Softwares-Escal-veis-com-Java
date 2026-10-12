# Domain-Driven-Design-DDD-e-Arquitetura-de-Softwares-Escal-veis-com-Java  --- AT

Projeto de referência para um marketplace de contratação de freelancers construído com arquitetura de microsserviços em Java e Spring.

A aplicação representa um cenário em que clientes contratam freelancers para a execução de trabalhos. O núcleo do sistema é o gerenciamento dos contratos firmados entre as partes. A partir desse domínio, outros serviços mantêm informações relacionadas a notificações, reputação e auditoria.

---

## Exercício 1 – Comunicação baseada em eventos

### Objetivo
O `contrato-service` deve publicar eventos relacionados às operações relevantes do domínio.  
A estrutura das mensagens deve permitir identificar:
- o evento
- o tipo do evento
- o contrato relacionado
- a data e hora em que ocorreu
- os dados necessários para processamento pelos consumidores
- informações de correlação para rastreamento

### Visão geral
O sistema é composto por seis aplicações Spring Boot:

- **eureka-server**: registro e descoberta dos serviços  
- **api-gateway**: ponto de entrada HTTP da aplicação  
- **contrato-service**: gerenciamento dos contratos  
- **notificacao-service**: armazenamento das notificações  
- **reputacao-service**: manutenção da reputação dos freelancers  
- **auditoria-service**: registro de eventos relevantes  

Infraestrutura: **PostgreSQL** e **Apache Kafka** via Docker Compose.

---

### Domínio
O domínio principal está no `contrato-service`.

Um contrato possui:
- identificador
- cliente
- freelancer
- título
- valor
- status
- data de criação

#### Estados
- ATIVO  
- ENTREGA_REGISTRADA  
- CONCLUIDO  
- CANCELADO  

Fluxo:
```
ATIVO → ENTREGA_REGISTRADA → CONCLUIDO
ATIVO → CANCELADO
```

---

### Eventos de domínio
Eventos implementados:
- `ContratoCriadoEvent`
- `EntregaRegistradaEvent`
- `ContratoConcluidoEvent`
- `ContratoCanceladoEvent`

#### Estrutura da mensagem
```json
{
  "eventId": "uuid",
  "eventType": "ContratoCriado",
  "contratoId": "uuid",
  "correlationId": "teste-123",
  "timestamp": "2026-10-12T01:14:59.838990Z",
  "clienteId": "11111111-1111-1111-1111-111111111111",
  "freelancerId": "22222222-2222-2222-2222-222222222222",
  "titulo": "Desenvolvimento de API",
  "valor": 5000.00,
  "status": "ATIVO"
}
```

---

### Kafka
- **Tópico:** `contratos-events`  
- **Produtor:** `KafkaContratoProducer`  
- **Consumidores esperados:** auditoria-service, notificacao-service, reputacao-service  
- **Chave de particionamento:** `contratoId`  
- **Formato:** JSON via Jackson  

---

### Recursos HTTP
- `POST /api/contratos` → criar contrato  
- `GET /api/contratos` → listar contratos  
- `GET /api/contratos/{id}` → buscar contrato  
- `PATCH /api/contratos/{id}/entrega` → registrar entrega  
- `PATCH /api/contratos/{id}/concluir` → concluir contrato  
- `PATCH /api/contratos/{id}/cancelar?motivo=...` → cancelar contrato  

---

### Infraestrutura local
Definida em `infra/docker-compose.yml`.

Para iniciar:
```bash
cd infra
docker compose up -d
```

Portas principais:
- API Gateway → 8080  
- contrato-service → 8081  
- notificacao-service → 8082  
- reputacao-service → 8083  
- auditoria-service → 8084  
- Eureka Server → 8761  
- Kafka → 9092  
- Kafka UI → 8090  
- PostgreSQL → 5432  

---

### Teste básico
Criar contrato:
```bash
curl -i -X POST http://localhost:8080/api/contratos \
  -H 'Content-Type: application/json' \
  -H 'X-Correlation-Id: teste-contrato-001' \
  -d '{
    "clienteId": "11111111-1111-1111-1111-111111111111",
    "freelancerId": "22222222-2222-2222-2222-222222222222",
    "titulo": "Construção de API de pagamentos",
    "valor": 3500.00
  }'
```

Registrar entrega:
```bash
curl -X PATCH http://localhost:8080/api/contratos/{id}/entrega \
  -H 'X-Correlation-Id: teste-contrato-001'
```

Concluir contrato:
```bash
curl -X PATCH http://localhost:8080/api/contratos/{id}/concluir \
  -H 'X-Correlation-Id: teste-contrato-001'
```

Cancelar contrato:
```bash
curl -X PATCH "http://localhost:8080/api/contratos/{id}/cancelar?motivo=Cliente desistiu" \
  -H 'X-Correlation-Id: teste-contrato-001'
```

---
