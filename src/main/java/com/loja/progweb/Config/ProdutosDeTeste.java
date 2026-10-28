
package com.loja.progweb.Config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.loja.progweb.Model.Categoria;
import com.loja.progweb.Model.Produto;
import com.loja.progweb.Repository.CategoriaRepository;
import com.loja.progweb.Repository.ProdutoRepository;

@Component
public class ProdutosDeTeste implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutosDeTeste(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) {

        // Cria as categorias caso ainda não existam
        Categoria roupas = obterOuCriarCategoria("Roupas");
        Categoria eletronicos = obterOuCriarCategoria("Eletrônicos");
        Categoria informatica = obterOuCriarCategoria("Informática");
        Categoria casa = obterOuCriarCategoria("Casa e utilidades");
        Categoria papelaria = obterOuCriarCategoria("Papelaria");

        // Se já existem produtos, associa os que ainda não possuem categoria
        if (produtoRepository.count() > 0) {

            for (Produto produto : produtoRepository.findAll()) {

                if (produto.getCategoria() == null) {

                    String nome = produto.getNome();

                    // Associa cada produto à categoria correspondente
                    if (nome.equalsIgnoreCase("Camiseta Básica")) {
                        produto.setCategoria(roupas);

                    } else if (nome.equalsIgnoreCase("Fone Bluetooth")) {
                        produto.setCategoria(eletronicos);

                    } else if (nome.equalsIgnoreCase("Mouse sem Fio")) {
                        produto.setCategoria(informatica);

                    } else if (nome.equalsIgnoreCase("Garrafa Térmica")) {
                        produto.setCategoria(casa);

                    } else if (nome.equalsIgnoreCase("Caderno Universitário")
                            || nome.equalsIgnoreCase("Mochila")) {
                        produto.setCategoria(papelaria);
                    }

                    // Salva a associação no banco de dados
                    if (produto.getCategoria() != null) {
                        produtoRepository.save(produto);
                    }
                }
            }

            // Não cadastra os produtos de teste novamente
            return;
        }

        // Cadastra os produtos de teste quando o banco está vazio
        produtoRepository.save(new Produto(
                "Camiseta Básica", "Algodão, várias cores",
                new BigDecimal("49.90"), 20, roupas));

        produtoRepository.save(new Produto(
                "Fone Bluetooth", "Com cancelamento de ruído",
                new BigDecimal("189.90"), 15, eletronicos));

        produtoRepository.save(new Produto(
                "Mouse sem Fio", "Receptor USB",
                new BigDecimal("89.90"), 30, informatica));

        produtoRepository.save(new Produto(
                "Garrafa Térmica", "Aço inox, 750 ml",
                new BigDecimal("69.90"), 3, casa));

        produtoRepository.save(new Produto(
                "Caderno Universitário", "200 folhas",
                new BigDecimal("24.90"), 50, papelaria));

        produtoRepository.save(new Produto(
                "Mochila", "Compartimento para notebook",
                new BigDecimal("159.90"), 0, papelaria));
    }

    // Procura uma categoria existente ou cria uma nova
    private Categoria obterOuCriarCategoria(String nome) {

        for (Categoria categoria : categoriaRepository.findAll()) {
            if (categoria.getNome().equalsIgnoreCase(nome)) {
                return categoria;
            }
        }

        return categoriaRepository.save(new Categoria(nome));
    }
}
