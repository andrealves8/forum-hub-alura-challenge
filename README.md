# 🏆 Badge Conquistado!
<img width="500" height="500" alt="Badge-Spring" src="https://github.com/user-attachments/assets/e88032bd-0082-479b-8b62-f1b1de5a30ad" />


# Fórum Hub

API REST construída com Spring Boot para gerenciamento de tópicos de fórum, autores e cursos. Projeto desenvolvido no **Programa ONE – Alura**.

---

## ✨ Visão Geral

O **Fórum Hub** expõe endpoints para:

* Autenticação com **JWT** (`/login`).
* CRUD de **Tópicos** (`/topicos`).
* CRUD de **Autores** (`/autores`).
* CRUD de **Cursos** (`/cursos`).
* **Paginação** e **ordenação** nas listagens.
* **Swagger/OpenAPI** para documentação interativa.
* **Tratamento global de erros** e **CORS** habilitado para `http://localhost:8080`.

---

## 🧱 Stack & Dependências

* **Java 17+**
* **Spring Boot 3** (Web, Validation, Security)
* **Spring Data JPA**
* **Lombok**
* **Auth0 Java JWT** (g geração/validação de tokens)
* **springdoc-openapi** (Swagger UI)
* Banco de dados: qualquer JDBC suportado pelo JPA (ex.: H2/PostgreSQL)

> A entidade/DAO criam o schema via JPA. Configure o datasource conforme o ambiente.

---

## 📦 Estrutura das Principais Camadas

```
com.alura.forum_hub
├─ ForumHubApplication.java        # Bootstrapping
├─ config/
│  └─ CorsConfiguration.java       # CORS para http://localhost:8080
├─ controller/
│  ├─ AuthController.java          # /login → gera token JWT
│  ├─ AutorController.java         # /autores CRUD + paginação
│  ├─ CursoController.java         # /cursos CRUD + paginação
│  └─ TopicoController.java        # /topicos CRUD + paginação
├─ domain/
│  ├─ autor/ Autor, DadosAutor
│  ├─ curso/ Curso*, DadosCurso
│  ├─ resposta/ Resposta
│  └─ topico/ Topico, DadosCadastroTopico, DadosDetalhamentoTopico
├─ infra/
│  ├─ exception/ TratamentoErros.java
│  ├─ security/
│  │  ├─ SecurityConfig.java, SecurityFilter.java
│  │  ├─ TokenService.java, DadosTokenJWT.java
│  └─ springdoc/ SpringDocConfig.java
├─ repository/
│  ├─ AutorRepository, CursoRepository, TopicoRepository
│  └─ UsuarioRepository
└─ usuario/
   ├─ Usuario (UserDetails), AutenticacaoService
   └─ Login* (record com login/senha)
```

\* O código de `Curso` e `Login` não foi listado, mas são referenciados pelas classes fornecidas:

* `Curso` deve conter `id`, `nome`, `categoria` (mapeado em `DadosCurso.categoriaCurso`).
* `Login` é um `record` com campos `login()` e `senha()`.

---

## 🔐 Segurança & Autenticação

* **Filtro JWT** (`SecurityFilter`) protege todas as rotas exceto:

  * `POST /login`
  * `GET /v3/api-docs/**`, `GET /swagger-ui.html`, `GET /swagger-ui/**`
* **Header** esperado em rotas protegidas: `Authorization: Bearer <TOKEN>`
* **Issuer** do token: `API ForumHub`
* **Expiração**: 2 horas
* **Propriedade de segredo** (obrigatória): `api.security.token.secret`

### Variáveis/Propriedades necessárias

No `application.properties`/`application.yml`:

```
# JWT
api.security.token.secret=defina_um_segredo_forte_aqui

# Datasource (exemplo H2)
spring.datasource.url=jdbc:h2:mem:forumhub;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# (ou configure PostgreSQL)
# spring.datasource.url=jdbc:postgresql://localhost:5432/forumhub
# spring.datasource.username=postgres
# spring.datasource.password=postgres
# spring.jpa.hibernate.ddl-auto=update
```

> **Dica:** use `BCryptPasswordEncoder` (já configurado) para salvar senhas com hash.

---

## ▶️ Como Executar

1. **Pré-requisitos:** Java 17+, Maven (ou Gradle equivalente).
2. Configure o `application.properties` com o segredo do JWT e o datasource.
3. **Rodar**:

   ```bash
   ./mvnw spring-boot:run
   # ou
   mvn spring-boot:run
   ```
4. Acesse o **Swagger**: `http://localhost:8080/swagger-ui.html`

---

## 📘 Documentação da API (Resumo)

### Autenticação

`POST /login`

* **Body (JSON):**

  ```json
  { "login": "usuario", "senha": "123456" }
  ```
* **200 OK**

  ```json
  { "token": "<jwt>" }
  ```

> Use este token nas próximas requisições: `Authorization: Bearer <jwt>`

---

### Autores (`/autores`) – protegido

* `GET /autores/{id}` → Detalhar autor
* `GET /autores?size=10&page=0&sort=nome,asc` → Listar paginado
* `POST /autores` → Cadastrar autor

  ```json
  { "nome": "Ada Lovelace", "email": "ada@exemplo.com" }
  ```
* `PUT /autores` → Editar autor (enviar JSON completo com `id`)

  ```json
  { "id": 1, "nome": "Ada L.", "email": "ada@exemplo.com" }
  ```
* `DELETE /autores/{id}` → Remover

**DTO de resposta:** `DadosAutor { id, nome, email }`

---

### Cursos (`/cursos`) – protegido

