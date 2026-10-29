package com.loja.progweb.Servicce;

/** Erro de regra de negócio na compra (cartão inválido, estoque insuficiente...). A mensagem vai para a tela. */


public class CompraInvalidaException extends RuntimeException {

    public CompraInvalidaException(String mensagem) {
        super(mensagem);
    }
}
