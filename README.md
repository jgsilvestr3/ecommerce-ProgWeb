## 1. Modificações realizadas

### 1.1. Página inicial da loja

A página inicial foi modificada para apresentar a interface da loja virtual ShopHub. As alterações incluem:

- Barra superior com a identidade visual da loja.
- Barra lateral para exibição das categorias.
- Barra de pesquisa de produtos.
- Exibição dinâmica dos produtos cadastrados no banco de dados.
- Apresentação do nome, descrição, preço e estoque dos produtos.
- Adaptação da interface para diferentes tamanhos de tela.

### 1.2. Integração com o banco de dados

A página inicial foi integrada aos repositórios de produtos e categorias.

O controlador responsável pela página inicial consulta os dados armazenados no banco de dados e os disponibiliza para a interface por meio do Thymeleaf.

Com isso, os produtos e as categorias são exibidos dinamicamente, sem a necessidade de inserir manualmente cada item no HTML.

### 1.3. Pesquisa e filtragem de produtos

Foi implementada uma funcionalidade de pesquisa que permite localizar produtos pelo nome e pela categoria.

Também é possível combinar os critérios de pesquisa, facilitando a localização de produtos específicos.

Para isso, foram utilizados métodos no repositório de produtos que permitem realizar consultas considerando o nome e a categoria.

### 1.4. Gerenciamento de categorias

Foi desenvolvida uma área administrativa para o gerenciamento das categorias dos produtos.

As funcionalidades implementadas incluem:

- Listagem das categorias cadastradas.
- Cadastro de novas categorias.
- Validação para impedir o cadastro de categorias com nomes duplicados, desconsiderando diferenças entre letras maiúsculas e minúsculas.
- Exclusão de categorias.
- Verificação para impedir a exclusão de categorias associadas a produtos.

As categorias cadastradas podem ser utilizadas para organizar os produtos exibidos na página inicial.

### 1.5. Gerenciamento de produtos

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

### 1.6. Autenticação e controle de acesso

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

### 1.7. Preparação para o envio de imagens dos produtos

Foi iniciada a implementação do suporte a imagens dos produtos.

As modificações realizadas incluem:

- Adição do atributo `imagem` à classe `Produto`, destinado a armazenar o nome do arquivo da imagem.
- Criação da pasta `uploads/produtos` na raiz do projeto.
- Criação da classe `ConfiguracaoImagens`, responsável por disponibilizar as imagens dessa pasta pelo navegador.
- Teste de acesso a uma imagem armazenada na pasta, realizado com sucesso.

**Situação atual:** o acesso às imagens está configurado e foi testado. A implementação do envio de imagens pelos formulários de cadastro e edição, assim como a exibição automática das imagens nos cartões dos produtos, ainda está pendente.

## 2. Principais arquivos do projeto

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

## 3. Endereços para acessar as telas do ShopHub

Com a aplicação Spring Boot em execução, as telas podem ser acessadas pelo navegador utilizando os seguintes endereços locais.

| Tela ou funcionalidade | Endereço |
|---|---|
| Página inicial da loja | `http://localhost:8080/` |
| Login | `http://localhost:8080/login` |
| Gerenciamento de categorias | `http://localhost:8080/admin/categorias` |
| Gerenciamento de produtos | `http://localhost:8080/admin/produtos` |
| Teste de acesso às imagens | `http://localhost:8080/imagens/produtos/teste.jpg` |

**Observação:** o endereço de teste das imagens funciona somente se o arquivo `teste.jpg` estiver presente na pasta `uploads/produtos`.

### 3.1. Acesso à área administrativa

Para acessar as funcionalidades administrativas:

1. Inicie a aplicação Spring Boot.
2. Abra `http://localhost:8080/login`.
3. Entre com uma conta que possua a permissão de administrador.
4. Acesse o gerenciamento de produtos ou categorias pelos endereços correspondentes.

As rotas administrativas são protegidas pelo Spring Security. Portanto, usuários sem autenticação ou sem a permissão necessária não poderão acessá-las normalmente.

### 3.2. Inicialização da aplicação

Para executar o projeto no Windows, abra o terminal na pasta raiz do repositório e execute:

```powershell
.\mvnw.cmd spring-boot:run