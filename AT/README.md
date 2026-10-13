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

## Exercício 3 – Especificação das mensagens

### Objetivo
Documentar os eventos publicados pelo `contrato-service` e consumidos pelos serviços auxiliares.  
A especificação representa o contrato de comunicação entre os microsserviços.

---

### Eventos

#### 1. ContratoCriado
- **Nome do evento:** `ContratoCriado`  
- **Tópico Kafka:** `contratos-events`  
- **Serviço produtor:** `contrato-service`  
- **Serviços consumidores:** `notificacao-service`, `auditoria-service`  
- **Estrutura do payload:**
  ```json
  {
    "eventId": "uuid",
    "eventType": "ContratoCriado",
    "contratoId": "uuid",
    "correlationId": "string",
    "criadoEm": "2026-10-12T16:55:31Z",
    "clienteId": "uuid",
    "freelancerId": "uuid",
    "titulo": "string",
    "valor": 5000.00,
    "status": "ATIVO"
  }
  ```
- **Campos obrigatórios:** `eventId`, `eventType`, `contratoId`, `clienteId`, `freelancerId`, `titulo`, `valor`, `status`, `criadoEm`  
- **Chave de publicação:** `contratoId`  
- **Exemplo:**
  ```json
  {
    "eventId": "d2a40640-402a-4a0a-96c1-bd604c0cf8f0",
    "eventType": "ContratoCriado",
    "contratoId": "53ae7760-6654-4911-a973-8cd0617f8041",
    "correlationId": "teste-123",
    "criadoEm": "2026-10-12T16:55:31Z",
    "clienteId": "11111111-1111-1111-1111-111111111111",
    "freelancerId": "22222222-2222-2222-2222-222222222222",
    "titulo": "Desenvolvimento de API",
    "valor": 5000.00,
    "status": "ATIVO"
  }
  ```

---

#### 2. EntregaRegistrada
- **Nome do evento:** `EntregaRegistrada`  
- **Tópico Kafka:** `contratos-events`  
- **Serviço produtor:** `contrato-service`  
- **Serviços consumidores:** `notificacao-service`, `auditoria-service`  
- **Estrutura do payload:** igual ao `ContratoCriado`, mas com `eventType = "EntregaRegistrada"` e `status = "ENTREGA_REGISTRADA"`  
- **Campos obrigatórios:** mesmos do `ContratoCriado`  
- **Chave de publicação:** `contratoId`  
- **Exemplo:**
  ```json
  {
    "eventId": "uuid",
    "eventType": "EntregaRegistrada",
    "contratoId": "uuid",
    "correlationId": "teste-456",
    "criadoEm": "2026-10-12T17:00:00Z",
    "clienteId": "uuid",
    "freelancerId": "uuid",
    "titulo": "string",
    "valor": 3500.00,
    "status": "ENTREGA_REGISTRADA"
  }
  ```

---

#### 3. ContratoConcluido
- **Nome do evento:** `ContratoConcluido`  
- **Tópico Kafka:** `contratos-events`  
- **Serviço produtor:** `contrato-service`  
- **Serviços consumidores:** `notificacao-service`, `reputacao-service`, `auditoria-service`  
- **Estrutura do payload:** igual ao `ContratoCriado`, mas com `eventType = "ContratoConcluido"` e `status = "CONCLUIDO"`  
- **Campos obrigatórios:** mesmos do `ContratoCriado`  
- **Chave de publicação:** `contratoId`  
- **Exemplo:**
  ```json
  {
    "eventId": "uuid",
    "eventType": "ContratoConcluido",
    "contratoId": "uuid",
    "correlationId": "teste-789",
    "criadoEm": "2026-10-12T17:10:00Z",
    "clienteId": "uuid",
    "freelancerId": "uuid",
    "titulo": "string",
    "valor": 4500.00,
    "status": "CONCLUIDO"
  }
  ```

---

#### 4. ContratoCancelado
- **Nome do evento:** `ContratoCancelado`  
- **Tópico Kafka:** `contratos-events`  
- **Serviço produtor:** `contrato-service`  
- **Serviços consumidores:** `notificacao-service`, `auditoria-service`  
- **Estrutura do payload:** igual ao `ContratoCriado`, mas com `eventType = "ContratoCancelado"` e `status = "CANCELADO"`  
- **Campos obrigatórios:** mesmos do `ContratoCriado`  
- **Chave de publicação:** `contratoId`  
- **Exemplo:**
  ```json
  {
    "eventId": "uuid",
    "eventType": "ContratoCancelado",
    "contratoId": "uuid",
    "correlationId": "teste-999",
    "criadoEm": "2026-10-12T17:20:00Z",
    "clienteId": "uuid",
    "freelancerId": "uuid",
    "titulo": "string",
    "valor": 2000.00,
    "status": "CANCELADO"
  }
  ```

---

## Exercício 6 – Logs da aplicação

### Objetivo
Todos os serviços devem produzir logs suficientes para acompanhar o processamento das operações.  
Os logs devem conter informações de contexto como:
- nome do serviço  
- identificador do contrato (`contratoId`)  
- identificador do evento (`eventId`)  
- identificador de correlação (`correlationId`)  
- início e conclusão do processamento  
- falhas  
- mensagens recebidas e publicadas  

Informações sensíveis não devem ser registradas.

### Implementação
- Cada microsserviço utiliza **SLF4J + Logback**.  
- O padrão de log foi configurado para incluir os valores do **MDC (Mapped Diagnostic Context)**.  
- Nos pontos de entrada (controllers e consumidores Kafka), o `correlationId` é colocado no MDC.  
- Nos pontos de saída (application services e produtores Kafka), `contratoId` e `eventId` também são adicionados.  
- Ao final de cada operação, o MDC é limpo com `MDC.clear()`.

