package com.example.springbootmongodb.configuration;

import com.example.springbootmongodb.controllers.TodoController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = TodoController.class)
public class SwaggerConfig {

    private Contact contact = new Contact("NyorJa", "", "botsot.felix@gmail.com");

    private ApiInfo getApiInfo(String scope, String description) {
        return new ApiInfoBuilder().title(scope).description(description).contact(contact).build();
    }


    @Bean
    public Docket csodApi() {
        ApiInfo apiInfo = getApiInfo("Localization Api", "Access the underlying data");
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/todos/**"))
                .build()
                .groupName("ToDo List Api")
                .apiInfo(apiInfo);
    }
}
