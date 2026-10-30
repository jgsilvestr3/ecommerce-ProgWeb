package com.loja.progweb.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
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

import org.springframework.ui.Model;
import jakarta.validation.Valid;

@Controller 
public class UsuarioController {


    private UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Cadatro público de cliente
    @GetMapping("/cadastro")
    public ModelAndView cadastrarCliente(){
        ModelAndView mv = new ModelAndView("cadastro-usuario");

        Usuario usuario = new Usuario();
        mv.addObject("usuario", usuario).addObject("modoAdmin", false);


        return mv;
    }

    @GetMapping("/novo/usuario")
    public ModelAndView novoUsuario(){

        Usuario us = new Usuario();
        us.setAtivo(true);
        us.setTipoUsuario(TipoUsuario.CLIENTE);

        return new ModelAndView("cadastro-usuario").addObject("usuario", us).addObject("modoAdmin", true);
    }

    @GetMapping("/usuarios")
    public ModelAndView listar(){

        return new ModelAndView("lista-usuario").addObject( "usuarios", usuarioService.listar());

    }

    @GetMapping ("/usuario/editar/{id}")
    public ModelAndView editar(@PathVariable Long id){

        Usuario us = new Usuario();
        us = usuarioService.buscarPorId(id).orElseThrow();
        return new ModelAndView("cadastro-usuario").addObject("usuario", us).addObject("modoAdmin", true);

    }

    // POST (e não GET): um link ou imagem em outro site não consegue apagar usuários
    @PostMapping("/usuario/excluir/{id}")
    public String excluir(@PathVariable Long id, Principal principal, RedirectAttributes ra){
        Usuario alvo = usuarioService.buscarPorId(id).orElseThrow();

        if (alvo.getEmail().equals(principal.getName())) {
            ra.addFlashAttribute("erro", "Você não pode excluir a sua própria conta.");
            return "redirect:/usuarios";
        }
        try {
            usuarioService.deletarPorId(id);
            ra.addFlashAttribute("sucesso", "Usuário excluído.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", "Este usuário tem pedidos e não pode ser excluído. Desative-o no cadastro.");
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/usuario/salvar")
    public String salvar(@Valid @ModelAttribute("usuario") Usuario usuario,
                     BindingResult resultado, Model model) {

        validarSenha(usuario, resultado);
        if (usuario.getTipoUsuario() == null) {
            resultado.rejectValue("tipoUsuario", "tipo.obrigatorio", "Selecione o tipo de usuário");
        }

        if (resultado.hasErrors()) {
            model.addAttribute("modoAdmin", true); // era isso que faltava
            return "cadastro-usuario";
        }

        if (usuario.getId() == null) {
            usuario.setDataCadastro(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            // editar não pode mudar a data em que a conta foi criada
            usuario.setDataCadastro(usuarioService.buscarPorId(usuario.getId()).orElseThrow().getDataCadastro());
        }
        usuarioService.salvar(usuario);
        return "redirect:/usuarios";
    }
    
    @PostMapping ("/cliente/salvar")
    public String salvarCliente(@Valid @ModelAttribute ("usuario") Usuario usuario, BindingResult resultado, Model model){
        
        usuario.setId(null);

        usuario.setDataCadastro(
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        );
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setAtivo(true);

        validarSenha(usuario, resultado);
        if (resultado.hasErrors()) {
            model.addAttribute("modoAdmin", false);
            return "cadastro-usuario";
        }

        usuarioService.salvar(usuario);
        return "redirect:/login?cadastrado";
    }

    private void validarSenha(Usuario usuario, BindingResult resultado) {
        String senha = usuario.getSenha();
        boolean semSenha = senha == null || senha.isBlank();
        if (usuario.getId() == null && semSenha) {
            resultado.rejectValue("senha", "senha.obrigatoria", "Senha é obrigatória");
        } else if (!semSenha && senha.length() < 4) {
            resultado.rejectValue("senha", "senha.curta", "Deve conter no mínimo 4 caracteres");
        }
    }


}
