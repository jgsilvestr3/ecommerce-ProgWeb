package com.example.demo.service;

import com.example.demo.model.Produto;
import com.example.demo.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repository;

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public ProdutoRepository getRepository() {
        return repository;
    }

    public void setRepository(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}