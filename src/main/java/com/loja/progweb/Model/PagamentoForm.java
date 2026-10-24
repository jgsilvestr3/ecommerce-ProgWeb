package com.loja.progweb.Model;

/**
 * Dados digitados no formulário de pagamento. Vive só durante a requisição:
 * não é uma entidade, então nada daqui (principalmente o CVV) vai para o banco.
 */
public class PagamentoForm {

    private String titular;
    private String numeroCartao;
    private String validade;
    private String cvv;

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public String getNumeroCartao() { return numeroCartao; }
    public void setNumeroCartao(String numeroCartao) { this.numeroCartao = numeroCartao; }
    public String getValidade() { return validade; }
    public void setValidade(String validade) { this.validade = validade; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}
