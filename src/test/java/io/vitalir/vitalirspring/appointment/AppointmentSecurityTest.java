package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.common.HttpEndpoints;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentService;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test-security")
public class AppointmentSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

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
}
