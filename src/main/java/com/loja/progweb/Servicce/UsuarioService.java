package com.loja.progweb.Servicce;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loja.progweb.Model.Usuario;
import com.loja.progweb.Repository.UsuarioRepository;


@Service 
public class UsuarioService implements UserDetailsService{

    public final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // para criação de um adm se não existir

    public UsuarioService(UsuarioRepository usuario, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuario;
        this.passwordEncoder = passwordEncoder;
    }



    public void salvar(Usuario usuario){
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
    }

    public Iterable<Usuario> listar(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public void deletarPorId(Long id){
        usuarioRepository.deleteById(id);
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                    new UsernameNotFoundException(
                        "Usuário não encontrado"
                    )
                );
        
        String perfil = usuario.getTipoUsuario().name();

        List<SimpleGrantedAuthority> authorities =
            List.of(
            new SimpleGrantedAuthority("ROLE_"+ perfil)
        );

        return new org.springframework.security.core.userdetails.User(
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.isAtivo(),
        true,
        true,
        true,
            authorities
        );

        //throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

}
