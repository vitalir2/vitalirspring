package io.vitalir.vitalirspring.features.user.presentation;


import io.vitalir.vitalirspring.features.user.domain.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserControlle implements UserApi {

    private final UserService userService;

    public UserControlle(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        var optionalUser = userService.getUserByEmail(email);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }
}
