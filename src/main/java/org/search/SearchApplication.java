package org.search;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SearchApplication {
	
	public static final Set<String> DEFAULT_PRODUCES_CONSUMES = 
			new HashSet<String>(Arrays.asList("application/json"));

	public static void main(String args[]) {
		
		SpringApplication.run(SearchApplication.class);
	}
	
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("SearchApplication")
				.apiInfo(apiInfo())
				.produces(DEFAULT_PRODUCES_CONSUMES)
				.consumes(DEFAULT_PRODUCES_CONSUMES);
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("UrlShortner APIs")
				.build();
	}
	
}
