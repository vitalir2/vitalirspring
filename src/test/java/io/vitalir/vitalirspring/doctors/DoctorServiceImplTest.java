package io.vitalir.vitalirspring.doctors;

import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorService;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorServiceImpl;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

    @Test
    void whenRemoveDoctorByIdWhichExists_returnItAndRemove() {
        given(doctorRepository.findById(anyLong())).willReturn(Optional.of(DOCTOR));

        var result = doctorService.removeDoctorById(DOCTOR.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(DOCTOR.getId());
        verify(doctorRepository).deleteById(DOCTOR.getId());
    }

    @Test
    void whenRemoveDoctorByIdWhichDoesNotExist_returnEmpty() {
        given(doctorRepository.findById(anyLong())).willReturn(Optional.empty());

        var result = doctorService.removeDoctorById(DOCTOR.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void whenChangeValidDoctorById_returnItsId() {
        given(doctorRepository.save(DOCTOR)).willReturn(DOCTOR);

        var result = doctorService.changeDoctor(DOCTOR);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(DOCTOR.getId());
    }

    @Test
    void whenChangeInvalidDoctorById_returnEmpty() {
        var result = doctorService.changeDoctor(INVALID_DOCTOR);

        assertThat(result).isEmpty();
    }

    @Test
    void whenGetDoctorsBySpecWhichExist_returnThem() {
        given(doctorRepository.findBySpecialty(MedicalSpecialty.CARDIOLOGY)).willReturn(List.of(DOCTOR));

        var result = doctorService.getDoctorsBySpecialty(MedicalSpecialty.CARDIOLOGY);

        assertThat(result).isNotNull();
        assertThat(result.get(0)).isEqualTo(DOCTOR);
    }
}
