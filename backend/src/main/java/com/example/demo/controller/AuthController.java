package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/cadastro")
    public Usuario cadastrar(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String senha = dados.get("senha");
        String role = dados.getOrDefault("role", "USER"); // se não vier role, assume USER
        return usuarioService.cadastrar(email, senha, role);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String senha = dados.get("senha");

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (!usuarioService.senhaValida(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail(), usuario.getRole());
        return Map.of("token", token, "role", usuario.getRole());
    }
}