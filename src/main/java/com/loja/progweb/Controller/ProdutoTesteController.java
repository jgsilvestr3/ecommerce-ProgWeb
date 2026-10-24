package com.loja.progweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loja.progweb.Repository.ProdutoRepository;

@Controller
public class ProdutoTesteController {

    private final ProdutoRepository produtoRepository;

    public ProdutoTesteController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Usei /teste/produtos de propósito, para não bater com o /produtos do seu colega
    @GetMapping("/teste/produtos")
    public ModelAndView listar() {
        return new ModelAndView("teste-produtos").addObject("produtos", produtoRepository.findAll());
    }
}