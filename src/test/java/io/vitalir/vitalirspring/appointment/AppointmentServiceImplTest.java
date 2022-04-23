package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.features.appointment.domain.*;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest extends AppointmentFeatureTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void initBeforeEach() {
        appointmentService = new AppointmentServiceImpl(userRepository, appointmentRepository);
    }

    private static final Appointment APPOINTMENT = new Appointment();

    private static final long APPOINTMENT_ID = APPOINTMENT.getId();

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

    @Test
    void whenRemoveAppointmentByIdsWhichExists_returnRemovedOne() {
        given(userRepository.existsById(USER_ID))
                .willReturn(true);
        given(userRepository.getById(USER_ID))
                .willReturn(Optional.of(new User("", "", Role.USER, Set.of(APPOINTMENT))));

        var result = appointmentService.removeAppointmentByIds(USER_ID, APPOINTMENT_ID);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(APPOINTMENT_ID);
        verify(appointmentRepository).deleteById(APPOINTMENT_ID);
    }

    @Test
    void whenRemoveAppointmentByUserIdWhichDoesNotExist_throwInvalidUserId() {
        given(userRepository.existsById(USER_ID))
                .willReturn(false);

        assertThatThrownBy(() -> appointmentService.getAppointmentsByUserId(USER_ID))
                .isInstanceOf(IllegalUserIdException.class);
    }

    @Test
    void whenRemoveAppointmentByAppointmentIdWhichDoesNotExist_returnEmptyOne() {
        given(userRepository.existsById(USER_ID))
                .willReturn(true);
        given(userRepository.getById(USER_ID))
                .willReturn(Optional.of(new User("", "")));

        var result = appointmentService.removeAppointmentByIds(USER_ID, APPOINTMENT_ID);

        assertThat(result).isEmpty();
    }
}
