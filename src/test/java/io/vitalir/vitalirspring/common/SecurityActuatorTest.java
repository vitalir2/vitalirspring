package io.vitalir.vitalirspring.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test-security")
public class SecurityActuatorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetHealthPageWithoutLogin_forbidIt() throws Exception {
        var requestBuilder = get(HttpEndpoints.SPRING_ACTUATOR_HEALTH)
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void whenGetHealthWithUser_forbidIt() throws Exception {
        var requestBuilder = get(HttpEndpoints.SPRING_ACTUATOR_HEALTH)
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenGetHealthWitAdmin_forbidIt() throws Exception {
        var requestBuilder = get(HttpEndpoints.SPRING_ACTUATOR_HEALTH)
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
}
