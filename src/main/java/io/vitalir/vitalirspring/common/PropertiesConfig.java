package io.vitalir.vitalirspring.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Value("${jwt_secret}")
    private String jwtSecret;

    @Bean("jwt_secret")
    public String jwtSecret() {
        return jwtSecret;
    }
}
