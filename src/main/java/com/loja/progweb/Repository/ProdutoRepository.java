package com.loja.progweb.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loja.progweb.Model.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long> {

    // Busca produtos cujo nome contenha o texto informado, ignorando maiúsculas e minúsculas
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // Busca produtos que pertençam à categoria informada
    List<Produto> findByCategoriaId(Long categoriaId);

    // Busca produtos pelo nome dentro de uma categoria específica
    List<Produto> findByCategoriaIdAndNomeContainingIgnoreCase(Long categoriaId, String nome);

    // Verifica se existe algum produto usando a categoria (impede excluir categoria em uso)
    boolean existsByCategoriaId(Long categoriaId);
}   