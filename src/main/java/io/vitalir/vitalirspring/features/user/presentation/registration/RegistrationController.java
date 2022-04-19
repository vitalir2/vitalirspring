package io.vitalir.vitalirspring.features.user.presentation.registration;

import io.vitalir.vitalirspring.features.user.domain.RegistrationService;
import io.vitalir.vitalirspring.features.user.domain.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")
public class RegistrationController implements RegistrationApi {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        var isSuccessfulRegistration =
                registrationService.register(registrationRequest.email(), registrationRequest.password());
        if (isSuccessfulRegistration) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
