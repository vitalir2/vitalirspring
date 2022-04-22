package io.vitalir.vitalirspring.features.doctors.data;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDoctorRepository extends CrudRepository<Doctor, Long>, DoctorRepository {

    @Override
    @Query("SELECT d FROM Doctor d JOIN d.medicalSpecialties s WHERE s = :specialty")
    List<Doctor> findBySpecialty(@Param("specialty") MedicalSpecialty medicalSpecialty);
}
