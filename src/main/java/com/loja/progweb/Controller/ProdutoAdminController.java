
package com.loja.progweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loja.progweb.Repository.CategoriaRepository;
import com.loja.progweb.Repository.ProdutoRepository;

@Controller
@RequestMapping("/admin/produtos")
public class ProdutoAdminController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoAdminController(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository) {

        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Exibe a página de gerenciamento de produtos
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());

        return "admin-produtos";
    }
}
