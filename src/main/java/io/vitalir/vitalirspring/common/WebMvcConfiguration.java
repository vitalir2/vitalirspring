package io.vitalir.vitalirspring.common;

import io.vitalir.vitalirspring.features.doctors.presentation.MedicalSpecialtyConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MedicalSpecialtyConverter());
    }
}
