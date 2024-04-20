package io.swyp.luckybackend.common;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Configuration
public class OpenApiConfiguration {

	static {
		SpringDocUtils.getConfig().addAnnotationsToIgnore(
			RequestParam.class,
			RequestBody.class,
			PageableDefault.class,
			PathVariable.class
		);
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.components(new Components()
						.addSecuritySchemes("Authorization", new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name("Authorization"))).addSecurityItem(new SecurityRequirement().addList("Authorization"))
				.info(new Info()
				.title("LuckyDay API")
				.description("LuckyDay Service API")
				.version("1.0")
				.license(new License()
						.name("이 API는 LuckyDay Service에서 제공되는 Backend API입니다")
						.url("http://www.apache.org/licenses/LICENSE-2.0")
				)
		);
	}

}
