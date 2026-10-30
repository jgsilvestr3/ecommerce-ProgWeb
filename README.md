# ShopHub

Loja virtual desenvolvida para a disciplina de **Programação Web (2026-2)**, com **Spring Boot** e **Thymeleaf** (renderização no servidor, Opção A do enunciado).

Visitantes navegam pelo catálogo com busca e filtro por categoria. Clientes montam um carrinho, pagam com cartão (pagamento **simulado**) e acompanham seus pedidos. Funcionários e administradores gerenciam produtos, categorias e usuários.

## Funcionalidades

| Perfil | O que pode fazer |
|---|---|
| **Visitante** | Ver o catálogo, buscar por nome, filtrar por categoria, adicionar itens ao carrinho e criar uma conta. |
| **Cliente** | Tudo do visitante, mais: ver o carrinho, pagar, ver os próprios pedidos. |
| **Funcionário** | Gerenciar produtos (cadastrar, editar e excluir). |
| **Administrador** | Tudo do funcionário, mais: gerenciar categorias e usuários (cadastrar, editar, ativar/desativar e excluir). |

Destaques técnicos:

- **Carrinho no navegador** (`localStorage`), separado por usuário. Itens de um visitante passam para a conta dele ao entrar.
- **Pagamento simulado** com validação do cartão (algoritmo de Luhn, validade e CVV). Nenhuma cobrança real é feita.
- **Criptografia do cartão**: o número é guardado criptografado com **AES-256-GCM**. O **CVV nunca é armazenado**; a tela mostra só a bandeira e os 4 últimos dígitos.
- **Segurança**: senhas com BCrypt, login por sessão, proteção CSRF, rotas protegidas por perfil e **preço e estoque sempre recalculados no servidor** (o carrinho do navegador só informa produto e quantidade).
- **Acessibilidade**: tags semânticas (`header`, `nav`, `main`, `footer`), link "Pular para o conteúdo", campos com `label`, cores com contraste acima do mínimo WCAG AA e estados indicados por texto, não só por cor.

## Tecnologias

Java 17 · Spring Boot 4.1.1 · Spring MVC · Thymeleaf · Spring Data JPA (Hibernate) · Spring Security · banco **H2 em arquivo** · Bootstrap 5 (incluído no projeto, funciona sem internet) · JavaScript · JUnit 5

## Como executar

**Pré-requisitos:** JDK 17 ou superior e Git. Na primeira execução é preciso de internet (o Maven baixa as dependências).

```bash
git clone https://github.com/jgsilvestr3/ecommerce-ProgWeb.git
cd ecommerce-ProgWeb
```

Windows (PowerShell):
```powershell
.\mvnw spring-boot:run
```

Linux ou macOS:
```bash
./mvnw spring-boot:run
```

Quando aparecer `Started ProgwebApplication`, abra **http://localhost:8080**.

Para parar: `Ctrl + C` no terminal.

## Contas e dados de exemplo

Na primeira execução o sistema cria sozinho categorias, produtos, usuários e alguns pedidos de exemplo.

| Perfil | E-mail | Senha |
|---|---|---|
| Administrador | `admin@loja.com` | `admin123` |
| Funcionário | `funcionario@loja.com` | `func123` |
| Cliente | `cliente@loja.com` | `cliente123` |

Há ainda outros 5 clientes de exemplo (por exemplo, `ana.souza@exemplo.com`), todos com a senha `cliente123`. Qualquer pessoa também pode criar uma conta em `/cadastro` (sempre como cliente).

**Cartões de teste** (validade futura e CVV com 3 dígitos):

| Número | Resultado |
|---|---|
| `4111 1111 1111 1111` | Aprovado (Visa) |
| `5555 5555 5555 4444` | Aprovado (Mastercard) |
| `4000 0000 0000 0002` | Recusado |

Essas credenciais são só de desenvolvimento e devem ser trocadas em um ambiente real.

## Perfis e permissões

| Quem | Rotas |
|---|---|
| Qualquer pessoa | `/` (home), `/login`, `/cadastro` |
| Usuário logado | `/carrinho`, `/checkout`, `/meus-pedidos`, `/pedido/{id}` (só os próprios pedidos; o administrador vê todos) |
| Funcionário e administrador | `/admin/produtos/**` |
| Somente administrador | `/admin/categorias/**`, `/usuarios`, `/novo/usuario`, `/usuario/**` |

Depois do login: o administrador vai para a lista de usuários, o cliente para o carrinho e o funcionário para a home. Quem tentar abrir uma tela sem permissão vê a página de acesso negado.

## Segurança

