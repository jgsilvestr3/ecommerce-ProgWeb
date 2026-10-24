package com.loja.progweb.Servicce;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Simula um gateway de pagamento. Nenhuma cobrança real é feita.
 *
 * Caminho ideal: todo cartão válido é aprovado. Para demonstrar o fluxo de
 * erro existe um cartão de teste que é sempre recusado.
 */
@Service
public class PagamentoSimuladoService {

    /** Cartão de teste que sempre é recusado (passa no Luhn, como os de teste reais). */
    public static final String CARTAO_RECUSADO = "4000000000000002";

    public record Resultado(boolean aprovado, String codigoTransacao, String mensagem) {
    }

    public Resultado processar(String numeroCartao, BigDecimal valor) {
        String codigo = "SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (CARTAO_RECUSADO.equals(numeroCartao)) {
            return new Resultado(false, codigo, "Pagamento recusado pela operadora (cartão de teste de recusa).");
        }
        return new Resultado(true, codigo, "Pagamento aprovado.");
    }
}
