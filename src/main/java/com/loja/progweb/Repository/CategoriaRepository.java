
package com.loja.progweb.Repository;

import org.springframework.data.repository.CrudRepository;
import com.loja.progweb.Model.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria, Long> {

    // Verifica se já existe uma categoria com esse nome
    boolean existsByNomeIgnoreCase(String nome);
}
