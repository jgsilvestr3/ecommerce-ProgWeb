package com.loja.progweb.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loja.progweb.Model.Pedido;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {

    // o Spring Data entende o nome: pedidos do usuário com aquele e-mail, do mais novo para o mais antigo
    List<Pedido> findByUsuarioEmailOrderByDataHoraDesc(String email);
}
