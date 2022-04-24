package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.common.HttpEndpoints;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentService;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidAppointmentIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidDoctorIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidUserIdException;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.presentation.AppointmentController;
import io.vitalir.vitalirspring.features.user.domain.CurrentUserService;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@ActiveProfiles("test")
public class AppointmentControllerTest extends AppointmentFeatureTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private CurrentUserService currentUserService;

    @Test
    void whenGetAppointmentsByExistingUserId_returnIt() throws Exception {
        given(appointmentService.getAppointmentsByUserId(USER_ID))
                .willReturn(List.of(APPOINTMENT));
        var requestBuilder = get(HttpEndpoints.APPOINTMENT_ENDPOINT + USER_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo((int) APPOINTMENT.getId())));
    }

    @Test
    void whenGetAppointmentsByNotExistingUserId_returnBadRequest() throws Exception {
        given(appointmentService.getAppointmentsByUserId(USER_ID))
                .willThrow(new InvalidUserIdException());
        var requestBuilder = get(HttpEndpoints.APPOINTMENT_ENDPOINT + USER_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void whenRemoveAppointmentByIdsWhichExists_returnRemovedAppointment() throws Exception {
        setupMockUser();
        given(appointmentService.removeAppointmentByIds(anyLong(), anyLong()))
                .willReturn(Optional.of(APPOINTMENT));
        var requestBuilder = delete(HttpEndpoints.APPOINTMENT_ENDPOINT +
                APPOINTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo((int) APPOINTMENT.getId())));
        verify(appointmentService).removeAppointmentByIds(anyLong(), anyLong());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void whenRemoveAppointmentByUserIdWhichDoesNotExist_returnBadRequest() throws Exception {
        setupMockUser();
        given(appointmentService.removeAppointmentByIds(anyLong(), anyLong()))
                .willThrow(new InvalidUserIdException());
        var requestBuilder = delete(HttpEndpoints.APPOINTMENT_ENDPOINT +
                APPOINTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void whenRemoveAppointmentByAppointmentIdWhichDoesNotExist_returnBadRequest() throws Exception {
        setupMockUser();
        given(appointmentService.removeAppointmentByIds(anyLong(), anyLong()))
                .willReturn(Optional.empty());
        var requestBuilder = delete(HttpEndpoints.APPOINTMENT_ENDPOINT +
                APPOINTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddAppointmentByIdsWhichExist_returnAddedAppointmentId() throws Exception {
        setupMockUser();
        given(appointmentService.addAppointment(any(), any()))
                .willReturn(APPOINTMENT_ID);
        var requestBuilder = post(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(ADD_APPOINTMENT_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", equalTo((int) APPOINTMENT_ID)));
        verify(appointmentService).addAppointment(any(), any());
    }

    @Test
    void whenAddAppointmentByUserIdWhichDoesNotExist_returnBadRequest() throws Exception {
        setupMockUser();
        given(appointmentService.addAppointment(any(), any()))
                .willThrow(new InvalidUserIdException());
        var requestBuilder = post(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(ADD_APPOINTMENT_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddAppointmentByDoctorIdWhichDoesNotExist_returnBadRequest() throws Exception {
        var addAppointmentRequest = new AddAppointmentRequest(
                2,
                LocalDateTime.now(),
                1000 * 60 * 15,
                "A description"
        );
        setupMockUser();
        given(appointmentService.addAppointment(any(), any()))
                .willThrow(new InvalidDoctorIdException());
        var requestBuilder = post(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(addAppointmentRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenChangeAppointmentWhichExists_changeIt() throws Exception {
        setupMockUser();
        given(appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .willReturn(APPOINTMENT_ID);
        var requestBuilder = put(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(CHANGE_APPOINTMENT_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo((int) APPOINTMENT_ID)));
    }

    @Test
    void whenChangeAppointmentByUserIdWhichDoesNotExist_returnBadRequest() throws Exception {
        setupMockUser();
        given(appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .willThrow(new InvalidUserIdException());
        var requestBuilder = put(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(CHANGE_APPOINTMENT_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenChangeAppointmentByAppointmentIdWhichDoesNotExist_returnBadRequest() throws Exception {
        setupMockUser();
        given(appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .willThrow(new InvalidAppointmentIdException());
        var requestBuilder = put(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(CHANGE_APPOINTMENT_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetCurrentUserAppointmentsByDate_returnThem() throws Exception {
        setupMockUser(USER_WITH_APPOINTMENTS);
        given(appointmentService.getAppointmentsInInterval(
                any(),
                any(),
                any()
        )).willReturn(List.of(FIRST_APPOINTMENT, SECOND_APPOINTMENT));
        LocalDateTime startDate = LocalDateTime.of(2022, 3, 15, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 4, 25, 15, 30);
        var requestBuilder = get(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("startDate", startDate.toString())
                .queryParam("endDate", endDate.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenGetCurrentUserAppointmentsByInvalidDate_returnThem() throws Exception {
        setupMockUser(USER_WITH_APPOINTMENTS);
        given(appointmentService.getAppointmentsInInterval(
                any(),
                any(),
                any()
        )).willThrow(new IllegalArgumentException("Kek"));
        LocalDateTime from = LocalDateTime.of(2022, 5, 15, 12, 0);
        LocalDateTime to = LocalDateTime.of(2022, 4, 25, 15, 30);
        var requestBuilder = get(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("startDate", from.toString())
                .queryParam("endDate", to.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    private void setupMockUser() {
        setupMockUser(USER);
    }

    private void setupMockUser(User user) {
        given(currentUserService.getCurrentUser())
                .willReturn(Optional.of(user));
        given(currentUserService.getCurrentUserId())
                .willReturn(Optional.of(user.getId()));
    }
}
