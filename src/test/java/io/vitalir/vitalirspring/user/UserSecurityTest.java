package io.vitalir.vitalirspring.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitalir.vitalirspring.common.constants.HttpEndpoints;
import io.vitalir.vitalirspring.features.user.domain.login.LoginService;
import io.vitalir.vitalirspring.features.user.domain.registration.RegistrationService;
import io.vitalir.vitalirspring.features.user.domain.model.LoginResult;
import io.vitalir.vitalirspring.features.user.presentation.login.LoginRequest;
import io.vitalir.vitalirspring.features.user.presentation.registration.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test-security")
public class UserSecurityTest extends UserFeatureTest {

    @MockBean
    private LoginService loginService;

    @MockBean
    private RegistrationService registrationService;

    @Autowired
    private MockMvc mockMvc;

    private final LoginRequest loginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);

    private final RegistrationRequest registrationRequest = new RegistrationRequest(VALID_EMAIL, VALID_PASSWORD);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenLoginWithoutAuth_permitIt() throws Exception {
        given(loginService.login(VALID_EMAIL, VALID_PASSWORD)).willReturn(Optional.of(new LoginResult(123L, "Bearer ...")));
        var requestBuilder = post(HttpEndpoints.AUTH_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void whenRegisterWithoutAuth_permitIt() throws Exception {
        given(registrationService.register(VALID_EMAIL, VALID_PASSWORD)).willReturn(123L);
        var requestBuilder = post(HttpEndpoints.REGISTER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }
}