* `GET /cursos/{id}` → Detalhar curso
* `GET /cursos?size=10&page=0&sort=nome,asc` → Listar paginado
* `POST /cursos` → Cadastrar curso

  ```json
  { "nome": "Java", "categoria": "BACKEND" }
  ```
* `PUT /cursos` → Editar curso (enviar JSON completo com `id`)

  ```json
  { "id": 1, "nome": "Java Avançado", "categoria": "BACKEND" }
  ```
* `DELETE /cursos/{id}` → Remover

**DTO de resposta:** `DadosCurso { id, nome, categoriaCurso }`

> Observação: o nome do campo de domínio é `categoria`, mas o DTO exibe `categoriaCurso`.

---

### Tópicos (`/topicos`) – protegido

* `POST /topicos` → Cadastrar tópico

  ```json
  {
    "titulo": "Erro ao subir projeto",
    "mensagem": "Recebo 403 ao acessar /topicos",
    "dataCriacao": "2025-08-14T08:00:00",
    "status": "ABERTO",
    "autor": { "id": 1 },
    "curso": { "id": 1 }
  }
  ```

  **201 Created** → `Location: /topicos/{id}` e corpo `DadosDetalhamentoTopico`.

* `GET /topicos/{id}` → Detalhar tópico

* `GET /topicos?size=10&page=0&sort=titulo,asc` → Listar paginado

* `PUT /topicos/{id}` → Atualizar (parcial por campo não-nulo)

  ```json
  {
    "titulo": "Erro 403 resolvido?",
    "status": "EM_ANDAMENTO"
  }
  ```

* `DELETE /topicos/{id}` → Excluir (204 No Content)

**DTO de resposta:** `DadosDetalhamentoTopico { id, titulo, mensagem, dataCriacao, status, autor, curso }`

> A atualização é **parcial**: apenas campos não nulos em `DadosCadastroTopico` sobrescrevem o existente.

---

## 🧭 Paginação & Ordenação

Parâmetros (Spring Data):

* `page` (default 0), `size` (default 20), `sort` (ex.: `sort=nome,asc`).

Controladores já marcam `@PageableDefault(sort = {"nome"|"titulo"})`.

---

## 🌐 CORS

`CorsConfiguration` libera **origem** `http://localhost:8080` para métodos `GET, POST, PUT, DELETE, OPTIONS` e headers `*` com `allowCredentials(true)`.

---

## 🧰 Tratamento de Erros

* `404 Not Found` → `EntityNotFoundException`
* `400 Bad Request` → `MethodArgumentNotValidException` (retorna lista `{ campo, mensagem }`)
* `400 Bad Request` → `ValidacaoException` (mensagem textual)

Exemplo de erro de validação (400):

```json
[
  { "campo": "titulo", "mensagem": "não deve estar em branco" }
]
```

---

## 🧑‍💻 Usuários & Senhas

A autenticação usa `Usuario` (`UserDetails`) com **ROLE\_USER**. Para testar em dev, crie um usuário na base com senha **BCrypt**. Exemplo `data.sql`:

```sql
INSERT INTO usuarios (login, senha) VALUES (
  'admin',
  '$2a$10$VYtJv7h1wDq3lUu3hH3hNeJH3kI1pQm8aCqk4Vq9s1H1sKzF1GmU.'
);
```

> A hash acima é ilustrativa. Gere a sua com `new BCryptPasswordEncoder().encode("sua_senha")`.

---

## 🌱 Exemplos cURL

### 1) Login

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","senha":"123456"}'
```

### 2) Criar Autor

```bash
curl -X POST http://localhost:8080/autores \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ada Lovelace","email":"ada@exemplo.com"}'
```

### 3) Criar Curso

```bash
curl -X POST http://localhost:8080/cursos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Java","categoria":"BACKEND"}'
```

### 4) Criar Tópico

```bash
curl -X POST http://localhost:8080/topicos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo":"Erro ao subir projeto",
    "mensagem":"403 ao acessar /topicos",
    "dataCriacao":"2025-08-14T08:00:00",
    "status":"ABERTO",
    "autor":{"id":1},
    "curso":{"id":1}
  }'
```

---

## 📎 Swagger / OpenAPI

O projeto já inclui `SpringDocConfig` com **Bearer Auth**. A UI fica disponível em:

* `GET /swagger-ui.html`
* Esquema de segurança: `bearer-key`

> Para testar rotas protegidas no Swagger, clique em **Authorize** e cole `Bearer <seu_token>`.

---

## 🧪 Notas de Implementação

* `Topico.atualizarDados(...)` faz update **campo a campo** somente quando não-nulo.
* `SecurityFilter` extrai o token de `Authorization` aceitando `"Bearer <token>"` (com `trim()`).
* `TokenService` usa `HMAC256(secret)`; **defina `api.security.token.secret`**.
* `DadosCurso` mapeia `categoria` do domínio para `categoriaCurso` no DTO.
* `Resposta` está mapeada e relacionada a `Topico`, porém **não há endpoints** de resposta na versão atual.

---

## ✅ Checklist de Configuração

* [ ] Definir `api.security.token.secret`.
* [ ] Configurar datasource (H2/PG) e `ddl-auto`.
* [ ] Criar usuário inicial (via `data.sql` ou endpoint admin próprio).
* [ ] Rodar `mvn spring-boot:run`.
* [ ] Testar `/login` e demais endpoints com `Authorization: Bearer <token>`.

---

## Desenvolvimento

Projeto desenvolvido por André Alves como parte do **Programa ONE – Alura**.
