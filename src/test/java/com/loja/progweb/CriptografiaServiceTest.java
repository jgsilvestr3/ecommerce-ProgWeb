package com.loja.progweb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.loja.progweb.Servicce.CriptografiaService;

class CriptografiaServiceTest {

    // chave SÓ de teste (32 bytes em Base64). A chave real vem da configuração.
    private static final String CHAVE_TESTE = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    private final CriptografiaService servico = new CriptografiaService(CHAVE_TESTE);

    @Test
    void descriptografarDevolveOTextoOriginal() {
        String cifrado = servico.criptografar("4111111111111111");
        assertEquals("4111111111111111", servico.descriptografar(cifrado));
    }

    @Test
    void otextoCifradoNaoContemONumero() {
        String cifrado = servico.criptografar("4111111111111111");
        assertFalse(cifrado.contains("4111111111111111"));
    }

    @Test
    void omesmoNumeroCriptografadoDuasVezesGeraTextosDiferentes() {
        // cada criptografia usa um IV aleatório novo
        assertNotEquals(servico.criptografar("4111111111111111"), servico.criptografar("4111111111111111"));
    }

    @Test
    void dadoAdulteradoEDetectado() {
        byte[] bytes = Base64.getDecoder().decode(servico.criptografar("4111111111111111"));
        bytes[bytes.length - 1] ^= 1; // muda 1 bit do final
        String adulterado = Base64.getEncoder().encodeToString(bytes);
        assertThrows(IllegalStateException.class, () -> servico.descriptografar(adulterado));
    }

    @Test
    void chaveComTamanhoErradoERejeitada() {
        String chaveCurta = Base64.getEncoder().encodeToString(new byte[16]);
        assertThrows(IllegalStateException.class, () -> new CriptografiaService(chaveCurta));
    }
}
