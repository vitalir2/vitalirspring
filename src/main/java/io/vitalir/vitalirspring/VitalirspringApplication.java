package io.vitalir.vitalirspring;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

/**
- Create JWT filter
- Create JWT mechanism
- Create UserController
- Create LoginController
- Create RegistrationController
- Create UserService
- Create UserRepository
- User -> id, login, password, email, firstName, lastName, patronymic?, birthDate
- RegistrationRequest -> login, password, firstName, last...
- LoginRequest -> login or email, password
- Add two roles: Admin (vitalir) and Client
- Admin -> post, delete, put for many things; other with logged in client -> user; others -> any
- App config for testing / simple
- Swagger for controllers
- Research: testing Swagger docs
 */
