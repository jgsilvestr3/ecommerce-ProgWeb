package com.loja.progweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProdutoController {

    // A vitrine agora é a página inicial (/), com busca e filtro por categoria.
    // Este endereço fica só para não quebrar links antigos.
    @GetMapping("/produtos")
    public String vitrine() {
        return "redirect:/";
    }
}
