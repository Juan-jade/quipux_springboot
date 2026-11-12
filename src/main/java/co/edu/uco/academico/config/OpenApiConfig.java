package co.edu.uco.academico.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("API Sistema Académico UCO")
                .version("1.0.0")
                .description("Backend con paneles de estudiante y profesor: notas, asistencia (llegadas tarde) "
                        + "y puntos positivos/negativos. Integrantes: [NOMBRE 1], [NOMBRE 2], [NOMBRE 3]")
                .contact(new Contact().name("Equipo UCO").email("equipo@uco.edu.co")));
    }
}
