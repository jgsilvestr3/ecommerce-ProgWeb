
package com.loja.progweb.Config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracaoImagens implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Define a pasta onde as imagens são armazenadas
        Path pastaImagens = Paths.get("uploads/produtos")
                .toAbsolutePath()
                .normalize();

        // Permite acessar as imagens pelo navegador
        registry.addResourceHandler("/imagens/produtos/**")
                .addResourceLocations(pastaImagens.toUri().toString());
    }
}
