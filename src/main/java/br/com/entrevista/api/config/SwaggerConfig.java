package br.com.entrevista.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenApi() {

		return new OpenAPI().components(new Components())
				.info(new Info().title("Learning Management System (LMS)")
						.description("Aplicação fullstack de Learning Management System (LMS)").version("v1"));
	}
}