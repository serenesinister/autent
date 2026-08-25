# User API

API REST para gerenciamento de usuários, desenvolvida com Spring Boot, PostgreSQL, JPA, Flyway e autenticação/autorização baseada em JWT.

## Tecnologias

* Java 21
* Spring Boot 4.1.1
* Spring Web MVC
* Spring Data JPA
* Spring Security
* JWT com JJWT
* PostgreSQL
* Flyway
* Bean Validation
* Lombok
* SpringDoc OpenAPI / Swagger UI
* Maven Wrapper

## Funcionalidades

* Cadastro de usuários
* Consulta de usuários
* Consulta do usuário autenticado
* Atualização de usuários
* Exclusão de usuários
* Autenticação com JWT
* Senhas protegidas com BCrypt
* Controle de acesso por perfil
* Tratamento global de exceções
* Migração do banco de dados com Flyway
* Documentação da API com OpenAPI/Swagger

## Perfis de acesso

A aplicação possui dois perfis:

| Perfil  | Permissões                                     |
| ------- | ---------------------------------------------- |
| `USER`  | Consultar recursos protegidos                  |
| `ADMIN` | Consultar, criar, atualizar e excluir usuários |

As operações de criação, atualização e exclusão de usuários exigem o perfil `ADMIN`.

## Pré-requisitos

Antes de executar a aplicação, é necessário ter instalado:

* Java 21
* PostgreSQL 17 ou compatível
* Git

O projeto utiliza o Maven Wrapper, portanto não é necessário instalar o Maven separadamente.

## Banco de dados

A aplicação está configurada para utilizar PostgreSQL:

```text
Host: 127.0.0.1
Porta: 5432
Banco: user_api
```

As configurações atuais estão em:

```text
src/main/resources/application.properties
```

Configuração utilizada:

```properties
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/user_api?sslmode=disable
spring.datasource.password=user_api_password
```

O Flyway executa automaticamente as migrations disponíveis em:

```text
src/main/resources/db/migration
```

A migration inicial cria a estrutura necessária para a tabela de usuários.

> Em ambientes reais, credenciais de banco devem ser fornecidas por variáveis de ambiente ou outro mecanismo seguro, em vez de permanecerem diretamente no arquivo de configuração.

## Executando o projeto

Clone o repositório:

```bash
git clone https://github.com/serenesinister/autent.git
cd autent
```

No Windows:

```powershell
.\mvnw spring-boot:run
```

Ou compile o projeto:

```powershell
.\mvnw clean compile
```

Para executar os testes:

```powershell
.\mvnw clean test
```

A aplicação será disponibilizada em:

```text
http://localhost:8080
```

## Autenticação

A autenticação é realizada através do endpoint:

```http
POST /auth/login
```

Exemplo:

```json
{
  "email": "admin@email.com",
  "password": "admin123"
}
```

A resposta contém o token JWT:

```json
{
  "token": "eyJ..."
}
```

O token deve ser enviado nas requisições protegidas através do header:

```http
Authorization: Bearer <token>
```

## Endpoints

### Autenticação

#### Login

```http
POST /auth/login
```

Realiza a autenticação e retorna um JWT.

---

### Usuários

#### Criar usuário

```http
POST /users
```

**Permissão:** `ADMIN`

Exemplo:

```json
{
  "name": "Usuario Teste",
  "email": "usuario.teste@email.com",
  "password": "senha123"
}
```

Resposta:

```json
{
  "id": 15,
  "name": "Usuario Teste",
  "email": "usuario.teste@email.com",
  "role": "USER"
}
```

#### Listar usuários

```http
GET /users
```

**Permissão:** usuário autenticado.

#### Buscar usuário por ID

```http
GET /users/{id}
```

**Permissão:** usuário autenticado.

Exemplo:

```http
GET /users/15
```

#### Consultar usuário autenticado

```http
GET /users/me
```

**Permissão:** usuário autenticado.

