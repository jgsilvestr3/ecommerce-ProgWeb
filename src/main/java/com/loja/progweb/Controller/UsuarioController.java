package com.loja.progweb.Controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.loja.progweb.Model.TipoUsuario;
import com.loja.progweb.Model.Usuario;
import com.loja.progweb.Servicce.UsuarioService;

import jakarta.validation.Valid;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ---------- Cadastro público: o visitante vira sempre CLIENTE ----------

    @GetMapping("/cadastro")
    public ModelAndView cadastro() {
        return new ModelAndView("cadastro-usuario")
                .addObject("usuario", new Usuario())
                .addObject("modoAdmin", false);
    }

    @PostMapping("/cadastro/salvar")
    public String cadastrar(@Valid @ModelAttribute("usuario") Usuario usuario,
                            BindingResult resultado, Model model) {
        usuario.setId(null); // o formulário não pode apontar para outra conta
        validarSenhaEEmail(usuario, resultado);
        if (resultado.hasErrors()) {
            model.addAttribute("modoAdmin", false);
            return "cadastro-usuario";
        }
        usuarioService.salvarCliente(usuario);
        return "redirect:/login?cadastrado";
    }

    // ---------- Área do ADMINISTRADOR ----------

    @GetMapping("/novo/usuario")
    public ModelAndView novoUsuario() {
        Usuario us = new Usuario();
        us.setAtivo(true);
        us.setDataCadastro(LocalDate.now().toString());
        us.setTipoUsuario(TipoUsuario.CLIENTE);
        return new ModelAndView("cadastro-usuario")
                .addObject("usuario", us)
                .addObject("modoAdmin", true);
    }

    @GetMapping("/usuarios")
    public ModelAndView listar() {
        return new ModelAndView("lista-usuario").addObject("usuarios", usuarioService.listar());
    }

    @GetMapping("/usuario/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        Usuario original = usuarioService.buscarPorId(id).orElseThrow();

        // cópia SEM a senha: o hash nunca vai para a tela
        Usuario us = new Usuario();
        us.setId(original.getId());
        us.setNome(original.getNome());
        us.setCpf(original.getCpf());
        us.setEmail(original.getEmail());
        us.setTelefone(original.getTelefone());
        us.setAtivo(original.isAtivo());
        us.setDataCadastro(original.getDataCadastro());
        us.setTipoUsuario(original.getTipoUsuario());
        us.setSenha("");

        return new ModelAndView("cadastro-usuario")
                .addObject("usuario", us)
                .addObject("modoAdmin", true);
    }

    // POST em vez de GET: um link ou imagem em outro site não consegue apagar usuários
    @PostMapping("/usuario/excluir/{id}")
    public String excluir(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        Usuario alvo = usuarioService.buscarPorId(id).orElseThrow();
        if (alvo.getEmail().equals(principal.getName())) {
            ra.addFlashAttribute("erro", "Você não pode excluir a sua própria conta.");
            return "redirect:/usuarios";
        }
        usuarioService.deletarPorId(id);
        return "redirect:/usuarios";
    }

    @PostMapping("/usuario/salvar")
    public String salvar(@Valid @ModelAttribute("usuario") Usuario usuario,
                         BindingResult resultado, Model model) {
        validarSenhaEEmail(usuario, resultado);
        if (resultado.hasErrors()) {
            model.addAttribute("modoAdmin", true);
            return "cadastro-usuario";
        }
        usuarioService.salvar(usuario);
        return "redirect:/usuarios";
    }

    // ---------- Validações em comum ----------

    private void validarSenhaEEmail(Usuario usuario, BindingResult resultado) {
        String senha = usuario.getSenha();
        boolean semSenha = senha == null || senha.isBlank();

        if (usuario.getId() == null && semSenha) {
            resultado.rejectValue("senha", "senha.obrigatoria", "Senha é obrigatória");
        } else if (!semSenha && senha.length() < 4) {
            resultado.rejectValue("senha", "senha.curta", "Deve conter no mínimo 4 caracteres");
        }

        if (usuario.getEmail() != null && usuarioService.emailEmUso(usuario.getEmail(), usuario.getId())) {
            resultado.rejectValue("email", "email.em.uso", "Este e-mail já está cadastrado");
        }
    }
}

