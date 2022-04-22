package io.vitalir.vitalirspring.features.doctors.domain;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository {

    List<Doctor> findAll();

    Optional<Doctor> findById(long id);

    Doctor save(Doctor doctor);

    void deleteById(long id);
}
