package io.vitalir.vitalirspring.doctors;

import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorService;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplTest extends DoctorFeatureTest {

    @Mock
    private DoctorRepository doctorRepository;

    private DoctorService doctorService;

    @BeforeEach
    void init() {
        doctorService = new DoctorServiceImpl(doctorRepository);
    }

    @Test
    void whenGetAllDoctors_returnThem() {
        given(doctorRepository.findAll()).willReturn(List.of(DOCTOR));

        var result = doctorService.getAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(DOCTOR);
    }

    @Test
    void whenGetDoctorByIdWhichExists_returnIt() {
        given(doctorRepository.findById(DOCTOR.getId())).willReturn(Optional.of(DOCTOR));

        var result = doctorService.getDoctorById(DOCTOR.getId());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(DOCTOR);
    }

    @Test
    void whenGetDoctorByIdWhichDoesNotExist_returnEmpty() {
        given(doctorRepository.findById(DOCTOR.getId())).willReturn(Optional.empty());

        var result = doctorService.getDoctorById(DOCTOR.getId());

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void whenAddValidDoctorById_returnItsId() {
        given(doctorRepository.save(DOCTOR)).willReturn(DOCTOR);

        var result = doctorService.addDoctor(DOCTOR);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(DOCTOR.getId());
    }

    @Test
    void whenAddInvalidDoctorById_returnEmpty() {
        var result = doctorService.addDoctor(INVALID_DOCTOR);

        assertThat(result).isEmpty();
    }
}
