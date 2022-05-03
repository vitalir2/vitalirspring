package io.vitalir.vitalirspring.features.user.presentation;


import io.vitalir.vitalirspring.common.constants.HttpEndpoints;
import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(HttpEndpoints.USERS_ENDPOINT)
public class UserControlle implements UserApi {

    private final UserService userService;

    public UserControlle(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        var optionalUser = userService.getUserByEmail(email);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        var optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
