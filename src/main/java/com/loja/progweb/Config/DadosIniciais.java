package com.loja.progweb.Config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.loja.progweb.Model.Categoria;
import com.loja.progweb.Model.PagamentoForm;
import com.loja.progweb.Model.Produto;
import com.loja.progweb.Model.TipoUsuario;
import com.loja.progweb.Model.Usuario;
import com.loja.progweb.Repository.CategoriaRepository;
import com.loja.progweb.Repository.ProdutoRepository;
import com.loja.progweb.Repository.UsuarioRepository;
import com.loja.progweb.Servicce.PedidoService;

/**
 * Deixa o banco "bem populado" na primeira execução: categorias, produtos, usuários de exemplo
 * e alguns pedidos (pagos e recusado). Só cria o que ainda não existe, então pode rodar sempre.
 * O administrador inicial continua sendo criado pela classe AdminInicial.
 */
@Component
public class DadosIniciais implements CommandLineRunner {

    private static final String SENHA_CLIENTES = "cliente123";
    private static final String SENHA_FUNCIONARIO = "func123";

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PedidoService pedidoService;

    public DadosIniciais(CategoriaRepository categoriaRepository,
                         ProdutoRepository produtoRepository,
                         UsuarioRepository usuarioRepository,
                         PasswordEncoder passwordEncoder,
                         PedidoService pedidoService) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.pedidoService = pedidoService;
    }

    @Override
    public void run(String... args) {
        Map<String, Produto> produtos = criarCatalogo();
        atualizarImagensProdutos();
        boolean usuariosNovos = criarUsuarios();

        // Pedidos de exemplo só quando tudo é novo (eles baixam o estoque dos produtos)
        if (!produtos.isEmpty() && usuariosNovos) {
            try {
                int total = criarPedidosDeExemplo(produtos);
                System.out.println(">>> Pedidos de exemplo criados: " + total);
            } catch (RuntimeException e) {
                // um problema nos exemplos não pode impedir a aplicação de iniciar
                System.out.println(">>> Aviso: os pedidos de exemplo nao foram criados (" + e.getMessage() + ")");
            }
        }
    }

    private void atualizarImagensProdutos() {
        for (Produto produto : produtoRepository.findAll()) {
            String imagem = imagemDoProduto(produto.getNome());
            if (imagem != null && imagemDeveSerAtualizada(produto, imagem)) {
                produto.setImagem(imagem);
                produtoRepository.save(produto);
            }
        }
    }

    private boolean imagemDeveSerAtualizada(Produto produto, String imagemEsperada) {
        String imagemAtual = produto.getImagem();
        if (imagemAtual == null || imagemAtual.isBlank()) {
            return true;
        }

        String nomeNormalizado = produto.getNome().toLowerCase(Locale.ROOT);
        boolean imagemDeCatalogoCorrigida = nomeNormalizado.contains("notebook")
                || nomeNormalizado.contains("calça jeans")
                || nomeNormalizado.contains("tênis casual")
                || nomeNormalizado.contains("luminária de mesa");
        return imagemDeCatalogoCorrigida
                ? !imagemAtual.equals(imagemEsperada)
                : false;
    }

    private String imagemDoProduto(String nome) {
        String nomeNormalizado = nome.toLowerCase(Locale.ROOT);
        if (nomeNormalizado.contains("notebook")) {
            return "/images/produtos/notebook.webp";
        }
        if (nomeNormalizado.contains("smartphone")) {
            return "/images/produtos/smartphone.webp";
        }
        if (nomeNormalizado.contains("fone bluetooth")) {
            return "/images/produtos/fone.jpg";
        }
        if (nomeNormalizado.contains("smartwatch")) {
            return "/images/produtos/relogio.webp";
        }
        if (nomeNormalizado.contains("caixa de som")) {
            return "/images/produtos/caixa.avif";
        }
        if (nomeNormalizado.contains("teclado")) {
            return "/images/produtos/teclado.jpg";
        }
        if (nomeNormalizado.contains("mouse")) {
            return "/images/produtos/mouse.webp";
        }
        if (nomeNormalizado.contains("monitor")) {
            return "/images/produtos/monitor.webp";
        }
        if (nomeNormalizado.contains("bola de futebol")) {
            return "/images/produtos/bola.jpg";
        }
        if (nomeNormalizado.contains("tapete de yoga")) {
            return "/images/produtos/yoga.jpg";
        }
        if (nomeNormalizado.contains("halteres")) {
            return "/images/produtos/halter.jpg";
        }
        if (nomeNormalizado.contains("garrafa térmica")) {
            return "/images/produtos/garrafa.jpg";
        }
        if (nomeNormalizado.contains("corda de pular")) {
            return "/images/produtos/corda.jpg";
        }
        if (nomeNormalizado.contains("java para iniciantes")) {
            return "/images/produtos/aprender_java.jpg";
        }
        if (nomeNormalizado.contains("banco de dados na prática")) {
            return "/images/produtos/banco_de_dados.webp";
        }
        if (nomeNormalizado.contains("interfaces acessíveis")) {
            return "/images/produtos/acessibilidade.jpg";
        }
        if (nomeNormalizado.contains("spring boot em ação")) {
            return "/images/produtos/spring_boot.jpg";
        }
        if (nomeNormalizado.contains("hidratante corporal")) {
            return "/images/produtos/hidratante.webp";
        }
        if (nomeNormalizado.contains("protetor solar")) {
            return "/images/produtos/protetor.jpg";
        }
        if (nomeNormalizado.contains("shampoo reparador")) {
            return "/images/produtos/shampoo.webp";
        }
        if (nomeNormalizado.contains("perfume floral")) {
            return "/images/produtos/perfume.webp";
        }
        if (nomeNormalizado.contains("camiseta básica")) {
            return "/images/produtos/camisa.jpg";
        }
        if (nomeNormalizado.contains("calça jeans")) {
            return "/images/produtos/jeans1.jpg";
        }
        if (nomeNormalizado.contains("jaqueta corta-vento")) {
            return "/images/produtos/cortavento.webp";
        }
        if (nomeNormalizado.contains("tênis casual")) {
            return "/images/produtos/tenis1.jpg";
        }
        if (nomeNormalizado.contains("moletom com capuz")) {
            return "/images/produtos/moletom.jpg";
        }
        if (nomeNormalizado.contains("meias")) {
            return "/images/produtos/meia.jpg";
        }
        if (nomeNormalizado.contains("jogo de panelas")) {
            return "/images/produtos/panela.jpg";
        }
        if (nomeNormalizado.contains("cafeteira elétrica")) {
            return "/images/produtos/cafeteira.webp";
        }
        if (nomeNormalizado.contains("liquidificador")) {
            return "/images/produtos/liquidificador.avif";
        }
        if (nomeNormalizado.contains("jogo de cama")) {
            return "/images/produtos/jogo_de_cama.webp";
        }
        if (nomeNormalizado.contains("luminária de mesa")) {
            return "/images/produtos/luminaria1.jpg";
        }
        if (nomeNormalizado.contains("organizador multiuso")) {
            return "/images/produtos/organizador.jpg";
        }
        return null;
    }

    // ---------------------------------------------------------------- catálogo

    private Map<String, Produto> criarCatalogo() {
        Map<String, Produto> criados = new LinkedHashMap<>();
        if (produtoRepository.count() > 0) {
            return criados; // já existe catálogo: não mexe
        }

        Map<String, Categoria> categorias = new LinkedHashMap<>();
        for (Categoria existente : categoriaRepository.findAll()) {
            categorias.put(existente.getNome().toLowerCase(), existente);
        }

        String eletronicos = "Eletrônicos";
        String roupas = "Roupas";
        String casa = "Casa e Cozinha";
        String esportes = "Esportes";
        String livros = "Livros";
        String beleza = "Beleza";

        add(criados, categorias, eletronicos, "Notebook 15\" 8 GB", "Tela Full HD e SSD de 256 GB", "3299.90", 12);
        add(criados, categorias, eletronicos, "Smartphone 128 GB", "Tela de 6,5 polegadas e câmera dupla", "1899.00", 25);
        add(criados, categorias, eletronicos, "Fone Bluetooth", "Com cancelamento de ruído", "189.90", 40);
        add(criados, categorias, eletronicos, "Smartwatch Fitness", "Monitor cardíaco e GPS", "349.00", 18);
        add(criados, categorias, eletronicos, "Caixa de Som Portátil", "À prova d'água, 12 horas de bateria", "229.90", 30);
        add(criados, categorias, eletronicos, "Teclado Mecânico", "Switch azul, layout ABNT2", "279.90", 3);
        add(criados, categorias, eletronicos, "Mouse sem Fio", "Receptor USB, 1600 DPI", "89.90", 60);
        add(criados, categorias, eletronicos, "Monitor 24\" Full HD", "75 Hz e entrada HDMI", "899.00", 0);

        add(criados, categorias, roupas, "Camiseta Básica", "Algodão, várias cores", "49.90", 80);
        add(criados, categorias, roupas, "Calça Jeans Slim", "Modelagem ajustada", "159.90", 35);
        add(criados, categorias, roupas, "Jaqueta Corta-Vento", "Leve e impermeável", "229.90", 20);
        add(criados, categorias, roupas, "Tênis Casual", "Solado macio", "249.90", 28);
        add(criados, categorias, roupas, "Moletom com Capuz", "Flanelado por dentro", "139.90", 4);
        add(criados, categorias, roupas, "Meias (kit com 6)", "Algodão, cano médio", "39.90", 100);

        add(criados, categorias, casa, "Jogo de Panelas 6 peças", "Antiaderente", "299.90", 15);
        add(criados, categorias, casa, "Cafeteira Elétrica", "Prepara até 30 xícaras", "149.90", 22);
        add(criados, categorias, casa, "Liquidificador 500 W", "Jarra de vidro", "179.90", 17);
        add(criados, categorias, casa, "Jogo de Cama Casal", "Algodão 200 fios", "189.90", 30);
        add(criados, categorias, casa, "Luminária de Mesa LED", "Três níveis de brilho", "79.90", 45);
        add(criados, categorias, casa, "Organizador Multiuso", "Plástico resistente", "59.90", 0);

        add(criados, categorias, esportes, "Bola de Futebol", "Oficial, tamanho 5", "119.90", 40);
        add(criados, categorias, esportes, "Tapete de Yoga", "Antiderrapante, 6 mm", "79.90", 33);
        add(criados, categorias, esportes, "Halteres 2 x 5 kg", "Revestidos em vinil", "129.90", 26);
        add(criados, categorias, esportes, "Garrafa Térmica 750 ml", "Aço inox", "69.90", 3);
        add(criados, categorias, esportes, "Corda de Pular", "Cabo com rolamento", "24.90", 70);

        add(criados, categorias, livros, "Java para Iniciantes", "Do primeiro programa às coleções", "89.90", 20);
        add(criados, categorias, livros, "Banco de Dados na Prática", "Modelagem e SQL com exemplos", "94.90", 15);
        add(criados, categorias, livros, "Interfaces Acessíveis", "Boas práticas de acessibilidade na web", "82.90", 12);
        add(criados, categorias, livros, "Spring Boot em Ação", "Do CRUD à segurança", "109.90", 10);

        add(criados, categorias, beleza, "Hidratante Corporal 400 ml", "Para pele seca, sem perfume", "34.90", 55);
        add(criados, categorias, beleza, "Protetor Solar FPS 50", "Resistente à água", "49.90", 48);
        add(criados, categorias, beleza, "Shampoo Reparador 450 ml", "Para cabelos danificados", "29.90", 60);
        add(criados, categorias, beleza, "Perfume Floral 100 ml", "Fragrância suave", "189.90", 14);

        System.out.println(">>> Catalogo de exemplo criado: " + categorias.size() + " categorias e "
                + criados.size() + " produtos");
        return criados;
    }

    private void add(Map<String, Produto> criados, Map<String, Categoria> categorias, String nomeCategoria,
                     String nome, String descricao, String preco, int estoque) {
        Categoria categoria = categorias.get(nomeCategoria.toLowerCase());
        if (categoria == null) {
            categoria = categoriaRepository.save(new Categoria(nomeCategoria));
            categorias.put(nomeCategoria.toLowerCase(), categoria);
        }
        Produto produto = produtoRepository.save(new Produto(nome, descricao, new BigDecimal(preco), estoque, categoria));
        criados.put(nome, produto);
    }

    // ---------------------------------------------------------------- usuários

    private boolean criarUsuarios() {
        if (usuarioRepository.findByEmail("cliente@loja.com").isPresent()) {
            return false;
        }
        criarUsuario("Funcionário Exemplo", "funcionario@loja.com", "(67) 99999-0001", TipoUsuario.FUNCIONARIO, SENHA_FUNCIONARIO, "000.000.001-00");
        criarUsuario("Cliente Exemplo", "cliente@loja.com", "(67) 99999-0002", TipoUsuario.CLIENTE, SENHA_CLIENTES, "000.000.002-00");
        criarUsuario("Ana Souza", "ana.souza@exemplo.com", "(67) 99999-0003", TipoUsuario.CLIENTE, SENHA_CLIENTES, "000.000.003-00");
        criarUsuario("Bruno Lima", "bruno.lima@exemplo.com", "(67) 99999-0004", TipoUsuario.CLIENTE, SENHA_CLIENTES, "000.000.004-00");
        criarUsuario("Carla Mendes", "carla.mendes@exemplo.com", "(67) 99999-0005", TipoUsuario.CLIENTE, SENHA_CLIENTES, "000.000.005-00");
        criarUsuario("Diego Alves", "diego.alves@exemplo.com", "(67) 99999-0006", TipoUsuario.CLIENTE, SENHA_CLIENTES, "000.000.006-00");
        criarUsuario("Elaine Costa", "elaine.costa@exemplo.com", "(67) 99999-0007", TipoUsuario.CLIENTE, SENHA_CLIENTES, "000.000.007-00");
        System.out.println(">>> Usuarios de exemplo criados: funcionario@loja.com / " + SENHA_FUNCIONARIO
                + " e cliente@loja.com / " + SENHA_CLIENTES + " (mais 5 clientes)");
        return true;
    }

    private void criarUsuario(String nome, String email, String telefone, TipoUsuario tipo, String senha, String cpf) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setAtivo(true);
        usuario.setDataCadastro(LocalDate.now().toString());
        usuario.setTipoUsuario(tipo);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuarioRepository.save(usuario);
    }

    // ---------------------------------------------------------------- pedidos

    private int criarPedidosDeExemplo(Map<String, Produto> produtos) {
        int total = 0;
        total += comprar(produtos, "cliente@loja.com", "Cliente Exemplo", "4111111111111111",
                "Fone Bluetooth", 1, "Mouse sem Fio", 2);
        total += comprar(produtos, "cliente@loja.com", "Cliente Exemplo", "5555555555554444",
                "Camiseta Básica", 3, "Meias (kit com 6)", 1);
        total += comprar(produtos, "cliente@loja.com", "Cliente Exemplo", "4000000000000002", // este é recusado
                "Smartwatch Fitness", 1);
        total += comprar(produtos, "ana.souza@exemplo.com", "Ana Souza", "4111111111111111",
                "Notebook 15\" 8 GB", 1);
        total += comprar(produtos, "bruno.lima@exemplo.com", "Bruno Lima", "5555555555554444",
                "Bola de Futebol", 2, "Tapete de Yoga", 1);
        total += comprar(produtos, "carla.mendes@exemplo.com", "Carla Mendes", "4111111111111111",
                "Perfume Floral 100 ml", 1, "Hidratante Corporal 400 ml", 2);
        total += comprar(produtos, "diego.alves@exemplo.com", "Diego Alves", "4111111111111111",
                "Java para Iniciantes", 1, "Spring Boot em Ação", 1);
        return total;
    }

    /** itens = nome do produto, quantidade, nome, quantidade, ... */
    private int comprar(Map<String, Produto> produtos, String email, String titular, String numeroCartao,
                        Object... itens) {
        List<Long> ids = new ArrayList<>();
        List<Integer> quantidades = new ArrayList<>();
        for (int i = 0; i < itens.length; i += 2) {
            Produto produto = produtos.get((String) itens[i]);
            if (produto == null) {
                throw new IllegalStateException("produto de exemplo nao encontrado: " + itens[i]);
            }
            ids.add(produto.getId());
            quantidades.add((Integer) itens[i + 1]);
        }
        PagamentoForm form = new PagamentoForm();
        form.setTitular(titular);
        form.setNumeroCartao(numeroCartao);
        form.setValidade(YearMonth.now().plusYears(3).format(DateTimeFormatter.ofPattern("MM/yy")));
        form.setCvv("123");
        pedidoService.finalizarCompra(email, ids, quantidades, form);
        return 1;
    }
}
