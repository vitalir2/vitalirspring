package io.vitalir.vitalirspring.features.user.presentation.login;

import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.security.JwtConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController implements LoginApi {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var email = loginRequest.email();
        var password = loginRequest.password();
        var jwt = userService.login(email, password);
        if (jwt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().header(
                HttpHeaders.AUTHORIZATION,
                JwtConstants.BEARER_PREFIX + jwt.get()
        ).build();
    }
}
