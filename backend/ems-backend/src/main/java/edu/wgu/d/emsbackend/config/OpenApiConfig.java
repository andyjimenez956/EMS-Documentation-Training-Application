package edu.wgu.d.emsbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BASIC_AUTH_SCHEME = "basicAuth";

    @Bean
    public OpenAPI emsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("EMS Documentation Training Application API")
                        .version("v1")
                        .description("Backend API for EMS student documentation practice and instructor reporting. Swagger UI is the Task 3 GUI."))
                .schemaRequirement(BASIC_AUTH_SCHEME, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"))
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH_SCHEME));
    }
}
