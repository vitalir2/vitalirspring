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
        doctor = new Doctor();
    }


    @Test
    void whenGetAllDoctors_returnThem() {
        testEntityManager.persist(doctor);

        var result = doctorRepository.findAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(doctor);
    }
}
