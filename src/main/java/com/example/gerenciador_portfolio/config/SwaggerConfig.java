package com.example.gerenciador_portfolio.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

   @Bean
   public OpenAPI defineOpenApi() {
       Server server = new Server();
       server.setUrl("http://localhost:8080");
       server.setDescription("Desenvolvimento");

       Contact myContact = new Contact();
       myContact.setName("Marcos Alencar");
       myContact.setEmail("marcos.12.01.07@gmail.com");

       Info information = new Info()
               .title("Sistema Para Gerenciamento de porojetos")
               .version("1.0")
               .description("A API expõe endpoints para gerenciar membros e projetos")
               .contact(myContact);

        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("basic");



        return new OpenAPI().info(information).servers(List.of(server))
                    .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                    .components(new io.swagger.v3.oas.models.Components()
                    .addSecuritySchemes("basicAuth", securityScheme));
    }
    
}
