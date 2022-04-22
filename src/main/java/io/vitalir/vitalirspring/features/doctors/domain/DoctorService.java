package io.vitalir.vitalirspring.features.doctors.domain;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<Doctor> getAll();

    Optional<Doctor> getDoctorById(long id);

    Optional<Long> addDoctor(Doctor doctor);

    Optional<Doctor> removeDoctorById(long id);
}
