package io.vitalir.vitalirspring.features.doctors.data;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDoctorRepository extends CrudRepository<Doctor, Long>, DoctorRepository {
}
