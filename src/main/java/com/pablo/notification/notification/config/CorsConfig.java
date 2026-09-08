package com.pablo.notification.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:*}")
    private String[] allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (allowedOrigins.length == 1 && "*".equals(allowedOrigins[0])) {
                    registry.addMapping("/**")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                            .allowedHeaders("*")
                            .allowedOriginPatterns("*")
                            .allowCredentials(true)
                            .maxAge(3600);
                } else {
                    registry.addMapping("/**")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                            .allowedHeaders("*")
                            .allowedOrigins(allowedOrigins)
                            .allowCredentials(true)
                            .maxAge(3600);
                }
            }
        };
    }
}
