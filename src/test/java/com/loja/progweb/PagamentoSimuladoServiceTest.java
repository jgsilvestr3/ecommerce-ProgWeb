package com.loja.progweb;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.loja.progweb.Servicce.PagamentoSimuladoService;

class PagamentoSimuladoServiceTest {

    private final PagamentoSimuladoService servico = new PagamentoSimuladoService();

    @Test
    void cartaoComumEAprovado() {
        PagamentoSimuladoService.Resultado r = servico.processar("4111111111111111", new BigDecimal("99.90"));
        assertTrue(r.aprovado());
        assertTrue(r.codigoTransacao().startsWith("SIM-"));
    }

    @Test
    void cartaoDeTesteDeRecusaERecusado() {
        PagamentoSimuladoService.Resultado r = servico.processar("4000000000000002", new BigDecimal("99.90"));
        assertFalse(r.aprovado());
    }
}
