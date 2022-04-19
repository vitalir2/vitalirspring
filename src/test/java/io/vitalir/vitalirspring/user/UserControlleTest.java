package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.features.user.presentation.UserControlle;
import io.vitalir.vitalirspring.features.user.domain.UserService;
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
public class UserControlleTest {

    private static final String EMAIL = "g@gmail.com";
    private static final String PASSWORD = "kek";

    private final User validUser = new User(EMAIL, PASSWORD, Role.ADMIN);

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetUserByEmailWhichExists_returnIt() throws Exception {
        given(userService.getUserByEmail(EMAIL)).willReturn(Optional.of(validUser));
        RequestBuilder requestBuilder = get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("email", EMAIL);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(validUser.getEmail())))
                .andExpect(jsonPath("$.password", equalTo(validUser.getPassword())));
    }

    @Test
    public void whenGetUserByEmailWhichDoesNotExist_returnNull() throws Exception {
        given(userService.getUserByEmail(EMAIL)).willReturn(Optional.empty());
        RequestBuilder requestBuilder = get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("email", EMAIL);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}
