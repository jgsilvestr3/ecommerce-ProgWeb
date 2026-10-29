package com.loja.progweb.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping ("/usuario/excluir/{id}")
    public String excluir(@PathVariable Long id){
        usuarioService.deletarPorId(id);
        return "redirect:/usuarios";
    }

    @PostMapping ("/usuario/salvar")
    public String salvar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult resultado){

        usuario.setDataCadastro(
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        );

        if (resultado.hasErrors()) {

            return "cadastro-usuario";
        }
        usuarioService.salvar(usuario);
        return "redirect:/usuarios";
    }
    
    @PostMapping ("/cliente/salvar")
    public String salvarCliente(@Valid @ModelAttribute ("usuario") Usuario usuario, BindingResult resultado, Model model){
        
        usuario.setDataCadastro(
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        );
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setAtivo(true);

        if (resultado.hasErrors()) {
            model.addAttribute("modoAdmin", false);
            return "cadastro-usuario";
        }

        usuarioService.salvar(usuario);
        return "redirect:/login";
    }




}
