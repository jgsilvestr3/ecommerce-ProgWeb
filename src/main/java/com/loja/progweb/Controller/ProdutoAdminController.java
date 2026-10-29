
package com.loja.progweb.Controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.loja.progweb.Model.Categoria;
import com.loja.progweb.Model.Produto;
import com.loja.progweb.Repository.CategoriaRepository;
import com.loja.progweb.Repository.ProdutoRepository;

@Controller
@RequestMapping("/admin/produtos")
public class ProdutoAdminController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoAdminController(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Exibe a lista de produtos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "admin-produtos";
    }

    // Cadastra um novo produto
    @PostMapping("/salvar")
    public String salvar(
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam BigDecimal preco,
            @RequestParam Integer estoque,
            @RequestParam(required = false) Long categoriaId,
            RedirectAttributes redirectAttributes) {

        nome = nome.trim();
        descricao = descricao.trim();

        if (nome.isBlank()) {
            redirectAttributes.addFlashAttribute("erro", "Informe o nome do produto.");
            return "redirect:/admin/produtos";
        }

        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("erro", "O preço deve ser maior que zero.");
            return "redirect:/admin/produtos";
        }

        if (estoque < 0) {
            redirectAttributes.addFlashAttribute("erro", "O estoque não pode ser negativo.");
            return "redirect:/admin/produtos";
        }

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);

        if (categoriaId != null) {
            Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);

            if (categoria == null) {
                redirectAttributes.addFlashAttribute("erro", "A categoria selecionada não existe.");
                return "redirect:/admin/produtos";
            }

            produto.setCategoria(categoria);
        }

        produtoRepository.save(produto);

        redirectAttributes.addFlashAttribute("sucesso", "Produto cadastrado com sucesso!");
        return "redirect:/admin/produtos";
    }

    // Abre o formulário de edição com os dados atuais
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model,
                         RedirectAttributes redirectAttributes) {

        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null) {
            redirectAttributes.addFlashAttribute("erro", "Produto não encontrado.");
            return "redirect:/admin/produtos";
        }

        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaRepository.findAll());

        return "editar-produto";
    }

    // Salva as alterações do produto
    @PostMapping("/atualizar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam BigDecimal preco,
            @RequestParam Integer estoque,
            @RequestParam(required = false) Long categoriaId,
            RedirectAttributes redirectAttributes) {

        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null) {
            redirectAttributes.addFlashAttribute("erro", "Produto não encontrado.");
            return "redirect:/admin/produtos";
        }

        nome = nome.trim();
        descricao = descricao.trim();

        if (nome.isBlank()) {
            redirectAttributes.addFlashAttribute("erro", "Informe o nome do produto.");
            return "redirect:/admin/produtos/editar/" + id;
        }

        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("erro", "O preço deve ser maior que zero.");
            return "redirect:/admin/produtos/editar/" + id;
        }

        if (estoque < 0) {
            redirectAttributes.addFlashAttribute("erro", "O estoque não pode ser negativo.");
            return "redirect:/admin/produtos/editar/" + id;
        }

        Categoria categoria = null;

        if (categoriaId != null) {
            categoria = categoriaRepository.findById(categoriaId).orElse(null);

            if (categoria == null) {
                redirectAttributes.addFlashAttribute("erro", "A categoria selecionada não existe.");
                return "redirect:/admin/produtos/editar/" + id;
            }
        }
       
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
        produto.setCategoria(categoria);

        produtoRepository.save(produto);

        redirectAttributes.addFlashAttribute("sucesso", "Produto atualizado com sucesso!");
        return "redirect:/admin/produtos";
    }
    
    // Exclui um produto pelo ID
    @PostMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Produto não encontrado.");
            return "redirect:/admin/produtos";
        }

        produtoRepository.delete(produto);

        redirectAttributes.addFlashAttribute(
                "sucesso", "Produto excluído com sucesso!");

        return "redirect:/admin/produtos";
    }
}