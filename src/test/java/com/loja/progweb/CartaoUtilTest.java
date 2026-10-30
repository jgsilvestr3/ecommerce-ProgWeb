package com.loja.progweb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import com.loja.progweb.Util.CartaoUtil;

class CartaoUtilTest {

    private static final YearMonth AGORA = YearMonth.of(2026, 10);

    @Test
    void numerosDeTesteComDigitoVerificadorCorretoSaoValidos() {
        assertTrue(CartaoUtil.numeroValido("4111111111111111"));
        assertTrue(CartaoUtil.numeroValido("5555555555554444"));
        assertTrue(CartaoUtil.numeroValido("4000000000000002"));
    }

    @Test
    void numeroComDigitoErradoOuTamanhoErradoEInvalido() {
        assertFalse(CartaoUtil.numeroValido("4111111111111112"));
        assertFalse(CartaoUtil.numeroValido("1234"));
        assertFalse(CartaoUtil.numeroValido("abcd111111111111"));
        assertFalse(CartaoUtil.numeroValido(null));
    }

    @Test
    void somenteDigitosIgnoraEspacosETracos() {
        assertEquals("4111111111111111", CartaoUtil.somenteDigitos("4111 1111-1111 1111"));
        assertEquals("", CartaoUtil.somenteDigitos(null));
    }

    @Test
    void identificaABandeira() {
        assertEquals("Visa", CartaoUtil.bandeira("4111111111111111"));
        assertEquals("Mastercard", CartaoUtil.bandeira("5555555555554444"));
        assertEquals("American Express", CartaoUtil.bandeira("378282246310005"));
        assertEquals("Outra", CartaoUtil.bandeira("9999999999999995"));
    }

    @Test
    void validadeAceitaOMesAtualMasNaoMesesPassados() {
        assertTrue(CartaoUtil.validadeValida("10/26", AGORA));
        assertTrue(CartaoUtil.validadeValida("12/2030", AGORA));
        assertFalse(CartaoUtil.validadeValida("09/26", AGORA));
        assertFalse(CartaoUtil.validadeValida("13/30", AGORA));
        assertFalse(CartaoUtil.validadeValida("1026", AGORA));
        assertFalse(CartaoUtil.validadeValida(null, AGORA));
    }

    @Test
    void cvvTemTresOuQuatroDigitos() {
        assertTrue(CartaoUtil.cvvValido("123"));
        assertTrue(CartaoUtil.cvvValido("1234"));
        assertFalse(CartaoUtil.cvvValido("12"));
        assertFalse(CartaoUtil.cvvValido("12a"));
    }

    @Test
    void ultimosQuatroDigitos() {
        assertEquals("1111", CartaoUtil.ultimosQuatro("4111111111111111"));
        assertEquals("", CartaoUtil.ultimosQuatro("123"));
    }
}
