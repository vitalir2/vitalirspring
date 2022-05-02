package io.vitalir.vitalirspring.common.properties;

import io.vitalir.vitalirspring.common.properties.PropertiesSecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Value("${jwt_secret}")
    private String jwtSecret;

    @Value("#{new Boolean('${vitalirspring.enable-swagger-security}')}")
    private Boolean isSwaggerSecurityEnabled;

    @Value("#{new Boolean('${vitalirspring.enable-actuator-security}')}")
    private Boolean isActuatorSecurityEnabled;

    @Value("#{new Boolean('${vitalirspring.enable-csrf}')}")
    private Boolean isCsrfEnabled;

    @Bean("jwt_secret")
    public String jwtSecret() {
        return jwtSecret;
    }

    @Bean
    public PropertiesSecurityConfig propertiesSecurityConfig() {
        return new PropertiesSecurityConfig(
                isSwaggerSecurityEnabled,
                isActuatorSecurityEnabled,
                isCsrfEnabled
        );
    }
}
