package io.swyp.luckybackend.common;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
				.title("LUCKY DAY!!")
				.description("Lucky Day Backend API Swagger UI")
				.version("0.0.1");
	}
}
