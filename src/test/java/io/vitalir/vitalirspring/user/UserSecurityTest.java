package io.vitalir.vitalirspring.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitalir.vitalirspring.features.user.domain.UserService;
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
public class UserSecurityTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private static final String EMAIL = "myemail@amazon.com";
    private static final String PASSWORD = "1233333";

    private final LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);

    private final RegistrationRequest registrationRequest = new RegistrationRequest(EMAIL, PASSWORD);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenLoginWithoutAuth_permitIt() throws Exception {
        given(userService.login(EMAIL, PASSWORD)).willReturn(Optional.of("Bearer ..."));
        var requestBuilder = post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void whenRegisterWithoutAuth_permitIt() throws Exception {
        given(userService.register(EMAIL, PASSWORD)).willReturn(true);
        var requestBuilder = post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
}
