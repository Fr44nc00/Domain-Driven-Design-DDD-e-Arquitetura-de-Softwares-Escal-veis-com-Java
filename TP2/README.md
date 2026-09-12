# Domain-Driven-Design-DDD-e-Arquitetura-de-Softwares-Escal-veis-com-Java

---

## Parte teórica

---

### 1. Explique de forma sucinta qual a razão de criar Agregados.
Agregados organizam e encapsulam entidades e objetos de valor em torno de uma raiz. Eles garantem que as regras de negócio (invariantes) sejam respeitadas e que as operações sejam consistentes dentro de um limite bem definido. Isso evita dependências caóticas entre entidades e facilita manter a integridade dos dados.

---

### 2. O que significa “consistência transacional”?
Significa que todas as mudanças feitas dentro de uma transação devem ser aplicadas de forma atômica: ou todas acontecem, ou nenhuma acontece. Isso assegura que o sistema não fique em um estado intermediário inválido.

---

### 3. Cite as 4 propriedades cruciais que definem transações.
Atomicidade: tudo ou nada.

Consistência: garante que regras e restrições sejam respeitadas.

Isolamento: transações não interferem umas nas outras.

Durabilidade: uma vez confirmada, a transação persiste mesmo após falhas.

---

### 4. O que são “invariantes de negócio”?
São regras que sempre devem ser verdadeiras dentro de um agregado. Exemplo: um pedido não pode ter valor negativo; um estoque nunca pode ter quantidade menor que zero.

---

### 5. Porque um agregado só deve ter acesso a outro agregado pelo ID?
Para manter independência e baixo acoplamento. Se um agregado acessasse diretamente outro, poderia violar invariantes de negócio fora do seu limite. Usar apenas o ID garante que cada agregado seja responsável por sua própria consistência.

---

### 8. O que é “evento de domínio”?
É uma notificação emitida por um agregado quando algo relevante acontece no negócio. Ele descreve uma mudança de estado ou ação importante (ex.: “PedidoPagoEvent”) e permite que outros componentes reajam de forma desacoplada.

---

### 11. Qual é a diferença entre filas e tópicos e como estes elementos funcionam em conjunto?
Na Fila (Queue), cada mensagem é consumida por um único consumidor. Já no Tópico (Topic), cada mensagem é publicada para todos os assinantes interessados. É possível publicar eventos em um tópico, e cada serviço que assina esse tópico recebe uma cópia. Se quiser que apenas um serviço processe, usa fila.

---

### 12. Dê um exemplo de solução de arquitetura para publicação de eventos de domínio dentro do escopo do projeto Pet Friends (ideal um desenho).
No meu projeto, o fluxo poderia ser:

Pedido dispara um PedidoPagoEvent -> esse evento é publicado em um tópico (pelo Kafka ou pelo RabbitMQ) -> serviços como Estoque reagem ao evento.

---

### 13. Explique qual a finalidade de uma Event Store no contexto de eventos de domínio.
Uma Event Store é um repositório especializado para armazenar eventos de domínio. Ela guarda cada evento ocorrido, permitindo auditar o histórico completo, reconstituir o estado de um agregado a partir dos eventos e facilitar integrações e análises.

---

### 14. O que é Event Sourcing e como ele se diferencia da persistência tradicional em bancos de dados relacionais? Explique como os eventos salvos são usados para recuperar o estado atual de um Agregado.
A persistência tradicional salva apenas o estado atual (ex.: tabela de pedidos com status “PAGO”). Já o Event Sourcing salva todos os eventos que levaram ao estado atual (ex.: “PedidoCriado”, “ItemAdicionado”, “PedidoPago”). Para recuperar o estado de um agregado, se reaplica todos os eventos em ordem até chegar ao estado atual. Isso dá rastreabilidade e permite reconstruir qualquer ponto da história.
