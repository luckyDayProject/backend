package io.swyp.luckybackend.common;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
	@Bean
	public OpenAPI openAPI() {
		SecurityScheme apiKey = new SecurityScheme()
				.type(SecurityScheme.Type.APIKEY)
				.in(SecurityScheme.In.HEADER)
				.name("Authorization")
				.scheme("Bearer")  // 추가: 스키마 타입 명시
				.bearerFormat("JWT");  // 추가: 베어러 포맷 명시

		SecurityRequirement securityRequirement = new SecurityRequirement()
				.addList("api_key", java.util.Arrays.asList("global"));  // "global"은 모든 연산에 대한 접근을 요구함을 의미
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("api_key", apiKey))
				.info(apiInfo())
				.addSecurityItem(securityRequirement);
	}

	private Info apiInfo() {
		return new Info()
				.title("LUCKY DAY!!")
				.description("Lucky Day Backend API Swagger UI")
				.version("0.0.1");
	}
}
