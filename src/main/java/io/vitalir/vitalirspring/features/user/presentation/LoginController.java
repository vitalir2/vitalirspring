package io.vitalir.vitalirspring.features.user.presentation;

import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.security.JwtConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        var jwt = userService.login(loginRequest.email(), loginRequest.password());
        if (jwt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().header(
                HttpHeaders.AUTHORIZATION,
                JwtConstants.BEARER_PREFIX + jwt.get()
        ).build();
    }
}
