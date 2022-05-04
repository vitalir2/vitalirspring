package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.common.time.IntervalChecker;
import io.vitalir.vitalirspring.features.appointment.domain.*;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidUserIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidAppointmentIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidDoctorIdException;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import io.vitalir.vitalirspring.features.service.domain.ServiceRepository;
import io.vitalir.vitalirspring.features.user.domain.users.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest extends AppointmentFeatureTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ServiceRepository serviceRepository;

    private AppointmentService appointmentService;

    private final IntervalChecker intervalChecker = new IntervalChecker();

    @BeforeEach
    void initBeforeEach() {
        appointmentService = new AppointmentServiceImpl(userRepository,
                appointmentRepository,
                doctorRepository,
                serviceRepository,
                intervalChecker
        );
    }

    private static final Appointment APPOINTMENT = new Appointment();

    private static final long APPOINTMENT_ID = APPOINTMENT.getId();

    private static final long DOCTOR_ID = 2;

    private static final AddAppointmentRequest ADD_APPOINTMENT_REQUEST = new AddAppointmentRequest(
            DOCTOR_ID,
            SERVICE_ID,
            LocalDateTime.now(),
            60
    );

    private static final User USER = new User("", "");

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
                .isInstanceOf(InvalidUserIdException.class);
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
                .isInstanceOf(InvalidUserIdException.class);
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

    @Test
    void whenAddAppointmentWhichDoesNotExist_returnNewId() {
        given(doctorRepository.findById(DOCTOR_ID))
                .willReturn(Optional.of(DOCTOR));
        given(appointmentRepository.save(any()))
                .willReturn(APPOINTMENT);
        given(serviceRepository.findById(SERVICE_ID))
                .willReturn(Optional.of(SERVICE));

        var result = appointmentService.addAppointment(USER, ADD_APPOINTMENT_REQUEST);

        assertThat(result).isNotNull();
        verify(appointmentRepository).save(any());
    }

    @Test
    void whenAddAppointmentByDoctorIdWhichDoesNotExist_throwInvalidDoctorId() {
        given(doctorRepository.findById(DOCTOR_ID))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.addAppointment(USER, ADD_APPOINTMENT_REQUEST))
                .isInstanceOf(InvalidDoctorIdException.class);
    }

    @Test
    void whenAddAppointmentOnDateWhereAnotherOneExists_throwIllegalArg() {
        given(doctorRepository.findById(DOCTOR_ID))
                .willReturn(Optional.of(DOCTOR));
        given(serviceRepository.findById(SERVICE_ID))
                .willReturn(Optional.of(SERVICE));
        var requestOnExistingPeriod = new AddAppointmentRequest(
                DOCTOR_ID,
                SERVICE_ID,
                FIRST_APPOINTMENT.getStartDate().minusMinutes(15L),
                30L
        );

        assertThatThrownBy(
                () -> appointmentService.addAppointment(USER_WITH_APPOINTMENTS, requestOnExistingPeriod)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenChangeAppointmentByIdsWhichExist_returnThem() {
        var appointment = getAppointmentWithUser();
        given(userRepository.existsById(USER_ID))
                .willReturn(true);
        given(appointmentRepository.getAppointmentsByUserId(USER_ID))
                .willReturn(List.of((appointment)));
        given(doctorRepository.findById(DOCTOR_ID))
                .willReturn(Optional.of(DOCTOR));
        given(serviceRepository.findById(SERVICE_ID))
                .willReturn(Optional.of(SERVICE));

        var result = appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST);

        assertThat(result).isEqualTo(appointment.getId());
        verify(appointmentRepository).save(any());
    }

    @Test
    void whenChangeAppointmentByUserIdWhichDoesNotExist_throwInvalidAppointmentId() {
        given(userRepository.existsById(USER_ID))
                .willReturn(true);
        given(appointmentRepository.getAppointmentsByUserId(USER_ID))
                .willReturn(List.of((APPOINTMENT)));

        assertThatThrownBy(() -> appointmentService.changeAppointment(USER_ID, new ChangeAppointmentRequest(
                -1,
                -1,
                -1,
                LocalDateTime.now(),
                0L
        )))
                .isInstanceOf(InvalidAppointmentIdException.class);
    }

    @Test
    void whenChangeAppointmentByAppointmentIdWhichDoesNotExist_throwInvalidUserId() {
        given(userRepository.existsById(USER_ID))
                .willReturn(false);

        assertThatThrownBy(() -> appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .isInstanceOf(InvalidUserIdException.class);
    }

    @Test
    void whenChangeAppointmentByDoctorIdWhichDoesNotExist_throwInvalidDoctorId() {
        given(userRepository.existsById(USER_ID))
                .willReturn(true);
        given(appointmentRepository.getAppointmentsByUserId(USER_ID))
                .willReturn(List.of((APPOINTMENT)));
        given(doctorRepository.findById(DOCTOR_ID))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .isInstanceOf(InvalidDoctorIdException.class);
    }

    @Test
    void whenGetAppointmentsByIntervalWhenAllAppointmentsThere_returnThem() {
        var startDate = LocalDateTime.of(2022, 1, 14, 14, 30);
        var endDate = LocalDateTime.of(2022, 5, 14, 22, 0);

        var result = appointmentService.getAppointmentsForCurrentUserByParams(
                USER_WITH_APPOINTMENTS,
                startDate,
                endDate,
                null,
                null
        );

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(anyAppointmentContainsWithId(result, FIRST_APPOINTMENT.getId())).isTrue();
        assertThat(anyAppointmentContainsWithId(result, SECOND_APPOINTMENT.getId())).isTrue();
    }

    @Test
    void whenGetAppointmentsByIntervalWhenOnlyFirstAppointmentThere_returnIt() {
        var startDate = LocalDateTime.of(2022, 3, 14, 14, 30);
        var endDate = LocalDateTime.of(2022, 5, 14, 22, 0);

        var result = appointmentService.getAppointmentsForCurrentUserByParams(
                USER_WITH_APPOINTMENTS,
                startDate,
                endDate,
                null,
                null
        );

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(anyAppointmentContainsWithId(result, FIRST_APPOINTMENT.getId())).isTrue();
        assertThat(anyAppointmentContainsWithId(result, SECOND_APPOINTMENT.getId())).isFalse();
    }

    @Test
    void whenGetAppointmentsByIntervalWhenOnlyLastAppointmentThere_returnIt() {
        var startDate = LocalDateTime.of(2022, 2, 14, 14, 30);
        var endDate = LocalDateTime.of(2022, 3, 14, 22, 0);

        var result = appointmentService.getAppointmentsForCurrentUserByParams(
                USER_WITH_APPOINTMENTS,
                startDate,
                endDate,
                null,
                null
        );

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(anyAppointmentContainsWithId(result, FIRST_APPOINTMENT.getId())).isFalse();
        assertThat(anyAppointmentContainsWithId(result, SECOND_APPOINTMENT.getId())).isTrue();
    }

    @Test
    void whenGetAppointmentsByIntervalAndSpecWithAppointmentsThere_returnThem() {
        var startDate = LocalDateTime.of(2022, 2, 14, 14, 30);
        var endDate = LocalDateTime.of(2022, 3, 14, 22, 0);

        var result = appointmentService.getAppointmentsForCurrentUserByParams(
                USER_WITH_APPOINTMENTS,
                startDate,
                endDate,
                MedicalSpecialty.ENDOCRINOLOGY,
                null
        );

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(anyAppointmentContainsWithId(result, FIRST_APPOINTMENT.getId())).isFalse();
        assertThat(anyAppointmentContainsWithId(result, SECOND_APPOINTMENT.getId())).isTrue();
    }

    @Test
    void whenGetAppointmentsByIntervalAndSpecWithAppointmentsWithNoSpec_returnThem() {
        var startDate = LocalDateTime.of(2022, 2, 14, 14, 30);
        var endDate = LocalDateTime.of(2022, 3, 14, 22, 0);

        var result = appointmentService.getAppointmentsForCurrentUserByParams(
                USER_WITH_APPOINTMENTS,
                startDate,
                endDate,
                MedicalSpecialty.CARDIOLOGY,
                null
        );

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void whenGetAppointmentsByIntervalAndDoctorIdWithAppointmentsWithNoSpecifiedDoctor_returnEmpty() {
        var startDate = LocalDateTime.of(2022, 1, 14, 14, 30);
        var endDate = LocalDateTime.of(2022, 12, 14, 22, 0);

        var result = appointmentService.getAppointmentsForCurrentUserByParams(
                USER_WITH_APPOINTMENTS,
                startDate,
                endDate,
                null,
                1234L
        );

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void whenGetAppointmentsByIntervalWhenNoAppointmentsThere_returnEmpty() {
        var startDate = LocalDateTime.of(2022, 6, 14, 14, 30);
        var endDate = LocalDateTime.of(2022, 7, 14, 22, 0);

        var result = appointmentService.getAppointmentsForCurrentUserByParams(
                USER_WITH_APPOINTMENTS,
                startDate,
                endDate,
                null,
                null
        );

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void whenGetAppointmentsByIntervalWhichIsInvalid_throwIllegalArgument() {
        var startDate = LocalDateTime.of(2022, 4, 14, 14, 30);
        var endDate = LocalDateTime.of(2022, 3, 14, 22, 0);

        assertThatThrownBy(
                () -> appointmentService.getAppointmentsForCurrentUserByParams(
                        USER_WITH_APPOINTMENTS,
                        startDate,
                        endDate,
                        null,
                        null
                )
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    private boolean anyAppointmentContainsWithId(List<Appointment> list, long id) {
        return list.stream()
                .anyMatch(appointment -> appointment.getId() == id);
    }

    private static Appointment getAppointmentWithUser() {
        var appointment = new Appointment();
        appointment.setUser(USER);
        return appointment;
    }
}
