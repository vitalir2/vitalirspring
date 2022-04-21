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
}
