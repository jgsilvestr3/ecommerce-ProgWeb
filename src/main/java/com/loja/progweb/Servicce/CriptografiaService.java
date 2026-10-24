package com.loja.progweb.Servicce;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Criptografia reversível (AES-256 no modo GCM) para dados que precisam ser
 * recuperados depois, como o número do cartão.
 *
 * Diferente do BCrypt (usado nas senhas), que é de mão única, aqui existe
 * "descriptografar". Por isso a chave NUNCA fica no código: vem da propriedade
 * app.seguranca.chave-cartao (ou da variável de ambiente CHAVE_CARTAO).
 *
 * Cada criptografia usa um IV (número aleatório) novo, guardado junto do texto:
 * o mesmo cartão criptografado duas vezes gera textos diferentes. O modo GCM
 * também detecta se alguém alterou o dado cifrado.
 */
@Service
public class CriptografiaService {

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int TAMANHO_IV = 12;
    private static final int TAMANHO_TAG_BITS = 128;

    private final SecretKeySpec chave;
    private final SecureRandom aleatorio = new SecureRandom();

    public CriptografiaService(@Value("${app.seguranca.chave-cartao}") String chaveBase64) {
        byte[] bytes = Base64.getDecoder().decode(chaveBase64.trim());
        if (bytes.length != 32) {
            throw new IllegalStateException(
                    "A chave AES precisa ter 32 bytes (256 bits) codificados em Base64.");
        }
        this.chave = new SecretKeySpec(bytes, "AES");
    }

    public String criptografar(String textoPuro) {
        try {
            byte[] iv = new byte[TAMANHO_IV];
            aleatorio.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave, new GCMParameterSpec(TAMANHO_TAG_BITS, iv));
            byte[] cifrado = cipher.doFinal(textoPuro.getBytes(StandardCharsets.UTF_8));

            // guarda IV + texto cifrado juntos, em Base64
            byte[] saida = ByteBuffer.allocate(iv.length + cifrado.length).put(iv).put(cifrado).array();
            return Base64.getEncoder().encodeToString(saida);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Falha ao criptografar o dado.", e);
        }
    }

    public String descriptografar(String textoCifrado) {
        try {
            byte[] entrada = Base64.getDecoder().decode(textoCifrado);
            if (entrada.length < TAMANHO_IV + 16) {
                throw new IllegalArgumentException("Dado criptografado inválido.");
            }
            byte[] iv = new byte[TAMANHO_IV];
            byte[] cifrado = new byte[entrada.length - TAMANHO_IV];
            System.arraycopy(entrada, 0, iv, 0, TAMANHO_IV);
            System.arraycopy(entrada, TAMANHO_IV, cifrado, 0, cifrado.length);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave, new GCMParameterSpec(TAMANHO_TAG_BITS, iv));
            return new String(cipher.doFinal(cifrado), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Dado criptografado inválido ou adulterado.", e);
        }
    }
}
