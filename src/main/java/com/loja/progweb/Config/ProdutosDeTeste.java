package com.loja.progweb.Config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.loja.progweb.Model.Produto;
import com.loja.progweb.Repository.ProdutoRepository;

// APAGUE este arquivo quando o catálogo real do seu colega chegar.
@Component
public class ProdutosDeTeste implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;

    public ProdutosDeTeste(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) {
        if (produtoRepository.count() > 0) {
            return;
        }
        produtoRepository.save(new Produto("Camiseta Básica", "Algodão, várias cores", new BigDecimal("49.90"), 20));
        produtoRepository.save(new Produto("Fone Bluetooth", "Com cancelamento de ruído", new BigDecimal("189.90"), 15));
        produtoRepository.save(new Produto("Mouse sem Fio", "Receptor USB", new BigDecimal("89.90"), 30));
        produtoRepository.save(new Produto("Garrafa Térmica", "Aço inox, 750 ml", new BigDecimal("69.90"), 3));
        produtoRepository.save(new Produto("Caderno Universitário", "200 folhas", new BigDecimal("24.90"), 50));
        produtoRepository.save(new Produto("Mochila", "Compartimento para notebook", new BigDecimal("159.90"), 0));
    }
}