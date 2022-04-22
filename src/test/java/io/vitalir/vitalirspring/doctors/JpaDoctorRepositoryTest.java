package io.vitalir.vitalirspring.doctors;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class JpaDoctorRepositoryTest extends DoctorFeatureTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private DoctorRepository doctorRepository;

    private Doctor doctor;

    @BeforeEach
    void initModels() {
        doctor = new Doctor("name");
    }


    @Test
    void whenGetAllDoctors_returnThem() {
        testEntityManager.persist(doctor);

        var result = doctorRepository.findAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(doctor);
    }

    @Test
    void whenGetDoctorByIdWhichExists_returnIt() {
        testEntityManager.persist(doctor);

        var result = doctorRepository.findById(doctor.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(doctor.getId());
    }

    @Test
    void whenGetDoctorByIdWhichDoesNotExist_returnIt() {
        var result = doctorRepository.findById(doctor.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void whenAddDoctor_addIt() {
        doctorRepository.save(doctor);

        var result = testEntityManager.find(Doctor.class, doctor.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(doctor.getId());
    }

    @Test
    void whenRemoveDoctorWhichExists_removeIt() {
        var id = testEntityManager.persistAndGetId(doctor, Long.class);

        doctorRepository.deleteById(id);

        assertThat(testEntityManager.find(Doctor.class, id)).isNull();
    }
}
