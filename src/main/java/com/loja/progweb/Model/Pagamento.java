package com.loja.progweb.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Dados do pagamento. Repare no que NÃO existe aqui: o CVV nunca é guardado.
 * O número do cartão só é guardado criptografado (AES-256-GCM); para exibir,
 * usamos apenas a bandeira e os 4 últimos dígitos.
 */
@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titular;
    private String bandeira;

    @Column(length = 4)
    private String finalCartao;

    @Column(length = 512)
    private String numeroCriptografado;

    private String codigoTransacao;
    private boolean aprovado;
    private String mensagem;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public String getBandeira() { return bandeira; }
    public void setBandeira(String bandeira) { this.bandeira = bandeira; }
    public String getFinalCartao() { return finalCartao; }
    public void setFinalCartao(String finalCartao) { this.finalCartao = finalCartao; }
    public String getNumeroCriptografado() { return numeroCriptografado; }
    public void setNumeroCriptografado(String numeroCriptografado) { this.numeroCriptografado = numeroCriptografado; }
    public String getCodigoTransacao() { return codigoTransacao; }
    public void setCodigoTransacao(String codigoTransacao) { this.codigoTransacao = codigoTransacao; }
    public boolean isAprovado() { return aprovado; }
    public void setAprovado(boolean aprovado) { this.aprovado = aprovado; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
