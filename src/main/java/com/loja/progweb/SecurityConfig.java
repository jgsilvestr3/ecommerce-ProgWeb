
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // Páginas e recursos públicos
                .requestMatchers(
                    "/",
                    "/login",
                    "/cadastro",
                    "/cadastro/salvar",
                    "/novo/usuario",
                    "/usuario/salvar",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/imagem/**",
                    "/error"
                ).permitAll()

                // Rotas exclusivas do administrador
                .requestMatchers("/admin/**")
                    .hasRole("ADMINISTRADOR")

                // Rotas compartilhadas por administrador e funcionário
                .requestMatchers("/funcionario/**")
                    .hasAnyRole("ADMINISTRADOR", "FUNCIONARIO")

                // Rotas de clientes
                .requestMatchers("/cliente/**")
                    .hasAnyRole("ADMINISTRADOR", "CLIENTE")

                // Lista de usuários exclusiva do administrador
                .requestMatchers("/usuarios")
                    .hasRole("ADMINISTRADOR")

                // Outras rotas exigem autenticação
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
