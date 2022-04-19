package io.vitalir.vitalirspring.features.user.presentation.registration;

import io.vitalir.vitalirspring.features.user.domain.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/register")
public class RegistrationController implements RegistrationApi {

    private static final long EXISTS_OR_INVALID_FORMAT = -1;

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> register(@RequestBody RegistrationRequest registrationRequest) {
        var newUserId =
                registrationService.register(registrationRequest.email(), registrationRequest.password());
        if (newUserId != EXISTS_OR_INVALID_FORMAT) {
            return ResponseEntity.created(URI.create("/api/v1/users/" + newUserId)).build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
