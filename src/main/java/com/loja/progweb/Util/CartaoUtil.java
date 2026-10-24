package com.loja.progweb.Util;

import java.time.YearMonth;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Funções puras para validar dados de cartão (sem Spring, fáceis de testar).
 */
public final class CartaoUtil {

    private static final Pattern VALIDADE = Pattern.compile("^(\\d{2})\\s*/\\s*(\\d{2}|\\d{4})$");

    // Alguns prefixos (BINs) conhecidos da bandeira Elo
    private static final List<String> PREFIXOS_ELO = List.of(
            "401178", "401179", "431274", "438935", "451416", "457393",
            "457631", "457632", "504175", "627780", "636297", "636368");

    private CartaoUtil() {
    }

    /** Remove tudo que não for dígito ("4111 1111-1111" vira "41111111111"). */
    public static String somenteDigitos(String texto) {
        return texto == null ? "" : texto.replaceAll("\\D", "");
    }

    /** Número com 13 a 19 dígitos e dígito verificador correto (algoritmo de Luhn). */
    public static boolean numeroValido(String digitos) {
        if (digitos == null || !digitos.matches("\\d{13,19}")) {
            return false;
        }
        return luhnValido(digitos);
    }

    public static boolean luhnValido(String digitos) {
        int soma = 0;
        boolean dobrar = false;
        for (int i = digitos.length() - 1; i >= 0; i--) {
            int d = digitos.charAt(i) - '0';
            if (dobrar) {
                d *= 2;
                if (d > 9) {
                    d -= 9;
                }
            }
            soma += d;
            dobrar = !dobrar;
        }
        return soma % 10 == 0;
    }

    public static String bandeira(String digitos) {
        if (digitos == null || digitos.length() < 2 || !digitos.matches("\\d+")) {
            return "Outra";
        }
        for (String prefixo : PREFIXOS_ELO) {
            if (digitos.startsWith(prefixo)) {
                return "Elo";
            }
        }
        if (digitos.startsWith("4")) {
            return "Visa";
        }
        int doisPrimeiros = Integer.parseInt(digitos.substring(0, 2));
        if (doisPrimeiros >= 51 && doisPrimeiros <= 55) {
            return "Mastercard";
        }
        if (digitos.length() >= 4) {
            int quatroPrimeiros = Integer.parseInt(digitos.substring(0, 4));
            if (quatroPrimeiros >= 2221 && quatroPrimeiros <= 2720) {
                return "Mastercard";
            }
        }
        if (doisPrimeiros == 34 || doisPrimeiros == 37) {
            return "American Express";
        }
        return "Outra";
    }

    /** Aceita "MM/AA" ou "MM/AAAA". O cartão vale até o fim do mês informado. */
    public static boolean validadeValida(String texto, YearMonth agora) {
        if (texto == null) {
            return false;
        }
        Matcher m = VALIDADE.matcher(texto.trim());
        if (!m.matches()) {
            return false;
        }
        int mes = Integer.parseInt(m.group(1));
        int ano = Integer.parseInt(m.group(2));
        if (m.group(2).length() == 2) {
            ano += 2000;
        }
        if (mes < 1 || mes > 12) {
            return false;
        }
        YearMonth validade = YearMonth.of(ano, mes);
        return !validade.isBefore(agora) && !validade.isAfter(agora.plusYears(20));
    }

    public static boolean cvvValido(String texto) {
        return texto != null && texto.matches("\\d{3,4}");
    }

    public static String ultimosQuatro(String digitos) {
        if (digitos == null || digitos.length() < 4) {
            return "";
        }
        return digitos.substring(digitos.length() - 4);
    }
}
