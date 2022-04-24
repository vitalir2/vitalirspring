package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.common.HttpEndpoints;
import io.vitalir.vitalirspring.features.appointment.domain.*;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidUserIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidAppointmentIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidDoctorIdException;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.presentation.AppointmentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void whenRemoveAppointmentByIdsWhichExists_returnRemovedAppointment() throws Exception {
        given(appointmentService.removeAppointmentByIds(anyLong(), anyLong()))
                .willReturn(Optional.of(APPOINTMENT));
        var requestBuilder = delete(HttpEndpoints.APPOINTMENT_ENDPOINT +
                USER_ID + "/" + APPOINTMENT.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo((int) APPOINTMENT.getId())));
        verify(appointmentService).removeAppointmentByIds(anyLong(), anyLong());
    }

    @Test
    void whenRemoveAppointmentByUserIdWhichDoesNotExist_returnBadRequest() throws Exception {
        given(appointmentService.removeAppointmentByIds(anyLong(), anyLong()))
                .willThrow(new InvalidUserIdException());
        var requestBuilder = delete(HttpEndpoints.APPOINTMENT_ENDPOINT +
                USER_ID + "/" + APPOINTMENT.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRemoveAppointmentByAppointmentIdWhichDoesNotExist_returnBadRequest() throws Exception {
        given(appointmentService.removeAppointmentByIds(anyLong(), anyLong()))
                .willReturn(Optional.empty());
        var requestBuilder = delete(HttpEndpoints.APPOINTMENT_ENDPOINT +
                USER_ID + "/" + APPOINTMENT.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddAppointmentByIdsWhichExist_returnAddedAppointmentId() throws Exception {
        var addAppointmentRequest = new AddAppointmentRequest(
                1,
                2,
                LocalDate.now(),
                1000 * 60 * 15,
                "A description"
        );
        given(appointmentService.addAppointment(any()))
                .willReturn(APPOINTMENT_ID);
        var requestBuilder = post(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(addAppointmentRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", equalTo((int) APPOINTMENT_ID)));
        verify(appointmentService).addAppointment(any());
    }

    @Test
    void whenAddAppointmentByUserIdWhichDoesNotExist_returnBadRequest() throws Exception {
        var addAppointmentRequest = new AddAppointmentRequest(
                1,
                2,
                LocalDate.now(),
                1000 * 60 * 15,
                "A description"
        );
        given(appointmentService.addAppointment(any()))
                .willThrow(new InvalidUserIdException());
        var requestBuilder = post(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(addAppointmentRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddAppointmentByDoctorIdWhichDoesNotExist_returnBadRequest() throws Exception {
        var addAppointmentRequest = new AddAppointmentRequest(
                1,
                2,
                LocalDate.now(),
                1000 * 60 * 15,
                "A description"
        );
        given(appointmentService.addAppointment(any()))
                .willThrow(new InvalidDoctorIdException());
        var requestBuilder = post(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(addAppointmentRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenChangeAppointmentWhichExists_changeIt() throws Exception {
        given(appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .willReturn(APPOINTMENT_ID);
        var requestBuilder = put(HttpEndpoints.APPOINTMENT_ENDPOINT + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(CHANGE_APPOINTMENT_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo((int) APPOINTMENT_ID)));
    }

    @Test
    void whenChangeAppointmentByUserIdWhichDoesNotExist_returnBadRequest() throws Exception {
        given(appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .willThrow(new InvalidUserIdException());
        var requestBuilder = put(HttpEndpoints.APPOINTMENT_ENDPOINT + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(CHANGE_APPOINTMENT_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenChangeAppointmentByAppointmentIdWhichDoesNotExist_returnBadRequest() throws Exception {
        given(appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .willThrow(new InvalidAppointmentIdException());
        var requestBuilder = put(HttpEndpoints.APPOINTMENT_ENDPOINT + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(CHANGE_APPOINTMENT_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}
