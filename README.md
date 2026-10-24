# E-commerce — Programação Web

Aplicação de e-commerce desenvolvida para a disciplina de Programação Web, utilizando Java e Spring Boot.

## Funcionalidades

- Cadastro e autenticação de usuários.
- Catálogo de produtos com estoque.
- Carrinho de compras.
- Finalização de compras com pagamento simulado.
- Consulta de pedidos.
- Controle de acesso para clientes e administradores.

## Tecnologias

- Java 17
- Spring Boot
- Spring MVC e Thymeleaf
- Spring Security
- Spring Data JPA / Hibernate
- Banco de dados H2
- Maven

## Como executar

1. Instale o **JDK 17** ou superior compatível com o projeto.
2. Clone o repositório e entre na pasta do projeto:

   ```bash
   git clone https://github.com/jgsilvestr3/ecommerce-ProgWeb.git
   cd ecommerce-ProgWeb
   ```

3. Execute a aplicação com o Maven Wrapper.

   **Windows:**

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **Linux/macOS:**

   ```bash
   chmod +x mvnw
   ./mvnw spring-boot:run
   ```

4. Acesse a aplicação em [http://localhost:8080](http://localhost:8080).

Na primeira execução, a aplicação cria produtos de demonstração e um usuário administrador. O banco H2 é criado localmente na pasta `data/`.

## Acesso de demonstração

- **E-mail:** `admin@loja.com`
- **Senha:** `admin123`

Essas credenciais são destinadas somente ao ambiente acadêmico/local. Não utilize essa senha nem a chave de criptografia padrão em produção.

## Testes

Para executar os testes automatizados:

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```

## Observação

O pagamento é simulado para fins de demonstração. Este projeto acadêmico não deve ser utilizado para processar pagamentos reais nem implantado em produção sem revisão de segurança e configuração de segredos por variáveis de ambiente.
