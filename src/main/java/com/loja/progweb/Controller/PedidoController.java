package com.loja.progweb.Controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.loja.progweb.Model.Pedido;
import com.loja.progweb.Servicce.PedidoService;

@Controller
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/meus-pedidos")
    public ModelAndView meusPedidos(Principal principal) {
        return new ModelAndView("meus-pedidos")
                .addObject("pedidos", pedidoService.listarDoUsuario(principal.getName()));
    }

    @GetMapping("/pedido/{id}")
    public ModelAndView detalhe(@PathVariable Long id, Authentication auth) {
        Pedido pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // um cliente só pode ver os PRÓPRIOS pedidos (o administrador vê todos)
        boolean dono = pedido.getUsuario().getEmail().equals(auth.getName());
        boolean admin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        if (!dono && !admin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return new ModelAndView("pedido").addObject("pedido", pedido);
    }
}
