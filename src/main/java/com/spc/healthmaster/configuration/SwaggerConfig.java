package com.spc.healthmaster.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.spc.healthmaster")) // Reemplaza "com.example.yourpackage" con el paquete base de tu aplicación
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Documentation Health Master Monitor")
                .description("Mediante la API proporcionada, puedes realizar diversas acciones en tus aplicaciones de manera programática. " +
                        "Esto incluye iniciar y detener aplicaciones o grupos de aplicaciones según sea necesario, así como obtener el estado actual de cada una de ellas")
                .version("1.0.0")
                .build();
    }
}

