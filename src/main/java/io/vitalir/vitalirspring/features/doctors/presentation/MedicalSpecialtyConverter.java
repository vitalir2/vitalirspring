package io.vitalir.vitalirspring.features.doctors.presentation;

import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

// TODO: Will be better to make a map of id to specialty
public class MedicalSpecialtyConverter implements Converter<String, MedicalSpecialty> {

    @Override
    public MedicalSpecialty convert(@NonNull String source) {
        try {
            return MedicalSpecialty.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
