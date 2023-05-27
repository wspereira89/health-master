package com.spc.healthmaster.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${swagger.disabledBtn}")
    private boolean disabledBtn;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API Documentation Health Master Monitor")
                        .description("Mediante la API proporcionada, puedes realizar diversas acciones en tus aplicaciones de manera programática. " +
                                "Esto incluye iniciar y detener aplicaciones o grupos de aplicaciones según sea necesario, así como obtener el estado actual de cada una de ellas")
                        .version("1.0.0"));
    }

    @Primary
    @Bean
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties properties = new SwaggerUiConfigProperties();
        properties.setDefaultModelRendering("example");
        properties.setSupportedSubmitMethods(disabledBtn ? Arrays.asList(new String[]{}) : null);
        return properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}