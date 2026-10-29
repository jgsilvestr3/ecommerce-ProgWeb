package com.loja.progweb.Controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Deixa o e-mail de quem está logado disponível em TODAS as páginas (${emailLogado}).
 * O carrinho.js usa esse valor para guardar um carrinho separado por usuário.
 * Visitante (sem login) recebe texto vazio.
 */
@ControllerAdvice
public class UsuarioLogadoAdvice {

    @ModelAttribute("emailLogado")
    public String emailLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return "";
        }
        return auth.getName();
    }
}