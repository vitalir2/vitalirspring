package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.features.appointment.domain.*;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void initBeforeEach() {
        appointmentService = new AppointmentServiceImpl(userRepository, appointmentRepository);
    }

    private static final long USER_ID = 1L;

    private static final Appointment APPOINTMENT = new Appointment();

    @Test
    void whenGetAppointmentsByUserIdWhichExist_returnThem() {
        given(userRepository.existsById(USER_ID))
                .willReturn(true);
        given(appointmentRepository.getAppointmentsByUserId(USER_ID))
                .willReturn(List.of(APPOINTMENT));

        var result = appointmentService.getAppointmentsByUserId(USER_ID);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(APPOINTMENT.getId());
    }

    @Test
    void whenGetAppointmentsByUserIdWhichDoesNotExist_throwInvalidUserId() {
        given(userRepository.existsById(USER_ID))
                .willReturn(false);

        assertThatThrownBy(() -> appointmentService.getAppointmentsByUserId(USER_ID))
                .isInstanceOf(IllegalUserIdException.class);
    }
}
