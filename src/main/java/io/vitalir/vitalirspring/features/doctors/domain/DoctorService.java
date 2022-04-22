package io.vitalir.vitalirspring.features.doctors.domain;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<Doctor> getAll();

    Optional<Doctor> getDoctorById(long id);

    Optional<Long> addDoctor(Doctor doctor);

    Optional<Doctor> removeDoctorById(long id);

    Optional<Long> changeDoctor(Doctor doctor);

    List<Doctor> getDoctorsBySpecialty(MedicalSpecialty specialty);
}
