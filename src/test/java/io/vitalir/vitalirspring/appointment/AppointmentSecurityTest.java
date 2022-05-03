package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.common.constants.HttpEndpoints;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentService;
import io.vitalir.vitalirspring.features.user.domain.users.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test-security")
public class AppointmentSecurityTest extends AppointmentFeatureTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private CurrentUserService currentUserService;

    private static final long USER_ID = 1;

    @Test
    void whenGetAppointmentsForUserIdWithoutAuth_returnForbidden() throws Exception {
        given(appointmentService.getAppointmentsByUserId(USER_ID))
                .willReturn(List.of());
        var requestBuilder = get(HttpEndpoints.APPOINTMENT_ENDPOINT + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void whenGetAppointmentsForUserIdWithAnyAuth_returnOk() throws Exception {
        given(appointmentService.getAppointmentsByUserId(USER_ID))
                .willReturn(List.of());
        var requestBuilder = get(HttpEndpoints.APPOINTMENT_ENDPOINT + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void whenAddAppointmentWithoutAuth_returnForbidden() throws Exception {
        given(appointmentService.addAppointment(USER, ADD_APPOINTMENT_REQUEST))
                .willReturn(USER_ID);
        var requestBuilder = post(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(APPOINTMENT))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void whenAddAppointmentForCurrentUser_returnOk() throws Exception {
        given(appointmentService.getAppointmentsByUserId(USER_ID))
                .willReturn(List.of());
        given(currentUserService.getCurrentUser())
                .willReturn(Optional.of(USER));
        var requestBuilder = post(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(APPOINTMENT))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }

    @Test
    void whenChangeAppointmentWithoutAuth_returnForbidden() throws Exception {
        given(appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .willReturn(APPOINTMENT_ID);
        var requestBuilder = put(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(APPOINTMENT))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void whenChangeAppointmentForCurrentUser_returnOk() throws Exception {
        given(appointmentService.changeAppointment(USER_ID, CHANGE_APPOINTMENT_REQUEST))
                .willReturn(APPOINTMENT_ID);
        given(currentUserService.getCurrentUserId())
                .willReturn(Optional.of(USER_ID));
        var requestBuilder = put(HttpEndpoints.APPOINTMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(APPOINTMENT))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteAppointmentWithoutAuth_returnForbidden() throws Exception {
        given(appointmentService.removeAppointmentByIds(USER_ID, APPOINTMENT_ID))
                .willReturn(Optional.of(APPOINTMENT));
        var requestBuilder = delete(HttpEndpoints.APPOINTMENT_ENDPOINT + APPOINTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void whenDeleteAppointmentForCurrentUser_returnOk() throws Exception {
        given(appointmentService.removeAppointmentByIds(USER.getId(), APPOINTMENT_ID))
                .willReturn(Optional.of(APPOINTMENT));
        given(currentUserService.getCurrentUser())
                .willReturn(Optional.of(USER));
        var requestBuilder = delete(HttpEndpoints.APPOINTMENT_ENDPOINT + APPOINTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
}
