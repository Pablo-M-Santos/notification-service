package com.pablo.notification.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Springdoc OpenAPI (Swagger).
 * Gera a documentação interativa em /swagger-ui.html e o spec em /api-docs.
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:notification-service}")
    private String applicationName;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notification Service API")
                        .description("API RESTful para gerenciamento de notificações. "
                                + "Permite criar, enqueue e consultar notificações por ID ou por usuário (externalId). "
                                + "O envio real é processado de forma assíncrona via RabbitMQ.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Pablo")
                                .email("pablo@exemplo.com")
                        )
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/license/mit")
                        )
                );
    }
}