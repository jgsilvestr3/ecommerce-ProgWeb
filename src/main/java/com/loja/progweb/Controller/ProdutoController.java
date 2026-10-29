package com.loja.progweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loja.progweb.Model.Produto;
import com.loja.progweb.Servicce.ProdutoService;

import jakarta.validation.Valid;

@Controller 
public class ProdutoController {

    private ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/produtos/novo")
    public ModelAndView novoProduto(){

        Produto produto = new Produto();
        return new ModelAndView("cadastro-produto").addObject("produto", produto);
    }

    @GetMapping("/produtos")
    public ModelAndView listarProdutos(){

        return new ModelAndView("lista-produto").addObject( "produtos", produtoService.listar());
    }
    
    @GetMapping("/admin/produtos")
    public ModelAndView adminProdutos(){

        return new ModelAndView("admin-lista-produto").addObject( "produtos", produtoService.listar());
    }

    @GetMapping ("/admin/produtos/editar/{id}")
    public ModelAndView editar(@PathVariable Long id){

        Produto pd = new Produto();
        pd = produtoService.buscarPorId(id).orElseThrow();
        return new ModelAndView("cadastro-produto").addObject("produto", pd);

    }

    @GetMapping ("/admin/produtos/excluir/{id}")
    public String excluir(@PathVariable Long id){
        produtoService.deletarPorId(id);
        return "redirect:/admin/produtos";
    }

    @PostMapping ("/admin/produtos/salvar")
    public String salvar(@Valid @ModelAttribute("produto") Produto produto, BindingResult resultado){

        if (resultado.hasErrors()) {

            return "cadastro-produto";
        }
        produtoService.salvar(produto);
        return "redirect:/admin/produtos";
    }

    
}
