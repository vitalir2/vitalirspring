package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.features.user.presentation.UserControlle;
import jdk.security.jarsigner.JarSigner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserControlle.class)
@ActiveProfiles({"test"})
public class UserControlleTest extends UserFeatureTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetUserByEmailWhichExists_returnIt() throws Exception {
        given(userService.getUserByEmail(VALID_EMAIL)).willReturn(Optional.of(VALID_USER));
        RequestBuilder requestBuilder = get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("email", VALID_EMAIL);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(VALID_EMAIL)))
                .andExpect(jsonPath("$.password", equalTo(HASHED_PASSWORD)));
    }

    @Test
    public void whenGetUserByEmailWhichDoesNotExist_returnNull() throws Exception {
        given(userService.getUserByEmail(VALID_EMAIL)).willReturn(Optional.empty());
        RequestBuilder requestBuilder = get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("email", VALID_EMAIL);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetUserByIdWhichExists_returnIt() throws Exception {
        given(userService.getUserById(VALID_UID)).willReturn(Optional.of(VALID_USER));
        var requestBuilder = get("/api/v1/users/" + VALID_UID)
                .queryParam("id", String.valueOf(VALID_UID));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(VALID_EMAIL)))
                .andExpect(jsonPath("$.password", equalTo(HASHED_PASSWORD)));
    }

    @Test
    public void whenGetUserByIdWhichDoesNotExist_returnBadRequest() throws Exception {
        given(userService.getUserById(VALID_UID)).willReturn(Optional.empty());
        var requestBuilder = get("/api/v1/users/" + VALID_UID)
                .queryParam("id", String.valueOf(VALID_UID));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}
