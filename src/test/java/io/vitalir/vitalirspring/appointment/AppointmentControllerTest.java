package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.common.HttpEndpoints;
import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentService;
import io.vitalir.vitalirspring.features.appointment.domain.IllegalUserIdException;
import io.vitalir.vitalirspring.features.appointment.presentation.AppointmentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@ActiveProfiles("test")
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    private static final long USER_ID = 1;

    private static final Appointment APPOINTMENT = new Appointment();

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
                .willThrow(new IllegalUserIdException());
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
                .willThrow(new IllegalUserIdException());
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
}