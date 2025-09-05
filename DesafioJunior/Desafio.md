# Desafio Técnico - Sistema de Gerenciamento de Biblioteca

## 📋 Contexto
Você foi contratado para desenvolver um sistema de gerenciamento de biblioteca que permitirá o controle de livros, usuários e empréstimos. O sistema deve ser uma API REST que facilite as operações básicas de uma biblioteca.

## 🎯 Objetivos Obrigatórios

### 1. Configuração do Projeto
- Criar um projeto Spring Boot utilizando Java 21+
- Configurar um banco de dados relacional (H2 para testes ou PostgreSQL/MySQL)
- Implementar versionamento com Git
- Criar um arquivo README.md com instruções de execução

### 2. Modelagem de Dados
Criar as seguintes entidades com seus relacionamentos:

**Livro (Book)**
- ID (Long)
- Título (String)
- Autor (String)
- ISBN (String, único)
- Ano de Publicação (Integer)
- Quantidade Total (Integer)
- Quantidade Disponível (Integer)
- Status (Enum: DISPONIVEL, INDISPONIVEL)

**Usuário (User)**
- ID (Long)
- Nome (String)
- Email (String, único)
- CPF (String, único)
- Data de Cadastro (LocalDateTime)
- Status (Enum: ATIVO, INATIVO)

**Empréstimo (Loan)**
- ID (Long)
- Usuário (relacionamento com User)
- Livro (relacionamento com Book)
- Data do Empréstimo (LocalDate)
- Data de Devolução Prevista (LocalDate)
- Data de Devolução Real (LocalDate, nullable)
- Status (Enum: ATIVO, DEVOLVIDO, ATRASADO)

### 3. Implementação da API REST
Desenvolver os seguintes endpoints:

**Livros:**
- `GET /api/books` - Listar todos os livros (com paginação)
- `GET /api/books/{id}` - Buscar livro por ID
- `POST /api/books` - Cadastrar novo livro
- `PUT /api/books/{id}` - Atualizar livro
- `DELETE /api/books/{id}` - Remover livro

**Usuários:**
- `GET /api/users` - Listar todos os usuários
- `GET /api/users/{id}` - Buscar usuário por ID
- `POST /api/users` - Cadastrar novo usuário
- `PUT /api/users/{id}` - Atualizar usuário

**Empréstimos:**
- `GET /api/loans` - Listar empréstimos
- `GET /api/loans/user/{userId}` - Listar empréstimos de um usuário
- `POST /api/loans` - Realizar empréstimo
- `PUT /api/loans/{id}/return` - Registrar devolução

### 4. Regras de Negócio
- Um usuário pode ter no máximo 3 livros emprestados simultaneamente
- Prazo padrão de empréstimo: 14 dias
- Não permitir empréstimo se não houver exemplares disponíveis
- Validar dados de entrada (email válido, CPF válido, etc.)
- Atualizar quantidade disponível automaticamente nos empréstimos/devoluções

### 5. Testes
- Implementar testes unitários usando JUnit 5
- Criar testes para as principais regras de negócio
- Utilizar Mockito para mock de dependências
- Atingir pelo menos 70% de cobertura de código nos services

### 6. Documentação
- Documentar a API usando SpringDoc (OpenAPI 3)
- Criar arquivo README.md detalhado
- Incluir collection do Postman ou arquivo com exemplos de requisições

## 🚀 Objetivos Opcionais (Diferenciais)

### Nível 1 - Melhorias Básicas
- **Validação Avançada**: Implementar Bean Validation com anotações customizadas
- **Tratamento de Exceções**: Criar um handler global para exceções
- **DTOs**: Implementar padrão DTO com MapStruct para conversões
- **Logs**: Adicionar logs estruturados com SLF4J e Logback

### Nível 2 - Funcionalidades Extras
- **Cache**: Implementar cache com Redis para consultas frequentes
- **Mensageria**: Usar RabbitMQ para notificar sobre empréstimos em atraso
- **Busca Avançada**: Implementar filtros de busca para livros (título, autor, ano)
- **Relatórios**: Endpoint para gerar relatório de empréstimos por período

### Nível 3 - Arquitetura e DevOps
- **Docker**: Containerizar a aplicação com Dockerfile e docker-compose
- **CI/CD**: Configurar pipeline básico (GitHub Actions ou GitLab CI)
- **Banco NoSQL**: Implementar histórico de empréstimos no MongoDB
- **Observabilidade**: Configurar métricas básicas com Micrometer
- **Arquitetura**: Implementar padrões como Repository, Service, Controller bem definidos

### Nível 4 - Funcionalidades Avançadas
- **Autenticação**: Implementar JWT para autenticação de usuários
- **Rate Limiting**: Implementar controle de taxa de requisições
- **Versionamento de API**: Implementar versionamento da API (v1, v2)
- **Health Check**: Endpoints de saúde da aplicação
- **Migration**: Scripts de migração do banco com Flyway

## 📦 Estrutura de Entrega

```
biblioteca-api/
├── src/
│   ├── main/
│   │   ├── java/com/biblioteca/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   ├── config/
│   │   │   └── exception/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── data.sql (opcional)
│   └── test/
├── docker-compose.yml (opcional)
├── Dockerfile (opcional)
├── README.md
├── postman_collection.json (opcional)
└── pom.xml
```

## 🔧 Tecnologias Esperadas

### Obrigatórias
- Java 21+
- Spring Boot 3.x
- Spring Data JPA
- H2/PostgreSQL/MySQL
- JUnit 5
- Git

### Opcionais (para diferenciais)
- MapStruct
- SpringDoc (OpenAPI)
- Redis
- RabbitMQ/Apache Kafka
- Docker
- MongoDB
- Flyway

## 📝 Critérios de Avaliação

### Código (40%)
- Qualidade e organização do código
- Aplicação de princípios SOLID e OOP
- Padrões de nomenclatura
- Tratamento de erros

### Funcionalidade (30%)
- Implementação dos requisitos obrigatórios
- Funcionamento correto das APIs
- Regras de negócio implementadas

### Testes (15%)
- Cobertura de testes
- Qualidade dos testes
- Uso correto de mocks

### Documentação (10%)
- Qualidade do README
- Documentação da API
- Comentários no código quando necessário

### Extras (5%)
- Implementação de objetivos opcionais
- Criatividade na solução
- Uso de boas práticas adicionais

## 📅 Prazo de Entrega
**7 dias corridos** a partir do recebimento do desafio

## 📤 Forma de Entrega
- Repositório público no GitHub
- Incluir instruções claras de como executar o projeto
- Enviar o link do repositório por email

## ❓ Dúvidas
Em caso de dúvidas sobre o desafio, entre em contato pelo email: [recruiter@empresa.com]

---

**Boa sorte e esperamos ver sua solução! 🚀**