# Projeto Desafio Técnico Java

## Proposta do Projeto

Este projeto tem como objetivo demonstrar a implementação de uma API RESTful com Java utilizando o Spring Boot, Hibernate, e outras bibliotecas populares. A aplicação deve gerenciar usuarios e realizar transações entre o mesmos, e o envio de notificações(Mockadas) após o sucesso das operações.

## Bibliotecas Utilizadas

- **Spring Boot**: Framework para criação de aplicações Java com configuração mínima.
- **Hibernate**: Framework de mapeamento objeto-relacional (ORM) para persistência de dados.
- **JUnit**: Framework para testes unitários em Java.
- **Mockito**: Biblioteca para criação de mocks em testes unitários.
- **Swagger**: Ferramenta para documentação de APIs RESTful.
- **H2 Database**: Banco de dados em memória utilizado para testes.
- **Flyway**: Ferramenta para versionamento e migração de banco de dados.
- **PostgreSQL**: Sistema de gerenciamento de banco de dados objeto-relacional avançado.

## Estrutura do Projeto

A estrutura do projeto segue a convenção padrão do Maven:

- **src/main/java**: Contém o código-fonte da aplicação.
  - **com.example.projeto**: Pacote principal da aplicação.
    - **controller**: Contém as classes de controle da aplicação (endpoints REST).
    - **service**: Contém as classes de serviço, onde a lógica de negócios é implementada.
    - **repository**: Contém as interfaces de repositório para acesso ao banco de dados.
    - **model**: Contém as classes de modelo que representam as entidades do banco de dados.
    - **configuration**: Contém as classes de configuração da aplicação.
- **src/main/resources**: Contém os recursos estáticos e arquivos de configuração.
  - **application.properties**: Arquivo de configuração principal do Spring Boot.
- **src/test/java**: Contém os testes unitários.
  - **com.example.projeto**: Pacote principal dos testes.
    - **controller**: Contém os testes das classes de controle.
    - **service**: Contém os testes das classes de serviço.
- **integration_test**: Contém as coleções do Postman para testes de integração.

## Configurações e Detalhes de Execução

### Pré-requisitos

- Java 17 ou superior
- Gradle
- PostgreSQL 12 ou superior

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:
   ```sql
   CREATE DATABASE desafio_tecnico_java;
   ```

2. Configure as credenciais do banco de dados no arquivo `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/desafio_tecnico_java
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

### Executando a Aplicação

1. Clone o repositório:
   ```sh
   git clone https://github.com/seu_usuario/desafio_tecnico_java.git
   cd desafio_tecnico_java
   ```

2. Compile e execute a aplicação usando Gradle:
   ```sh
   ./gradlew clean build
   ./gradlew bootRun
   ```

3. A aplicação estará disponível em `http://localhost:8080`.

### Executando os Testes

Para executar os testes unitários utilize o comando:
```sh
./gradlew test
```

Para executar os testes de integração utilize o comando:
```sh
npm install -g newman
newman run integration_test/CrudAPI.postman_collection.json -r cli,html
```

### Documentação da API

A documentação da API pode ser acessada em `http://localhost:8080/swagger-ui.html` após iniciar a aplicação.

`