- O **cadastro público sempre cria um cliente**: tipo, situação e data são definidos no servidor, e o `id` recebido é descartado (ninguém consegue sobrescrever outra conta).
- Excluir e as demais ações que alteram dados usam **POST** com token CSRF.
- A senha **nunca** é exibida. Ao editar um usuário, deixar a senha em branco mantém a atual.
- Usuário **inativo** não consegue entrar.
- O administrador não consegue excluir a própria conta, nem usuários que já têm pedidos.
- A **chave de criptografia do cartão** vem da propriedade `app.seguranca.chave-cartao`. Em um ambiente real, defina a variável de ambiente `CHAVE_CARTAO` em vez de usar o valor padrão.

## Estrutura do projeto

```
src/main/java/com/loja/progweb
├── Config/       SecurityConfig, AdminInicial, DadosIniciais
├── Controller/   telas e rotas (home, usuários, produtos, categorias, carrinho, checkout, pedidos)
├── Model/        entidades (Usuario, Produto, Categoria, Pedido, ItemPedido, Pagamento...)
├── Repository/   acesso ao banco (Spring Data JPA)
├── Servicce/     regras de negócio (UsuarioService, PedidoService, CriptografiaService...)
└── Util/         CartaoUtil (validação de cartão)

src/main/resources
├── templates/    páginas Thymeleaf (e fragmento/ com o menu e o rodapé compartilhados)
└── static/       css/ e js/ (carrinho.js e checkout.js)
```

Nota: o pacote `Servicce` (com três "c") veio do projeto original e foi mantido.

## Banco de dados

H2 em **modo arquivo** (`./data/loja`), então os dados continuam salvos depois de reiniciar a aplicação. Para recomeçar do zero, pare a aplicação e apague a pasta `data/`: na próxima execução tudo é recriado, junto com os dados de exemplo.

## Testes

```powershell
.\mvnw test
```

São 14 testes unitários (validação de cartão, criptografia e pagamento simulado) e um teste que sobe o sistema inteiro. Os testes usam um banco em memória, então podem rodar mesmo com a aplicação aberta.

## Equipe e contribuições

