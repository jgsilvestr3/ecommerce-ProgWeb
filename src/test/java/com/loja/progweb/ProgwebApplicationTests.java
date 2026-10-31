package com.loja.progweb;

import static org.hamcrest.Matchers.containsString;
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

    @Test
    void contextLoads() {
    }

    @Test
    void homeRenderizaImagemDoProdutoEFallback() throws Exception {
        Produto produto = new Produto(
                "Produto com imagem", "Descrição", new BigDecimal("10.00"), 2);
        produto.setImagem("/images/produtos/produto-teste.jpg");
        produtoRepository.save(produto);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "src=\"/images/produtos/produto-teste.jpg\"")))
                .andExpect(content().string(containsString(
                        "src=\"/images/produtos/sem-imagem.svg\"")))
                .andExpect(content().string(containsString(
                        "alt=\"Produto com imagem\"")));
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
