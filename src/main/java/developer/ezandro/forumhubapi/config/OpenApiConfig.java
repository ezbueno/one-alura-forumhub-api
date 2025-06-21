package developer.ezandro.forumhubapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME = "bearer-key";
    private static final String BEARER_FORMAT = "JWT";

    private static final String API_TITLE = "ForumHub API";
    private static final String API_DESCRIPTION =
            "REST API for the ForumHub application, providing user, topic, and course management with JWT-based authentication.";

    private static final String CONTACT_NAME = "Ezandro Bueno";
    private static final String CONTACT_URL = "https://github.com/ezbueno";

    private static final String LICENSE_NAME = "Apache 2.0";
    private static final String LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat(BEARER_FORMAT)))
                .info(new Info()
                        .title(API_TITLE)
                        .description(API_DESCRIPTION)
                        .contact(new Contact()
                                .name(CONTACT_NAME)
                                .url(CONTACT_URL))
                        .license(new License()
                                .name(LICENSE_NAME)
                                .url(LICENSE_URL)));
    }
}