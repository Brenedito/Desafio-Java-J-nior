# Desafio Técnico - Sistema de Gerenciamento de Biblioteca

Este repositório contém a solução para o Desafio Técnico de um Sistema de Gerenciamento de Biblioteca, desenvolvido em Spring Boot com Java 21. O objetivo principal é fornecer uma API RESTful para gerenciar livros, usuários e empréstimos, seguindo as especificações e requisitos definidos no desafio original.

## Visão Geral do Projeto

O projeto é uma aplicação Spring Boot que implementa uma API REST para um sistema de biblioteca. Ele utiliza Maven para gerenciamento de dependências e construção do projeto. O banco de dados configurado é o H2 (para desenvolvimento e testes), e a documentação da API é gerada automaticamente via SpringDoc (OpenAPI 3).

## Comparativo com o Desafio Original

O desafio original, detalhado no arquivo `Desafio.md`, estabeleceu uma série de objetivos obrigatórios e opcionais. A seguir, apresentamos um comparativo das funcionalidades implementadas em relação aos requisitos do desafio:

### 🎯 Objetivos Obrigatórios

| Requisito do Desafio Original | Status de Implementação | Detalhes da Implementação                                                                                                                                 |
|---|---|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| Configuração do Projeto (Spring Boot, Java 21+, DB, Git, README) | ✅ Concluído | Projeto Spring Boot com Java 21, H2 como DB. README em construção.                                                                                        |
| Modelagem de Dados (Livro, Usuário, Empréstimo) | ✅ Concluído | Entidades `BookModel`, `UserModel` e `LoanModel` com relacionamentos definidos.                                                                           |
| Implementação da API REST (Endpoints para Livros, Usuários, Empréstimos) | ✅ Concluído | Controladores (`BooksController`, `UsersController`, `LoansController`) com endpoints RESTful para CRUD e operações específicas.                          |
| Regras de Negócio (Limite de empréstimos, Prazo, Disponibilidade, Validação, Atualização de quantidade) | ✅ Concluído | Regras implementadas nos serviços (`BooksService`, `UsersService`, `LoansService`), incluindo validações e atualização automática de estoque.             |
| Testes (Unitários com JUnit 5, Mockito, Cobertura 70%+) | ✅ Concluído | Testes unitários para os serviços, utilizando JUnit 5 e Mockito. Cobertura de código verificada.                                                          |
| Documentação (SpringDoc, README, Postman Collection) | ✅ Concluído | API documentada com SpringDoc (acessível em `/swagger-ui.html`) e Insomnia Collection. |

### 🚀 Objetivos Opcionais (Diferenciais)

| Requisito do Desafio Original | Status de Implementação | Detalhes da Implementação |
|---|---|---|
| **Nível 1 - Melhorias Básicas** | | |
| Validação Avançada (Bean Validation, anotações customizadas) | ✅ Concluído | Utilização de Bean Validation com grupos de validação (`OnCreate`, `OnUpdate`). |
| Tratamento de Exceções (Handler global) | ❌ Não Implementado | Não foi implementado um handler global de exceções. |
| DTOs (MapStruct para conversões) | ✅ Concluído | DTOs (`BookDTO`, `LoanDTO`, `UserDTO`) utilizados para transferência de dados. Não foi utilizado MapStruct, mas as conversões são realizadas manualmente. |
| Logs (SLF4J e Logback) | ✅ Concluído | Configuração de logs com SLF4J e Logback, com arquivo de log (`biblioteca-api.log`). |
| **Nível 3 - Arquitetura e DevOps** | | |
| Docker (Dockerfile e docker-compose) | ✅ Concluído | `Dockerfile` fornecido para containerização da aplicação. Instruções de uso via Docker serão detalhadas neste README. |
| Arquitetura (Repository, Service, Controller) | ✅ Concluído | Projeto segue a arquitetura em camadas com `Repository`, `Service` e `Controller` bem definidos. |

## Funcionalidades da API

- A API oferece os seguintes recursos:

### Livros
- `GET /api/books`: Lista todos os livros com paginação.
- `GET /api/books/{id}`: Busca um livro pelo ID.
- `POST /api/books`: Cadastra um novo livro.
- `PUT /api/books/{id}`: Atualiza um livro existente.
- `DELETE /api/books/{id}`: Remove um livro.

### Usuários
- `GET /api/users`: Lista todos os usuários.
- `GET /api/users/{id}`: Busca um usuário pelo ID.
- `POST /api/users`: Cadastra um novo usuário.
- `PUT /api/users/{id}`: Atualiza um usuário existente.

### Empréstimos
- `GET /api/loans`: Lista todos os empréstimos.
- `GET /api/loans/user/{userId}`: Lista empréstimos de um usuário específico.
- `POST /api/loans`: Realiza um novo empréstimo, aplicando as regras de negócio (limite de livros por usuário, disponibilidade).
- `PUT /api/loans/{id}/return`: Registra a devolução de um livro, atualizando o status do empréstimo e a quantidade disponível do livro.

## Como Rodar o Projeto com Docker

Para executar a aplicação utilizando Docker, siga os passos abaixo:

### Pré-requisitos
Certifique-se de ter o Docker instalado em sua máquina.

### 1. Construir a Imagem Docker
Navegue até o diretório raiz do projeto (`DesafioJunior/DesafioJunior`) onde o `Dockerfile` está localizado e execute o seguinte comando para construir a imagem Docker da aplicação:

```bash
docker build -t desafiojunior-api .
```


### 2. Executar o Contêiner Docker
Após a construção da imagem, você pode executar o contêiner Docker. A aplicação estará disponível na porta 8080.

```bash
docker run -p 8080:8080 desafiojunior-api
```


### 3. Acessar a API
Uma vez que o contêiner esteja em execução, a API estará acessível em `http://localhost:8080`.

Você pode acessar a documentação da API (Swagger UI) em `http://localhost:8080/swagger-ui.html`.

### 4. Parar o Contêiner
Para parar o contêiner em execução, você pode usar `Ctrl+C` no terminal onde o `docker run` está ativo. Se você o executou em segundo plano, use:

```bash
docker ps
```
Para listar os contêineres em execução e encontrar o ID ou nome do contêiner `desafiojunior-api`, e então:

```bash
docker stop desafiojunior-api
```

## Considerações Finais

Este projeto demonstra a implementação de uma API RESTful completa para gerenciamento de biblioteca, cobrindo os requisitos essenciais e alguns diferenciais propostos no desafio. A utilização de Docker facilita a implantação e execução da aplicação em diferentes ambientes.

---

**Autor:** Breno Lemos de Brito \
**Data:** 5 à 12 de Setembro de 2025