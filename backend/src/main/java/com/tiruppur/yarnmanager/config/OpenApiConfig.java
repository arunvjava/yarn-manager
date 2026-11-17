package com.tiruppur.yarnmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tiruppurErpOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Yarn Manager - Tiruppur Knitwear & Garment ERP")
                        .description("RESTful API suite tracking the complete lifecycle from Yarn procurement, circular knitting, dyeing job-work DC, cutting bundles, sewing line output, to carton dispatch.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tiruppur Textile Tech Ops")
                                .email("tech@tiruppurtextiles.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