Exemplo de log:
```
2026-10-12 20:47:27 INFO  contrato-service - contratoId=fd32dca9 eventId=ba04ec7a correlationId=teste-001 contrato.evento.publicacao eventType=ContratoCriado
```

---

## Exercício 7 – Centralização de logs

### Objetivo
Os logs produzidos pelos microsserviços devem ser enviados para uma solução centralizada, permitindo pesquisar eventos e operações executadas em serviços diferentes utilizando informações em comum (`contratoId`, `eventId`, `correlationId`).

### Implementação
- Infraestrutura adicionada ao `docker-compose.yml`:  
  - **Grafana Loki** para armazenamento e indexação dos logs.  
  - **Promtail** para coleta dos logs dos containers Docker.  
  - **Grafana** para consulta e visualização.  
- Configuração do Promtail (`promtail-config.yml`) aponta para `/var/lib/docker/containers/*/*.log`.  
- Cada log enviado pelos serviços já contém os identificadores necessários para correlação.

### Consulta
No Grafana Explore, é possível pesquisar por qualquer identificador:
```
{job="docker-logs"} |= "contratoId=fd32dca9"
```

Isso retorna todos os logs relacionados ao contrato, vindos de diferentes serviços.

---

## Exercício 12 – Documentação da solução

### Visão geral da arquitetura
A solução foi construída com arquitetura de microsserviços em **Java + Spring Boot**, utilizando comunicação assíncrona via **Apache Kafka** e infraestrutura containerizada com **Docker Compose**.  
Os principais componentes são:
- **API Gateway**: ponto único de entrada HTTP, responsável pelo roteamento.  
- **Eureka Server**: registro e descoberta de serviços.  
- **Microsserviços**: contrato, notificação, reputação e auditoria.  
- **Infraestrutura**: PostgreSQL para persistência, Kafka para mensageria, Zipkin para tracing distribuído, Loki + Promtail + Grafana para centralização de logs.

---

### Serviços participantes
- **contrato-service**: núcleo do domínio, responsável pela criação e gestão de contratos.  
- **notificacao-service**: gera e armazena notificações relacionadas a eventos de contrato.  
- **reputacao-service**: atualiza a reputação dos freelancers com base em contratos concluídos.  
- **auditoria-service**: registra eventos relevantes para rastreabilidade.  
- **api-gateway**: expõe endpoints REST e encaminha requisições.  
- **eureka-server**: provê service discovery.

---

### Tópicos Kafka utilizados
- **contratos-events**: tópico principal para publicação de eventos de contrato.  
  - Produtor: contrato-service  
  - Consumidores: notificacao-service, reputacao-service, auditoria-service  

---

### Eventos existentes
- `ContratoCriado`  
- `EntregaRegistrada`  
- `ContratoConcluido`  
- `ContratoCancelado`  

---

### Formato das mensagens
Mensagens publicadas em JSON, com os seguintes campos:
- `eventId` (UUID)  
- `eventType` (string)  
- `contratoId` (UUID)  
- `correlationId` (string)  
- `criadoEm` (timestamp)  
- `clienteId` (UUID)  
- `freelancerId` (UUID)  
- `titulo` (string)  
- `valor` (decimal)  
- `status` (string)

---

### Estratégia de particionamento
- Particionamento por `contratoId` para garantir a **ordem dos eventos de um mesmo contrato**.

---

### Estratégia de idempotência
- Cada consumidor verifica se o `eventId` já foi processado antes de persistir, evitando duplicidade.

---

### Publicação transacional
- O contrato-service utiliza **Kafka Producer idempotente e transacional**, garantindo que a persistência no banco e a publicação no Kafka ocorram de forma atômica.

---

### Centralização de logs
- Logs enviados via **Promtail** para **Grafana Loki**.  
- Consultas realizadas no **Grafana Explore**, permitindo correlação por `contratoId`, `eventId` ou `correlationId`.

---

### Configuração utilizada para tracing
- Configuração em `application.yml`:
  ```yaml
  management:
      tracing:
        sampling:
          probability: 1.0
        export:
          zipkin:
            endpoint: http://localhost:9411/api/v2/spans
  ```

---

### Instruções para inicialização da infraestrutura
Na pasta `infra`:
```bash
docker compose up -d
```
Principais serviços:
- API Gateway → 8080  
- contrato-service → 8081  
- notificacao-service → 8082  
- reputacao-service → 8083  
- auditoria-service → 8084  
- Eureka Server → 8761  
- Kafka → 9092  
- Kafka UI → 8090  
- PostgreSQL → 5432  
- Zipkin → 9411  
- Grafana → 3000  

---

### Exemplos de chamadas para geração de eventos
- Criar contrato:
  ```bash
  curl -X POST http://localhost:8080/api/contratos \
    -H 'Content-Type: application/json' \
    -H 'X-Correlation-Id: teste-contrato-001' \
    -d '{
      "clienteId": "11111111-1111-1111-1111-111111111111",
      "freelancerId": "22222222-2222-2222-2222-222222222222",
      "titulo": "Construção de API de pagamentos",
      "valor": 3500.00
    }'
  ```

- Registrar entrega:
  ```bash
  curl -X PATCH http://localhost:8080/api/contratos/{id}/entrega \
    -H 'X-Correlation-Id: teste-contrato-001'
  ```

- Concluir contrato:
  ```bash
  curl -X PATCH http://localhost:8080/api/contratos/{id}/concluir \
    -H 'X-Correlation-Id: teste-contrato-001'
  ```

- Cancelar contrato:
  ```bash
  curl -X PATCH "http://localhost:8080/api/contratos/{id}/cancelar?motivo=Cliente desistiu" \
    -H 'X-Correlation-Id: teste-contrato-001'
  ```