Exemplo de resposta:

```json
{
  "name": "Administrador",
  "role": "ADMIN"
}
```

#### Atualizar usuário

```http
PUT /users/{id}
```

**Permissão:** `ADMIN`

#### Excluir usuário

```http
DELETE /users/{id}
```

**Permissão:** `ADMIN`

Em caso de sucesso, a operação retorna `204 No Content`.

Se o usuário não existir, a API retorna `404 Not Found`.

## Segurança

A aplicação utiliza:

* Spring Security
* JWT
* BCrypt para armazenamento seguro das senhas
* autorização baseada em roles

Rotas públicas:

```text
POST /auth/login
GET /v3/api-docs/**
GET /swagger-ui/**
GET /swagger-ui.html
```

As demais rotas exigem autenticação.

Operações administrativas:

```text
POST   /users
PUT    /users/**
DELETE /users/**
```

exigem:

```text
ROLE_ADMIN
```

## Swagger / OpenAPI

A API possui documentação interativa através do Swagger UI.

Acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

A especificação OpenAPI está disponível em:

```text
http://localhost:8080/v3/api-docs
```

A documentação inclui os controllers de autenticação e usuários, além dos schemas utilizados nas requisições e respostas.

### Autorização no Swagger

Após realizar o login e obter o JWT, o token pode ser utilizado para testar endpoints protegidos através do botão **Authorize** do Swagger.

Informe o token no formato:

```text
Bearer eyJ...
```

Depois disso, as requisições protegidas realizadas pelo Swagger utilizarão o JWT informado.

## Tratamento de erros

A aplicação possui tratamento global de exceções.

Entre os casos tratados estão:

* credenciais inválidas;
* usuário não encontrado;
* dados inválidos;
* acesso não autorizado.

Exemplo:

```http
404 Not Found
```

é retornado quando um usuário solicitado não existe.

## Estrutura do projeto

```text
src/
└── main/
    ├── java/com/example/demo/
    │   ├── config/
    │   │   ├── DataInitializer.java
    │   │   ├── OpenApiConfig.java
    │   │   └── SecurityConfig.java
    │   │
    │   ├── controller/
    │   │   ├── AuthController.java
    │   │   └── UserController.java
    │   │
    │   ├── dto/
    │   │   ├── CreateUserRequest.java
    │   │   ├── LoginRequest.java
    │   │   ├── LoginResponse.java
    │   │   ├── UserRequest.java
    │   │   └── UserResponse.java
    │   │
    │   ├── exception/
    │   │   ├── GlobalExceptionHandler.java
    │   │   ├── InvalidCredentialsException.java
    │   │   └── UserNotFoundException.java
    │   │
    │   ├── repository/
    │   │   └── UserRepository.java
    │   │
    │   ├── security/
    │   │   ├── JwtAuthenticationFilter.java
    │   │   └── JwtService.java
    │   │
    │   ├── service/
    │   │   ├── AuthService.java
    │   │   └── UserService.java
    │   │
    │   └── DemoApplication.java
    │
    └── resources/
        ├── application.properties
        └── db/
            └── migration/
                └── V1__create_users_table.sql
```

## Validações realizadas

Durante o desenvolvimento foram validados:

* compilação da aplicação;
* conexão com PostgreSQL;
* execução das migrations Flyway;
* autenticação JWT;
* acesso ao endpoint `/users/me`;
* bloqueio de requisições sem autenticação;
* autorização de operações administrativas;
* criação de usuários;
* listagem de usuários;
* exclusão de usuários;
* retorno `404` para usuário inexistente;
* acesso ao OpenAPI;
* acesso ao Swagger UI.

## Status

Projeto funcional e com documentação OpenAPI/Swagger disponível.

```text
API:       http://localhost:8080
Swagger:   http://localhost:8080/swagger-ui/index.html
OpenAPI:   http://localhost:8080/v3/api-docs
```
