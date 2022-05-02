package io.vitalir.vitalirspring.common.properties;

public record PropertiesSecurityConfig(
        boolean isSwaggerSecurityEnabled,
        boolean isActuatorSecurityEnabled,
        boolean isCsrfEnabled
) {
}
