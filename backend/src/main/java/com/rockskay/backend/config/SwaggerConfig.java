package com.rockskay.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =   @Info(
                title = "Rockskay API",
                version = "v1",
                description = "REST APIs for Rockskay",
                contact = @Contact(
                        name = "Rockskay Team"
                )
        )
)
public class SwaggerConfig {
}
