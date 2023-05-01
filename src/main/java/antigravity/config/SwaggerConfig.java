package antigravity.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	info = @Info(
		title = "피팅노트 API 명세",
		description = "피팅노트 API 문서",
		version = "v1",
		contact = @Contact(
			name = "shiningcastle",
			email = "shiningcastle.dev@gmail.com"
		)
	)
)
@Configuration
public class SwaggerConfig {

}

