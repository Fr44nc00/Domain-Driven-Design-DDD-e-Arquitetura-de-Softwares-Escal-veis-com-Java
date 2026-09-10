# Domain-Driven-Design-DDD-e-Arquitetura-de-Softwares-Escal-veis-com-Java

---

## Parte teórica

---

### 1 - Explique de forma sucinta o que são microsserviços.

Microsserviços são um estilo arquitetural em que uma aplicação é dividida em vários serviços pequenos, independentes e especializados, que se comunicam por APIs ou mensagens e podem ser desenvolvidos, implantados e escalados separadamente.

---

### 2 - Cite uma vantagem da arquitetura de microsserviços.

Uma vantagem dos microsserviços é a escalabilidade independente, permitindo aumentar recursos apenas dos serviços que possuem maior demanda, reduzindo custos e melhorando o desempenho.

---

### 3 - Cite uma desvantagem da arquitetura de microsserviços.

Uma desvantagem dos microsserviços é o aumento da complexidade operacional, pois é necessário gerenciar comunicação entre serviços, monitoramento, deploy e consistência dos dados distribuídos.

---

### 4 - Cite uma característica dos microsserviços que os diferenciam de outras arquiteturas de software.

Uma característica que diferencia os microsserviços é a autonomia dos serviços, já que cada um possui sua própria lógica de negócio, ciclo de vida e, geralmente, seu próprio banco de dados.

---

### 5 - Explique de forma sucinta o que é um monólito.

Um monólito é uma aplicação em que todas as funcionalidades são desenvolvidas e implantadas em uma única unidade executável, compartilhando código e recursos da mesma aplicação.

---

### 6 - Explique de forma sucinta o que significa Acoplamento do ponto de vista de Engenharia de Software.

Acoplamento é o grau de dependência entre módulos ou componentes de um sistema; quanto menor o acoplamento, mais independentes e fáceis de manter são os componentes.

---

### 7 - Explique de forma sucinta o que significa Coesão do ponto de vista de Engenharia de Software.

Coesão é o grau de relacionamento entre as responsabilidades de um módulo; quanto maior a coesão, mais focado e organizado é o componente em relação ao seu propósito.

---

### 8 - Explique de forma sucinta o que é um Agregado do DDD.

Um Agregado no DDD é um conjunto de objetos de domínio relacionados e tratados como uma única unidade de consistência, sendo controlados por uma Raiz de Agregado (Aggregate Root).

---

### 9 - Cite uma vantagem de construir microsserviços usando Agregados do DDD.

Uma vantagem de construir microsserviços usando Agregados do DDD é que os limites do domínio ficam mais claros, reduzindo acoplamentos e facilitando a autonomia de cada serviço.

---

### 10 - Dê um exemplo de mapeamento de um Contexto Delimitado para um microserviço, utilizando a linguagem Java.

Um exemplo de Contexto Delimitado mapeado para um microsserviço em Java é o contexto de Pagamento, implementado em um módulo próprio contendo classes como PagamentoService, Pagamento, ProcessadorCartao e PagamentoRepository.

---

### 11 - Dê um exemplo de mapeamento de um Agregado, com Objeto de Valor, para o contexto de um microserviço, utilizando a linguagem Java.

Um exemplo de Agregado com Objeto de Valor é o Agregado Pagamento, que utiliza os Value Objects Dinheiro, NumeroCartao e PagamentoId para representar conceitos do domínio com regras e validações próprias.

---

### 12 - Porque o compartilhamento de banco de dados é uma estratégia de integração ruim sob o ponto de vista de microsserviços?

O compartilhamento de banco de dados é uma estratégia ruim em microsserviços porque aumenta o acoplamento entre serviços, dificulta a autonomia das equipes e torna mudanças no esquema de dados mais arriscadas.

---

### 13 - Explique, com suas próprias palavras, qual estratégia de migração (como o padrão Strangler Fig / Padrão Estrangulador ou Branch by Abstraction) você utilizaria para iniciar a transformação desse monólito em microsserviços de forma segura, sem precisar desligar o sistema atual.

Para iniciar a migração desse monólito para microsserviços, eu utilizaria o padrão Strangler Fig (Padrão Estrangulador), extraindo gradualmente funcionalidades do sistema legado para novos serviços independentes, como o contexto de Pagamento, redirecionando as requisições aos poucos até que a funcionalidade antiga pudesse ser removida sem interromper a operação do sistema.
