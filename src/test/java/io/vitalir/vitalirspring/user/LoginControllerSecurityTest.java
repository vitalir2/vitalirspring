package io.vitalir.vitalirspring.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitalir.vitalirspring.features.user.domain.LoginService;
import io.vitalir.vitalirspring.features.user.domain.model.LoginResult;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.presentation.login.LoginRequest;
import io.vitalir.vitalirspring.security.jwt.JwtConstants;
import io.vitalir.vitalirspring.security.jwt.JwtProvider;
import io.vitalir.vitalirspring.security.jwt.JwtVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test-security")
public class LoginControllerSecurityTest extends UserFeatureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtVerifier jwtVerifier;

    @MockBean
    private LoginService loginService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenLoginWithCorrectLoginAndPassword_loginAndReturnJwt() throws Exception {
        var loginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        var expectedJwt = jwtProvider.generateToken(VALID_EMAIL, Role.USER);
        given(loginService.login(VALID_EMAIL, VALID_PASSWORD))
                .willReturn(Optional.of(new LoginResult(123L, expectedJwt)));
        var requestBuilder = post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, startsWith(JwtConstants.BEARER_PREFIX)))
                .andDo(result -> {
                    var authHeader = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
                    var jwt = Objects.requireNonNull(authHeader).substring(JwtConstants.BEARER_PREFIX.length());
                    assertEquals(expectedJwt, jwt);
                    assertTrue(jwtVerifier.validateToken(jwt));
                });
    }

    @Test
    public void whenLoginWithInvalidLoginOrPassword_returnBadRequest() throws Exception {
        var loginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        given(loginService.login(VALID_EMAIL, VALID_PASSWORD))
                .willReturn(Optional.empty());
        var requestBuilder = post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}
