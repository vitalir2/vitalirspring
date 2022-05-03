package io.vitalir.vitalirspring.features.user.presentation.login;

import io.vitalir.vitalirspring.common.constants.HttpEndpoints;
import io.vitalir.vitalirspring.features.user.domain.login.LoginService;
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
@RequestMapping(HttpEndpoints.AUTH_ENDPOINT)
public class LoginController implements LoginApi {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> login(@RequestBody LoginRequest loginRequest) {
        var email = loginRequest.email();
        var password = loginRequest.password();
        var optionalLoginResult = loginService.login(email, password);
        if (optionalLoginResult.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        var jwt = optionalLoginResult.get().jwt();
        var userId = optionalLoginResult.get().userId();
        return ResponseEntity.ok().header(
                HttpHeaders.AUTHORIZATION,
                JwtConstants.BEARER_PREFIX + jwt
        ).body(userId);
    }
}
