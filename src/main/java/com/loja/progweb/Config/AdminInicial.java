package com.loja.progweb.Config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.loja.progweb.Model.TipoUsuario;
import com.loja.progweb.Model.Usuario;
import com.loja.progweb.Repository.UsuarioRepository;

/** Garante um administrador para o primeiro acesso (só cria se o e-mail ainda não existir). */
@Component
public class AdminInicial implements CommandLineRunner {

    private static final String EMAIL = "admin@loja.com";
    private static final String SENHA = "admin123";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInicial(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail(EMAIL).isPresent()) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setCpf("000.000.000-00");
        admin.setEmail(EMAIL);
        admin.setTelefone("(00) 00000-0000");
        admin.setAtivo(true);
        admin.setDataCadastro(LocalDate.now().toString());
        admin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        admin.setSenha(passwordEncoder.encode(SENHA));
        usuarioRepository.save(admin);
        System.out.println(">>> Admin inicial criado: " + EMAIL + " / " + SENHA);
    }
}