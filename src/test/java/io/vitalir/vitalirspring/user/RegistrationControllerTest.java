package io.vitalir.vitalirspring.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitalir.vitalirspring.features.user.domain.RegistrationService;
import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.features.user.presentation.registration.RegistrationController;
import io.vitalir.vitalirspring.features.user.presentation.registration.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@ActiveProfiles("test")
public class RegistrationControllerTest extends UserFeatureTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenRegisterWithValidCredentials_addUserToSystem() throws Exception {
        var registrationRequest = new RegistrationRequest(VALID_EMAIL, VALID_PASSWORD);
        given(registrationService.register(VALID_EMAIL, VALID_PASSWORD))
                .willReturn(123L);
        var requestBuilder = post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/v1/users/" + 123L));
        verify(registrationService).register(VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    public void whenRegisterWithInvalidCredentials_returnBadRequest() throws Exception {
        var registrationRequest = new RegistrationRequest(VALID_EMAIL, VALID_PASSWORD);
        given(registrationService.register(VALID_EMAIL, VALID_PASSWORD))
                .willReturn(-1L);
        var requestBuilder = post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
        verify(registrationService).register(VALID_EMAIL, VALID_PASSWORD);
    }
}
