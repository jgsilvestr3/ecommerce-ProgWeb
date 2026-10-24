package com.loja.progweb;

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
                .requestMatchers("/usuarios", "/login","/css/**", "/js/**",
                                 "/images/**", "/novo/usuario", "/usuario/salvar", "/").permitAll()

                // caminhos privados administrador
                .requestMatchers("/usuarios", "/admin/**")
                    .hasRole("ADMINISTRADOR")

                // caminho dos funcionários
                .requestMatchers("/usuarios", "/funcionario/**")
                    .hasAnyRole("ADMINISTRADOR", "FUNCIONARIO")

                .requestMatchers("/cliente/**")
                    .hasAnyRole("ADMINISTRADOR", "CLIENTE")

                .requestMatchers("/", "/login", "/cadastro", "/cadastro/salvar",
                    "/produtos", "/carrinho", "/imagem/**", "/teste/produtos",
                    "/css/**", "/js/**", "/images/**", "/error").permitAll()

                // demais páginas não exigem autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/usuarios", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
    

}
