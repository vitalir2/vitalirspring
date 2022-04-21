package io.vitalir.vitalirspring.features.doctors.domain;

import java.util.List;

public interface DoctorRepository {

    List<Doctor> findAll();
}
