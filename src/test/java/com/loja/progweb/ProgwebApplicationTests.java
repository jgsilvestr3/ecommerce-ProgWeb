package com.loja.progweb;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.loja.progweb.Config.DadosIniciais;
import com.loja.progweb.Model.Produto;
import com.loja.progweb.Repository.ProdutoRepository;

// Usa um banco em memória SÓ nos testes, para não mexer no arquivo ./data/loja da aplicação
// (assim dá para rodar os testes mesmo com a aplicação aberta).
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:teste;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class ProgwebApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private DadosIniciais dadosIniciais;

    @Test
    void contextLoads() {
    }

    @Test
    void catalogoAssociaImagemAosProdutosComFoto() {
        assertImagemDoProduto("Notebook 15\" 8 GB", "/images/produtos/notebook.webp");
        assertImagemDoProduto("Smartphone 128 GB", "/images/produtos/smartphone.webp");
        assertImagemDoProduto("Fone Bluetooth", "/images/produtos/fone.jpg");
        assertImagemDoProduto("Smartwatch Fitness", "/images/produtos/relogio.webp");
        assertImagemDoProduto("Caixa de Som Portátil", "/images/produtos/caixa.avif");
        assertImagemDoProduto("Teclado Mecânico", "/images/produtos/teclado.jpg");
        assertImagemDoProduto("Mouse sem Fio", "/images/produtos/mouse.webp");
        assertImagemDoProduto("Monitor 24\" Full HD", "/images/produtos/monitor.webp");
        assertImagemDoProduto("Bola de Futebol", "/images/produtos/bola.jpg");
        assertImagemDoProduto("Tapete de Yoga", "/images/produtos/yoga.jpg");
        assertImagemDoProduto("Halteres 2 x 5 kg", "/images/produtos/halter.jpg");
        assertImagemDoProduto("Garrafa Térmica 750 ml", "/images/produtos/garrafa.jpg");
        assertImagemDoProduto("Corda de Pular", "/images/produtos/corda.jpg");
        assertImagemDoProduto("Java para Iniciantes", "/images/produtos/aprender_java.jpg");
        assertImagemDoProduto("Banco de Dados na Prática", "/images/produtos/banco_de_dados.webp");
        assertImagemDoProduto("Interfaces Acessíveis", "/images/produtos/acessibilidade.jpg");
        assertImagemDoProduto("Spring Boot em Ação", "/images/produtos/spring_boot.jpg");
        assertImagemDoProduto("Hidratante Corporal 400 ml", "/images/produtos/hidratante.webp");
        assertImagemDoProduto("Protetor Solar FPS 50", "/images/produtos/protetor.jpg");
        assertImagemDoProduto("Shampoo Reparador 450 ml", "/images/produtos/shampoo.webp");
        assertImagemDoProduto("Perfume Floral 100 ml", "/images/produtos/perfume.webp");
        assertImagemDoProduto("Camiseta Básica", "/images/produtos/camisa.jpg");
        assertImagemDoProduto("Calça Jeans Slim", "/images/produtos/jeans1.jpg");
        assertImagemDoProduto("Jaqueta Corta-Vento", "/images/produtos/cortavento.webp");
        assertImagemDoProduto("Tênis Casual", "/images/produtos/tenis1.jpg");
        assertImagemDoProduto("Moletom com Capuz", "/images/produtos/moletom.jpg");
        assertImagemDoProduto("Meias (kit com 6)", "/images/produtos/meia.jpg");
        assertImagemDoProduto("Jogo de Panelas 6 peças", "/images/produtos/panela.jpg");
        assertImagemDoProduto("Cafeteira Elétrica", "/images/produtos/cafeteira.webp");
        assertImagemDoProduto("Liquidificador 500 W", "/images/produtos/liquidificador.avif");
        assertImagemDoProduto("Jogo de Cama Casal", "/images/produtos/jogo_de_cama.webp");
        assertImagemDoProduto("Luminária de Mesa LED", "/images/produtos/luminaria1.jpg");
        assertImagemDoProduto("Organizador Multiuso", "/images/produtos/organizador.jpg");
    }

    @Test
    void imagensDeNotebookJeansETenisSaoAtualizadasEmProdutosExistentes() {
        atualizarImagem("Notebook 15\" 8 GB", "/images/produtos/imagem-antiga.jpg");
        atualizarImagem("Calça Jeans Slim", "/images/produtos/imagem-antiga.jpg");
        atualizarImagem("Tênis Casual", "/images/produtos/imagem-antiga.jpg");

        dadosIniciais.run();

        assertImagemDoProduto("Notebook 15\" 8 GB", "/images/produtos/notebook.webp");
        assertImagemDoProduto("Calça Jeans Slim", "/images/produtos/jeans1.jpg");
        assertImagemDoProduto("Tênis Casual", "/images/produtos/tenis1.jpg");
    }

    @Test
    void imagensDoCatalogoSaoServidasComFormatoCorreto() throws Exception {
        assertImagemDisponivel("/images/produtos/notebook.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/smartphone.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/fone.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/relogio.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/caixa.avif", "image/avif");
        assertImagemDisponivel("/images/produtos/teclado.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/mouse.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/monitor.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/bola.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/yoga.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/halter.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/garrafa.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/corda.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/aprender_java.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/banco_de_dados.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/acessibilidade.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/spring_boot.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/hidratante.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/protetor.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/shampoo.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/perfume.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/camisa.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/jeans1.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/cortavento.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/tenis1.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/moletom.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/meia.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/panela.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/cafeteira.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/liquidificador.avif", "image/avif");
        assertImagemDisponivel("/images/produtos/jogo_de_cama.webp", "image/webp");
        assertImagemDisponivel("/images/produtos/luminaria1.jpg", "image/jpeg");
        assertImagemDisponivel("/images/produtos/organizador.jpg", "image/jpeg");
    }

    @Test
    void homeRenderizaImagemDoProdutoEFallback() throws Exception {
        Produto produto = new Produto(
                "Produto com imagem", "Descrição", new BigDecimal("10.00"), 2);
        produto.setImagem("/images/produtos/produto-teste.jpg");
        produtoRepository.save(produto);
        produtoRepository.save(new Produto(
                "Produto sem imagem", "Descrição", new BigDecimal("10.00"), 2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "src=\"/images/produtos/produto-teste.jpg\"")))
                .andExpect(content().string(containsString(
                        "src=\"/images/produtos/sem-imagem.svg\"")))
                .andExpect(content().string(containsString(
                        "alt=\"Produto com imagem\"")));
    }

    private void assertImagemDoProduto(String nome, String imagemEsperada) {
        Produto produto = produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .filter(candidato -> candidato.getNome().equals(nome))
                .findFirst()
                .orElseThrow();

        assertEquals(imagemEsperada, produto.getImagem(), nome);
    }

    private void atualizarImagem(String nome, String imagem) {
        Produto produto = produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .filter(candidato -> candidato.getNome().equals(nome))
                .findFirst()
                .orElseThrow();
        produto.setImagem(imagem);
        produtoRepository.save(produto);
    }

    private void assertImagemDisponivel(String caminho, String tipoConteudo) throws Exception {
        mockMvc.perform(get(caminho))
                .andExpect(status().isOk())
                .andExpect(content().contentType(tipoConteudo));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void edicaoRenderizaPreviewDaImagemAtual() throws Exception {
        Produto produto = new Produto(
                "Produto editável", "Descrição", new BigDecimal("10.00"), 2);
        produto.setImagem("/images/produtos/produto-editavel.jpg");
        produto = produtoRepository.save(produto);

        mockMvc.perform(get("/admin/produtos/editar/{id}", produto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "src=\"/images/produtos/produto-editavel.jpg\"")))
                .andExpect(content().string(containsString(
                        "value=\"/images/produtos/produto-editavel.jpg\"")));
    }

}
