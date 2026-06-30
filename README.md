# Restaurante API

API REST feita em Java com Spring Boot para gerenciar o funcionamento de um restaurante: mesas, cardápio, pedidos, fluxo de cozinha, fechamento de conta e pagamento. Fiz esse projeto pra praticar arquitetura backend de verdade, separando bem as camadas e lidando com integração entre serviços.

## O que a API faz

A ideia é simular o dia a dia de um restaurante:

- Cadastro de mesas e controle do status de cada uma (livre, ocupada, reservada, inativa)
- Cadastro de produtos e categorias do cardápio
- Abertura de pedidos vinculados a uma mesa, com adição de itens
- Fluxo de cozinha: os itens passam por pendente, em preparo, pronto e entregue, cada etapa com o respectivo controller
- Fechamento de conta de um pedido, calculando subtotal, taxa de serviço, desconto e total
- Pagamento do pedido, feito através de uma chamada para um serviço externo de pagamento (via Feign). Se o pagamento for aprovado, o pedido é fechado e a mesa volta a ficar livre automaticamente
- Um worker rodando em background a cada minuto, usando virtual threads, que verifica se algum item da cozinha está demorando mais do que o tempo de preparo esperado e dispara um alerta

Para testar a parte de pagamento é necessário rodar num projeto separado a API mock que simula o serviço de pagamento, disponível aqui:

https://github.com/Ighor-A-Oliveira/Restaurante-API-PagamentoFake

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Spring Web
- Spring Validation
- Spring Cloud OpenFeign (integração com o serviço de pagamento)
- Flyway (versionamento e criação do schema do banco)
- H2 (banco usado no ambiente atual, em memória)
- PostgreSQL (driver já incluso no projeto, pra uso em produção)
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Maven

## Endpoints

### Produtos (`/produtos`)

| Método | Rota | Descrição |
|---|---|---|
| POST | /produtos | Cadastra um novo produto |
| GET | /produtos | Lista produtos (paginado) |
| GET | /produtos/{id} | Busca um produto por id |
| PUT | /produtos/{id} | Atualiza um produto |
| DELETE | /produtos/{id} | Remove um produto |

### Pedidos (`/pedidos`)

| Método | Rota | Descrição |
|---|---|---|
| POST | /pedidos | Abre um novo pedido para uma mesa |
| GET | /pedidos | Lista pedidos (paginado) |
| GET | /pedidos/{id} | Busca um pedido por id |
| POST | /pedidos/{pedidoId}/itens | Adiciona um item ao pedido |
| GET | /pedidos/{pedidoId}/itens | Lista os itens de um pedido |
| POST | /pedidos/{pedidoId}/pagar | Processa o pagamento do pedido |

### Fechamento de conta (`/pedidos/{pedidoId}/fechamento`)

| Método | Rota | Descrição |
|---|---|---|
| POST | /pedidos/{pedidoId}/fechamento | Fecha a conta do pedido (calcula subtotal, taxa e total) |
| GET | /pedidos/{pedidoId}/fechamento | Consulta o fechamento de um pedido |

### Cozinha (`/cozinha`)

| Método | Rota | Descrição |
|---|---|---|
| GET | /cozinha/itens-pendentes | Lista itens pendentes de preparo |
| GET | /cozinha/itens-em-preparo | Lista itens que já estão em preparo |
| PATCH | /cozinha/itens/{itemId}/iniciar-preparo | Marca o item como em preparo |
| PATCH | /cozinha/itens/{itemId}/marcar-pronto | Marca o item como pronto |
| PATCH | /cozinha/itens/{itemId}/entregar | Marca o item como entregue |

## Documentação interativa (Swagger / OpenAPI)

O projeto usa o Springdoc OpenAPI, então com a aplicação rodando dá pra explorar e testar todos os endpoints direto pelo navegador, sem precisar de Postman ou Insomnia. A interface do Swagger fica disponível em:

```
http://localhost:8080/swagger-ui.html
```

E o JSON da especificação OpenAPI fica em:

```
http://localhost:8080/v3/api-docs
```

## Estrutura do projeto

O código segue a divisão padrão em camadas:

- `controller`: endpoints REST (produtos, pedidos, cozinha, fechamento de conta)
- `service`: regras de negócio (PedidoService, ProdutoService, CozinhaService, FechamentoContaService, PagamentoService)
- `repository`: acesso a dados via Spring Data JPA
- `domain/entity`: entidades (Mesa, Produto, CategoriaProduto, Pedido, PedidoItem, FechamentoConta, Pagamento)
- `domain/enums`: status e enumerações usadas nas entidades
- `dto`: objetos de request e response
- `client`: cliente Feign que conversa com a API externa de pagamento
- `worker`: tarefa agendada que monitora itens atrasados na cozinha
- `exception`: tratamento global de exceções

As migrações do banco ficam em `src/main/resources/db/migration`, já com um schema inicial e alguns dados de exemplo (mesas e produtos) pra facilitar os testes.

## Como rodar o projeto

1. Clone os repositórios em projetos diferentes:

```
git clone https://github.com/Ighor-A-Oliveira/Restaurante-API.git
```

2. Rode a aplicação com o Maven Wrapper:

```
./mvnw spring-boot:run
```

Por padrão a aplicação sobe usando o H2 em memória, então não precisa configurar nenhum banco externo pra testar. O console do H2 fica disponível em `/h2-console`.

3. Se quiser testar o fluxo de pagamento, suba também a API mock de pagamento (link acima) e configure a porta dela em `pagamento.api.url`, no `application.properties`.

4. Com a aplicação no ar, acesse o Swagger UI pra ver e testar todos os endpoints listados acima.

## Observações

Esse projeto ainda está em evolução, é um espaço onde venho aplicando e testando conceitos enquanto estudo Spring e arquitetura de APIs. Sugestões e feedbacks são sempre bem-vindos.

## Autor

Ighor Oliveira
GitHub: https://github.com/Ighor-A-Oliveira

