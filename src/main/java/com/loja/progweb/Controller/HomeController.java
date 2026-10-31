package com.loja.progweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.loja.progweb.Model.Produto;
import com.loja.progweb.Repository.CategoriaRepository;
import com.loja.progweb.Repository.ProdutoRepository;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public HomeController(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public ModelAndView home(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false, defaultValue = "") String busca) {

        ModelAndView mv = new ModelAndView("home");

        Iterable<Produto> produtos;

        if (categoriaId != null && !busca.isBlank()) {
            produtos = produtoRepository
                    .findByCategoriaIdAndNomeContainingIgnoreCase(categoriaId, busca);
        } else if (categoriaId != null) {
            produtos = produtoRepository.findByCategoriaId(categoriaId);
        } else if (!busca.isBlank()) {
            produtos = produtoRepository.findByNomeContainingIgnoreCase(busca);
        } else {
            produtos = produtoRepository.findAll();
        }

        mv.addObject("produtos", produtos);
        mv.addObject("categorias", categoriaRepository.findAll());
        mv.addObject("busca", busca);
        mv.addObject("categoriaSelecionada", categoriaId);

        return mv;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    // Página mostrada quando alguém sem permissão tenta abrir uma tela (configurada no SecurityConfig)
    @GetMapping("acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }
}