| Integrante | Parte do projeto |
|---|---|
| **Eduardo Ferro** | Base da aplicação com Thymeleaf: usuários, login por perfil, CRUD de produtos (versão inicial), menu e página de acesso negado. |
| **Lucas Dantas** | Módulo de produtos e categorias: home com busca e filtro, painel de produtos com validação, gerenciamento de categorias. |
| **João Gabriel** ([@jgsilvestr3](https://github.com/jgsilvestr3)) | Integração das partes, correções de segurança, carrinho por usuário, checkout com pagamento simulado, pedidos, criptografia do cartão, testes automatizados e visual ShopHub. |

## Limitações e próximos passos

- O **pagamento é simulado** e não há controle de concorrência no estoque (duas compras simultâneas da última unidade poderiam ser aceitas).
- O envio de **imagens dos produtos** ainda não foi concluído.
- Ainda não implementados: relatório de vendas e login por rede social.

---

# Anexo: documentação do módulo de produtos e categorias (Lucas Dantas)

> Texto original do Lucas, escrito antes da integração com as demais partes. Duas observações para a versão atual: os produtos de exemplo agora são criados pela classe `DadosIniciais` (no lugar de `ProdutosDeTeste`), e a pasta `uploads/` não é versionada no Git. A classe `ConfiguracaoImagens` entra junto com a conclusão do envio de imagens.

### 1. Modificações realizadas

#### 1.1. Página inicial da loja

A página inicial foi modificada para apresentar a interface da loja virtual ShopHub. As alterações incluem:

- Barra superior com a identidade visual da loja.
- Barra lateral para exibição das categorias.
- Barra de pesquisa de produtos.
- Exibição dinâmica dos produtos cadastrados no banco de dados.
- Apresentação do nome, descrição, preço e estoque dos produtos.
- Adaptação da interface para diferentes tamanhos de tela.

#### 1.2. Integração com o banco de dados

A página inicial foi integrada aos repositórios de produtos e categorias.

O controlador responsável pela página inicial consulta os dados armazenados no banco de dados e os disponibiliza para a interface por meio do Thymeleaf.

Com isso, os produtos e as categorias são exibidos dinamicamente, sem a necessidade de inserir manualmente cada item no HTML.

#### 1.3. Pesquisa e filtragem de produtos

Foi implementada uma funcionalidade de pesquisa que permite localizar produtos pelo nome e pela categoria.

Também é possível combinar os critérios de pesquisa, facilitando a localização de produtos específicos.

Para isso, foram utilizados métodos no repositório de produtos que permitem realizar consultas considerando o nome e a categoria.

#### 1.4. Gerenciamento de categorias

Foi desenvolvida uma área administrativa para o gerenciamento das categorias dos produtos.

As funcionalidades implementadas incluem:

- Listagem das categorias cadastradas.
- Cadastro de novas categorias.
- Validação para impedir o cadastro de categorias com nomes duplicados, desconsiderando diferenças entre letras maiúsculas e minúsculas.
- Exclusão de categorias.
- Verificação para impedir a exclusão de categorias associadas a produtos.

As categorias cadastradas podem ser utilizadas para organizar os produtos exibidos na página inicial.

#### 1.5. Gerenciamento de produtos

Foi desenvolvida uma área administrativa para gerenciar os produtos da loja.

As funcionalidades implementadas incluem:

- Listagem dos produtos cadastrados.
- Cadastro de produtos.
- Edição das informações dos produtos.
- Exclusão de produtos.
- Associação de produtos às categorias cadastradas.
- Validação dos campos dos formulários.

Entre as validações implementadas estão:

- O nome do produto não pode ficar vazio.
- O preço deve ser maior que zero.
- O estoque não pode ser negativo.
- A categoria selecionada deve existir no banco de dados.

As operações são realizadas por meio de controladores e repositórios, mantendo a integração com o banco de dados.

#### 1.6. Autenticação e controle de acesso

O sistema utiliza o Spring Security para controlar o acesso às funcionalidades.

Foram configuradas permissões para diferentes tipos de usuários:

- Administrador.
- Funcionário.
- Cliente.

As rotas administrativas são protegidas para impedir o acesso de usuários sem a autorização necessária.

Também foi configurada a criação inicial de um usuário administrador, caso ele ainda não esteja cadastrado.

**Credenciais administrativas do ambiente de desenvolvimento:**

- **E-mail:** `admin@loja.com`
- **Senha:** `admin123`

Essas credenciais devem ser alteradas antes de disponibilizar o sistema em um ambiente real.

#### 1.7. Preparação para o envio de imagens dos produtos

Foi iniciada a implementação do suporte a imagens dos produtos.

As modificações realizadas incluem:

- Adição do atributo `imagem` à classe `Produto`, destinado a armazenar o nome do arquivo da imagem.
- Criação da pasta `uploads/produtos` na raiz do projeto.
- Criação da classe `ConfiguracaoImagens`, responsável por disponibilizar as imagens dessa pasta pelo navegador.
- Teste de acesso a uma imagem armazenada na pasta, realizado com sucesso.

**Situação atual:** o acesso às imagens está configurado e foi testado. A implementação do envio de imagens pelos formulários de cadastro e edição, assim como a exibição automática das imagens nos cartões dos produtos, ainda está pendente.

### 2. Principais arquivos do projeto

| Arquivo | Responsabilidade |
|---|---|
| `Produto.java` | Representa os produtos, seus atributos e sua associação com categorias. Também contém o atributo destinado à imagem. |
| `Categoria.java` | Representa as categorias dos produtos. |
| `ProdutoRepository.java` | Realiza consultas e operações de persistência dos produtos. |
| `CategoriaRepository.java` | Realiza consultas e operações de persistência das categorias. |
| `HomeController.java` | Busca produtos e categorias e disponibiliza os dados para a página inicial. |
| `ProdutoAdminController.java` | Gerencia as operações administrativas relacionadas aos produtos. |
| `ConfiguracaoImagens.java` | Configura o acesso às imagens armazenadas na pasta de uploads. |
| `SecurityConfig.java` | Configura as regras de autenticação e autorização. |
| `AdminInicial.java` | Inicializa o usuário administrador quando necessário. |
| `ProdutosDeTeste.java` | Insere produtos de teste quando o repositório está vazio. |
| `home.html` | Apresenta a página inicial da loja. |
| `admin-produtos.html` | Apresenta a interface administrativa de produtos. |
| `editar-produto.html` | Apresenta o formulário de edição de produtos. |

### 3. Endereços para acessar as telas do ShopHub

Com a aplicação Spring Boot em execução, as telas podem ser acessadas pelo navegador utilizando os seguintes endereços locais.

| Tela ou funcionalidade | Endereço |
|---|---|
| Página inicial da loja | `http://localhost:8080/` |
| Login | `http://localhost:8080/login` |
| Gerenciamento de categorias | `http://localhost:8080/admin/categorias` |
| Gerenciamento de produtos | `http://localhost:8080/admin/produtos` |
| Teste de acesso às imagens | `http://localhost:8080/imagens/produtos/teste.jpg` |

**Observação:** o endereço de teste das imagens funciona somente se o arquivo `teste.jpg` estiver presente na pasta `uploads/produtos`.

#### 3.1. Acesso à área administrativa

Para acessar as funcionalidades administrativas:

1. Inicie a aplicação Spring Boot.
2. Abra `http://localhost:8080/login`.
3. Entre com uma conta que possua a permissão de administrador.
4. Acesse o gerenciamento de produtos ou categorias pelos endereços correspondentes.

As rotas administrativas são protegidas pelo Spring Security. Portanto, usuários sem autenticação ou sem a permissão necessária não poderão acessá-las normalmente.

#### 3.2. Inicialização da aplicação

Para executar o projeto no Windows, abra o terminal na pasta raiz do repositório e execute:

```powershell
.\mvnw.cmd spring-boot:run