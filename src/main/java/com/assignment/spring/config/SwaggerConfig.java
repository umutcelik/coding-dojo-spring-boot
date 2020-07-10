package com.assignment.spring.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration.
 * Configures swagger ui for http://localhost:8080/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    /**
     * Swagger docket definition.
     *
     * @return Swagger docket.
     */
    @Bean
    public Docket api() {

        final List<ResponseMessage> getMessages = Arrays.asList(
            new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase()).build()
         );

        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .globalResponseMessage(RequestMethod.GET, getMessages)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.assignment.spring.api"))
            .paths(PathSelectors.any())
            .build();
    }

    @Override
    protected void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}