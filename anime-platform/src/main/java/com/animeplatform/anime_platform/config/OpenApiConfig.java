package com.animeplatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI animePlatformOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Anime Platform API")
                        .version("1.0.0")
                        .description("REST API for managing movies, anime, " + "series, episodes and multimedia files.")
                        .contact(new Contact().name("Anime Platform Team")));
    }
}