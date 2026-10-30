package com.loja.progweb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Usa um banco em memória SÓ nos testes, para não mexer no arquivo ./data/loja da aplicação
// (assim dá para rodar os testes mesmo com a aplicação aberta).
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:teste;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProgwebApplicationTests {

    @Test
    void contextLoads() {
    }

}
