package com.loja.progweb.Servicce;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.loja.progweb.Model.Produto;
import com.loja.progweb.Repository.ProdutoRepository;


@Service 
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void salvar(Produto produto){
        produtoRepository.save(produto);
    }

    public Iterable<Produto> listar(){
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id){
        return produtoRepository.findById(id);
    }

    public void deletarPorId(Long id){
        produtoRepository.deleteById(id);
    }


}
