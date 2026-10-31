package com.loja.progweb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.loja.progweb.Controller.ProdutoAdminController;
import com.loja.progweb.Model.Produto;
import com.loja.progweb.Repository.CategoriaRepository;
import com.loja.progweb.Repository.ProdutoRepository;

class ProdutoAdminControllerTest {

    private final ProdutoRepository produtoRepository = mock(ProdutoRepository.class);
    private final CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
    private final ProdutoAdminController controller =
            new ProdutoAdminController(produtoRepository, categoriaRepository);
    private final RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

    @Test
    void cadastroSalvaOCaminhoDaImagemInformado() {
        controller.salvar("Notebook", "Descrição", new BigDecimal("1000.00"),
                5, null, " /images/produtos/notebook.jpg ", redirectAttributes);

        var produtoSalvo = forClass(Produto.class);
        verify(produtoRepository).save(produtoSalvo.capture());
        assertEquals("/images/produtos/notebook.jpg", produtoSalvo.getValue().getImagem());
    }

    @Test
    void atualizacaoPreservaImagemQuandoCampoNaoEInformado() {
        Produto produto = new Produto();
        produto.setImagem("/images/produtos/notebook.jpg");
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        controller.atualizar(1L, "Notebook", "Descrição", new BigDecimal("1000.00"),
                5, null, "  ", redirectAttributes);

        assertEquals("/images/produtos/notebook.jpg", produto.getImagem());
        verify(produtoRepository).save(produto);
    }

    @Test
    void atualizacaoSubstituiImagemQuandoNovoCaminhoEInformado() {
        Produto produto = new Produto();
        produto.setImagem("/images/produtos/notebook.jpg");
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        controller.atualizar(1L, "Notebook", "Descrição", new BigDecimal("1000.00"),
                5, null, "/images/produtos/notebook-novo.jpg", redirectAttributes);

        assertEquals("/images/produtos/notebook-novo.jpg", produto.getImagem());
        verify(produtoRepository).save(produto);
    }
}
