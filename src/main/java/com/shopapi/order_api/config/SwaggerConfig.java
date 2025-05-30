/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.shopapi.order_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author kevin
 */
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)@OpenAPIDefinition(
    servers = {
        @Server(url = "")
    }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiDocConfig() {

        return new OpenAPI()
                .info(new Info()
                        .title("API REST User Management")
                        .description("API Rest User Management")
                        .version("1.0.0")
                        .contact(
                                new Contact()
                                        .name("Kevin Ortega")
                                        .email("kevinantonioortegaparra10@gmail.com")
                                        
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("linkedin")
                        .url("www.linkedin.com/in/kevin-antonio-ortega-parra-602206327"));
    }

}
