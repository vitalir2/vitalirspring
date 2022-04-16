package io.vitalir.vitalirspring.features.user.presentation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController implements LoginApi {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> error(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @Override
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var jwt = "Bearer alksdjflaksdjfklsadjf.jasdlfjasdfljasdjfasd";
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }
}
