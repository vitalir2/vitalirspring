package io.vitalir.vitalirspring.features.user.presentation;

import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.security.JwtConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController implements LoginApi {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public LoginController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
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
        userDetailsService.loadUserByUsername(email);
        return ResponseEntity.ok().header(
                HttpHeaders.AUTHORIZATION,
                JwtConstants.BEARER_PREFIX + jwt.get()
        ).build();
    }
}
