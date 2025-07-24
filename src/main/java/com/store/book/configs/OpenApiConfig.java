package com.store.book.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer addAcceptLanguageHeader() {
        return openApi -> {
            HeaderParameter acceptLanguageHeader = (HeaderParameter) new HeaderParameter()
                    .name("Accept-Language")
                    .description("Language for localization")
                    .example("az")
                    .required(false)
                    .schema(new StringSchema());

            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }

            openApi.getPaths().values().forEach(pathItem -> {
                pathItem.readOperations().forEach(operation -> {
                    operation.addParametersItem(acceptLanguageHeader);
                });
            });
        };
    }
}
