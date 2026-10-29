package com.loja.progweb.Repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.loja.progweb.Model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
    Optional<Usuario> findByEmail(String email);


    
}
