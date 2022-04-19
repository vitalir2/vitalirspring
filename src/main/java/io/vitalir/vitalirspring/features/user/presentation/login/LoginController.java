package io.vitalir.vitalirspring.features.user.presentation.login;

import io.vitalir.vitalirspring.features.user.domain.LoginService;
import io.vitalir.vitalirspring.security.jwt.JwtConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController implements LoginApi {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var email = loginRequest.email();
        var password = loginRequest.password();
        var jwt = loginService.login(email, password);
        if (jwt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().header(
                HttpHeaders.AUTHORIZATION,
                JwtConstants.BEARER_PREFIX + jwt.get()
        ).build();
    }
}
