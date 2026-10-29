package com.loja.progweb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http //Caminhos públicos
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/produtos", "/js/**",
                                 "/images/**", "/cadastro", "/cliente/salvar", "/").permitAll()

                // caminhos privados administrador
                .requestMatchers( "/usuario/salvar", "/novo/usuario", "/usuarios", "/admin/**")
                    .hasRole("ADMINISTRADOR")

                // caminho dos funcionários
                .requestMatchers( "/funcionario/**", "/produtos/novo" )
                    .hasAnyRole("ADMINISTRADOR", "FUNCIONARIO")
                // caminhos cliente
                .requestMatchers("/cliente/**")
                    .hasAnyRole("ADMINISTRADOR", "CLIENTE")

                // demais páginas não exigem autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .successHandler((request, response, authentication) -> {
                    // Pega as permissões (roles) do usuário logado
                    var roles = authentication.getAuthorities();
                    
                    // Converte as roles para texto para facilitar a checagem
                    String targetUrl = "/produtos"; // Rota padrão se nada bater

                    for (var authority : roles) {
                        String roleName = authority.getAuthority();
                        
                        if (roleName.equals("ROLE_ADMINISTRADOR")) {
                            targetUrl = "/usuarios"; // Rota para Administrador
                            break;
                        } else if (roleName.equals("ROLE_CLIENTE")) {
                            targetUrl = "/carrinho"; // Rota para cliente
                            break;
                        }
                    }
                    
                    // Executa o redirecionamento para a rota definida
                    response.sendRedirect(targetUrl);
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
            .accessDeniedPage("/acesso-negado") // Define a URL da sua página personalizada
            );

        return http.build();
    }
    

}
