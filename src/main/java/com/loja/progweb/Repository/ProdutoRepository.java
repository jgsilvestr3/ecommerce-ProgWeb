package com.loja.progweb.Repository;

import org.springframework.data.repository.CrudRepository;

import com.loja.progweb.Model.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long> {
}