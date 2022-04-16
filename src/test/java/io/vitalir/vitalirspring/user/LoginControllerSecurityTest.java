package io.vitalir.vitalirspring.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.features.user.presentation.LoginRequest;
import io.vitalir.vitalirspring.security.JwtConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test-security")
public class LoginControllerSecurityTest {

    private static final String VALID_EMAIL = "g@gmail.com";
    private static final String VALID_PASSWORD = "1324";
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void initBeforeEach() {
        user = new User(
                VALID_EMAIL,
                VALID_PASSWORD,
                Role.USER
        );
    }

    @Test
    public void whenLoginWithCorrectLoginAndPassword_loginAndReturnJwt() throws Exception {
        var objectMapper = new ObjectMapper();
        var loginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        given(userService.getUserByEmail(VALID_EMAIL)).willReturn(Optional.of(user));
        var requestBuilder = post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().string(JwtConstants.AUTHORIZATION, startsWith(JwtConstants.BEARER_PREFIX)));
    }
}
