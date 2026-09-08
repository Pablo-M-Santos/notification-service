# Notification Service

Serviço de notificações assíncrono, escalável e desacoplado, construído com Spring Boot e mensageria via RabbitMQ. Responsável por receber, processar e enviar notificações multi-canal com rastreabilidade completa.

## Funcionalidades

- Recebimento de notificações via API REST
- Envio assíncrono com filas RabbitMQ
- Rastreamento de status e tentativas de envio
- Persistência de usuários e histórico de notificações
- Suporte a múltiplos canais de notificação (ex.: Telegram)
- Validação de dados de entrada
- Documentação automática com Spring Actuator
- Migrations versionadas com Flyway

## Arquitetura

O projeto segue uma estrutura modular em camadas:

- **controller**: endpoints REST e validação HTTP
- **dto**: contratos de entrada e saída
- **domain**: regras e enums do negócio
- **entity**: modelos JPA
- **repository**: acesso a dados
- **service**: orquestração e regras de negócio
- **messaging**: produtor, consumidor e mensagens
- **provider**: integrações externas
- **config**: configurações da aplicação
- **exception**: tratamento centralizado de erros

Fluxo principal:
1. Cliente envia `POST /notifications`
2. Serviço persiste a notificação como `PENDING`
3. Evento é publicado na fila RabbitMQ
4. Consumidor processa a mensagem e envia pelo provider
5. Status é atualizado para `SENT` ou `FAILED`

## Stack Tecnológica

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring AMQP / RabbitMQ
- Flyway
- PostgreSQL
- Lombok
- Bean Validation
- Spring Actuator
- Maven
- Docker

## Como executar localmente

### Pré-requisitos

- Java 21
- Maven 3.9+
- PostgreSQL
- RabbitMQ

### Variáveis de ambiente

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=postgres
DB_USERNAME=postgres
DB_PASSWORD=postgres

TELEGRAM_BOT_TOKEN=seu_token
TELEGRAM_CHAT_ID=seu_chat_id

RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=admin
RABBITMQ_PASSWORD=admin
```

### Suba as dependências

```bash
docker compose up -d
```

### Execute a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação ficará disponível em `http://localhost:8080`.

## API

### Enviar notificação

```http
POST /notifications
Content-Type: application/json

{
  "externalId": "user-123",
  "title": "Bem-vindo",
  "message": "Olá, sua conta foi criada com sucesso.",
  "channel": "TELEGRAM"
}
```

### Buscar notificação

```http
GET /notifications/{id}
```

### Códigos HTTP

- `202 Accepted` - Notificação aceita para processamento
- `404 Not Found` - Notificação não encontrada
- `400 Bad Request` - Dados inválidos

## Banco de dados

Migrations versionadas localizadas em:
`src/main/resources/db/migration`

Versões incluídas:
- `V1` - Criação da tabela `notifications`
- `V2` - Adição de mensagem de erro em notificações
- `V3` - Criação da tabela `notification_attempts`
- `V4` - Criação de usuários e relacionamento com notificações
- `V5` - Índices de performance em notificações

## Docker

Build e execução completa com RabbitMQ embutido:

```bash
docker compose up --build
```

Acesse:
- API: `http://localhost:8080`
- Management RabbitMQ: `http://localhost:15672`

## Convenções

- Commits organizados por domínio
- Commits por bloco: migration, domain, dto, repository, service, messaging, infra
- Mensagens de commit no padrão: `tipo(escopo): descricao`
- Código limpo com Lombok e builders

## Status

Em desenvolvimento.
