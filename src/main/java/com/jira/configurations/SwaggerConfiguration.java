package com.jira.configurations;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import com.google.common.base.Predicate;
import net.bytebuddy.asm.Advice.Return;
import springfox.documentation.service.Contact;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;
import java.util.Collections;
import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	private ApiInfo apiInfo() {
		        return new ApiInfo("jira services  API", "Esta API permite recibir los informes obtenidos por Jira y procesarlos en la base de datos.", "API v1.0.1", "Terms of service",
		          new Contact("QUBIST", "http://localhost:8080/swagger-ui", "joseluis.israel@machette.tech"),
		          "License of API", "API license URL", Collections.emptyList());
		    }
    
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
          .select()
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build();
    }
	
	 @Bean
	    UiConfiguration uiConfig() {
		 
			        return UiConfigurationBuilder.builder()
			          .deepLinking(true)
			          .displayOperationId(false)
			          .defaultModelsExpandDepth(1)
			          .defaultModelExpandDepth(1)
			          .defaultModelRendering(ModelRendering.EXAMPLE)
			          .displayRequestDuration(false)
			          .docExpansion(DocExpansion.NONE)
			          .filter(false)
			          .maxDisplayedTags(null)
			          .operationsSorter(OperationsSorter.ALPHA)
			          .showExtensions(false)
			          .tagsSorter(TagsSorter.ALPHA)
			          //.supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
			          .validatorUrl(null)
			          .build();
			    }

			
	    
	
}
