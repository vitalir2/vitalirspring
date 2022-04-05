package io.vitalir.vitalirspring;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API частной поликлиники",
                version = "1.0",
                description = "Ч"
        )
)
public class VitalirspringApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitalirspringApplication.class, args);
    }
}
