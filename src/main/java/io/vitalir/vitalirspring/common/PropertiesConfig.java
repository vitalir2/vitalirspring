package io.vitalir.vitalirspring.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Value("${jwt_secret}")
    private String jwtSecret;

    @Value("#{new Boolean('${vitalirspring.enable-swagger-security}')}")
    private Boolean isSwaggerSecurityEnabled;

    @Bean("jwt_secret")
    public String jwtSecret() {
        return jwtSecret;
    }

    @Bean("enable-swagger-security")
    public boolean isSwaggerSecurityEnabled() {
        return isSwaggerSecurityEnabled;
    }
}
