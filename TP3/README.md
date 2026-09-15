# Domain-Driven-Design-DDD-e-Arquitetura-de-Softwares-Escal-veis-com-Java

---

## 1. Explique os padrões de comunicação utilizados em uma arquitetura de microsserviços.

Em arquiteturas de microsserviços, os serviços precisam trocar informações para executar processos de negócio. Os principais padrões de comunicação são:

1. Comunicação síncrona: um serviço envia uma requisição para outro e aguarda a resposta imediatamente.
2. Comunicação assíncrona: um serviço envia uma mensagem sem esperar uma resposta imediata.
3. Request/Response: geralmente implementado com HTTP/REST ou gRPC.
4. Publish/Subscribe (Pub/Sub): um serviço publica eventos e vários consumidores podem recebê-los.
5. Message Queue (Fila de Mensagens): mensagens são armazenadas em filas para processamento posterior.
6. Event-Driven Architecture (Arquitetura Orientada a Eventos): os microsserviços reagem a eventos gerados por outros serviços.
---

## 2. Qual a diferença entre a comunicação síncrona e a comunicação assíncrona em uma arquitetura de microsserviços?
Na comunicação síncrona, um serviço fica esperando a resposta de outro, sendo dependente, e normalmente se utiliza HTTP/REST ou gRPC. Na assíncrona, o serviço é independente do outro e geralmente utiliza filas, eventos ou brokers de mensagem.

---

## 3. Apresente um exemplo real da diferença da comunicação síncrona e assíncrona em uma arquitetura de microsserviços.

### Exemplo: Loja Virtual
Na comunicação síncrona, quando o cliente realiza uma compra, o serviço de Pedidos consulta o serviço de Estoque e aguarda a resposta. Se houver estoque disponível, a compra continua. Se o serviço de Estoque estiver indisponível, a operação pode falhar.

Agora, na comunicação assíncrona, após a compra, o serviço de Pedidos publica o evento "PedidoCriado", os serviços de Estoque, Pagamento e Notificação recebem o evento e cada serviço processa a informação de forma independente. Mesmo que um serviço esteja temporariamente indisponível, a mensagem permanece disponível para ser processada.

---

## 4. Explique os tipos de comunicação assíncrona em uma arquitetura de microsserviços.

### Filas de Mensagens (Queue)
1. Cada mensagem é consumida por apenas um consumidor.
2. Adequado para processamento de tarefas.

### Publicação e Assinatura (Publish/Subscribe)
1. Uma mensagem pode ser enviada para vários consumidores.
2. Ideal para disseminação de eventos.

### Event Streaming
1. Eventos são armazenados em sequência.
2. Consumidores podem ler os eventos posteriormente.
3. Muito utilizado com Apache Kafka.

---

## 5. Explique a comunicação assíncrona em uma arquitetura de microsserviços utilizando o padrão de mensagens.

No padrão de mensagens , o serviço de Pedidos cria um pedido (mensagem), envia ele para uma fila e outros serviços processam a mensagem quando estiverem disponíveis. Isso reduz o acoplamento entre os serviços e aumenta a escalabilidade do sistema.

---

## 6. Explique a comunicação assíncrona em uma arquitetura de microsserviços utilizando um message broker.


---

## 7. O que é “evento de domínio”?
É uma notificação emitida por um agregado quando algo relevante acontece no negócio. Ele descreve uma mudança de estado ou ação importante (ex.: “PedidoPagoEvent”) e permite que outros componentes reajam de forma desacoplada.

---

## 8. O que é “evento de domínio”?
É uma notificação emitida por um agregado quando algo relevante acontece no negócio. Ele descreve uma mudança de estado ou ação importante (ex.: “PedidoPagoEvent”) e permite que outros componentes reajam de forma desacoplada.

---

## 9. O que é “evento de domínio”?
É uma notificação emitida por um agregado quando algo relevante acontece no negócio. Ele descreve uma mudança de estado ou ação importante (ex.: “PedidoPagoEvent”) e permite que outros componentes reajam de forma desacoplada.

---

## 10. O que é “evento de domínio”?
É uma notificação emitida por um agregado quando algo relevante acontece no negócio. Ele descreve uma mudança de estado ou ação importante (ex.: “PedidoPagoEvent”) e permite que outros componentes reajam de forma desacoplada.

---

## 11. Qual é a diferença entre filas e tópicos e como estes elementos funcionam em conjunto?
Na Fila (Queue), cada mensagem é consumida por um único consumidor. Já no Tópico (Topic), cada mensagem é publicada para todos os assinantes interessados. É possível publicar eventos em um tópico, e cada serviço que assina esse tópico recebe uma cópia. Se quiser que apenas um serviço processe, usa fila.

---

## 12. Dê um exemplo de solução de arquitetura para publicação de eventos de domínio dentro do escopo do projeto Pet Friends (ideal um desenho).
No meu projeto, o fluxo poderia ser:

Pedido dispara um PedidoPagoEvent -> esse evento é publicado em um tópico (pelo Kafka ou pelo RabbitMQ) -> serviços como Estoque reagem ao evento.

---

## 13. Explique qual a finalidade de uma Event Store no contexto de eventos de domínio.
Uma Event Store é um repositório especializado para armazenar eventos de domínio. Ela guarda cada evento ocorrido, permitindo auditar o histórico completo, reconstituir o estado de um agregado a partir dos eventos e facilitar integrações e análises.

---

## 14. O que é Event Sourcing e como ele se diferencia da persistência tradicional em bancos de dados relacionais? Explique como os eventos salvos são usados para recuperar o estado atual de um Agregado.
A persistência tradicional salva apenas o estado atual (ex.: tabela de pedidos com status “PAGO”). Já o Event Sourcing salva todos os eventos que levaram ao estado atual (ex.: “PedidoCriado”, “ItemAdicionado”, “PedidoPago”). Para recuperar o estado de um agregado, se reaplica todos os eventos em ordem até chegar ao estado atual. Isso dá rastreabilidade e permite reconstruir qualquer ponto da história.
