package com.loja.progweb.Servicce;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loja.progweb.Model.TipoUsuario;
import com.loja.progweb.Model.Usuario;
import com.loja.progweb.Repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    public final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuario, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuario;
        this.passwordEncoder = passwordEncoder;
    }

    // Cadastro feito pelo ADMINISTRADOR (pode escolher o tipo)
    public void salvar(Usuario usuario) {
        boolean semSenhaNova = usuario.getSenha() == null || usuario.getSenha().isBlank();
        if (usuario.getId() != null && semSenhaNova) {
            // edição sem trocar a senha: mantém o hash que já estava no banco
            String senhaAtual = usuarioRepository.findById(usuario.getId()).orElseThrow().getSenha();
            usuario.setSenha(senhaAtual);
        } else {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        usuarioRepository.save(usuario);
    }

    // Cadastro PÚBLICO: o servidor decide tudo que é sensível, não o formulário
    public void salvarCliente(Usuario usuario) {
        usuario.setId(null);                       // nunca sobrescrever conta existente
        usuario.setTipoUsuario(TipoUsuario.CLIENTE); // ninguém se cadastra como admin
        usuario.setAtivo(true);
        usuario.setDataCadastro(LocalDate.now().toString());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
    }

    public boolean emailEmUso(String email, Long idAtual) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> idAtual == null || !u.getId().equals(idAtual))
                .isPresent();
    }

    public Iterable<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().name()));

        // o 3º argumento (enabled) faz o checkbox "Ativo" valer: usuário inativo não entra
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.isAtivo(),
                true, true, true,
                authorities);
    }
}