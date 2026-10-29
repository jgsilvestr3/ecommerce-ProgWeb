
package com.loja.progweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.loja.progweb.Model.Categoria;
import com.loja.progweb.Repository.CategoriaRepository;
import com.loja.progweb.Repository.ProdutoRepository;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaAdminController {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaAdminController(
            CategoriaRepository categoriaRepository,
            ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    // Exibe a página de gerenciamento de categorias
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "admin-categorias";
    }

    // Cadastra uma nova categoria
    @PostMapping("/salvar")
    public String salvar(
            @RequestParam String nome,
            RedirectAttributes redirectAttributes) {

        nome = nome.trim();

        if (nome.isBlank()) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Digite o nome da categoria.");
            return "redirect:/admin/categorias";
        }

        if (categoriaRepository.existsByNomeIgnoreCase(nome)) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Já existe uma categoria com esse nome.");
            return "redirect:/admin/categorias";
        }

        categoriaRepository.save(new Categoria(nome));

        redirectAttributes.addFlashAttribute(
                "sucesso", "Categoria cadastrada com sucesso.");

        return "redirect:/admin/categorias";
    }

    // Exclui uma categoria somente se ela não tiver produtos associados
    @PostMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Categoria categoria = categoriaRepository.findById(id).orElse(null);

        if (categoria == null) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Categoria não encontrada.");
            return "redirect:/admin/categorias";
        }

        if (produtoRepository.existsByCategoriaId(id)) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Não é possível excluir uma categoria que possui produtos.");
            return "redirect:/admin/categorias";
        }

        categoriaRepository.delete(categoria);

        redirectAttributes.addFlashAttribute(
                "sucesso", "Categoria excluída com sucesso.");

        return "redirect:/admin/categorias";
    }
}
