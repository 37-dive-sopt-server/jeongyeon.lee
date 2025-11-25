package org.sopt.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "Bofit 백엔드 API 명세서 ",
                description = "Springdoc을 이용한 Swagger API 문서입니다.",
                version = "1.0"
        )
)

@Configuration
public class SwaggerConfig {
    private final String securitySchemaName = "JWT";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(setComponents())
                .addSecurityItem(setSecurityItems());
    }
    private Components setComponents() {
        return new Components()
                .addSecuritySchemes(securitySchemaName, bearerAuth());
    }

    private SecurityScheme bearerAuth() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);
    }

    private SecurityRequirement setSecurityItems() {
        return new SecurityRequirement()
                .addList(securitySchemaName);
    }
}
