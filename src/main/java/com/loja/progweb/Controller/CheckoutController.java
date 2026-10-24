package com.loja.progweb.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.loja.progweb.Model.PagamentoForm;
import com.loja.progweb.Model.Pedido;
import com.loja.progweb.Servicce.CompraInvalidaException;
import com.loja.progweb.Servicce.PedidoService;

@Controller
public class CheckoutController {

    private final PedidoService pedidoService;

    public CheckoutController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/checkout")
    public ModelAndView checkout() {
        return new ModelAndView("checkout").addObject("pagamento", new PagamentoForm());
    }

    @PostMapping("/checkout/pagar")
    public String pagar(@RequestParam(name = "produtoId", required = false) List<Long> produtoIds,
                        @RequestParam(name = "quantidade", required = false) List<Integer> quantidades,
                        @ModelAttribute("pagamento") PagamentoForm pagamento,
                        Principal principal, Model model) {
        try {
            Pedido pedido = pedidoService.finalizarCompra(principal.getName(), produtoIds, quantidades, pagamento);
            // redirect evita cobrar duas vezes se a pessoa apertar F5 na tela de confirmação
            return "redirect:/pedido/" + pedido.getId();
        } catch (CompraInvalidaException e) {
            // nunca devolver número do cartão nem CVV para a tela
            pagamento.setNumeroCartao("");
            pagamento.setCvv("");
            model.addAttribute("erro", e.getMessage());
            return "checkout";
        }
    }
}
