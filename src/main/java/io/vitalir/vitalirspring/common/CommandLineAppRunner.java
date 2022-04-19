package io.vitalir.vitalirspring.common;

import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppRunner implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String DEFAULT_PASSWORD = "password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CommandLineAppRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        var foundAdmin = userRepository.getUserByEmail(ADMIN_EMAIL);
        if (foundAdmin.isPresent()) {
            return;
        }
        String password = parsePassword(args);
        addAdmin(password);
    }

    private String parsePassword(String... args) {
        String password;
        try {
            password = args[0];
        } catch (ArrayIndexOutOfBoundsException exception) {
            password = DEFAULT_PASSWORD;
        }
        return password;
    }

    private void addAdmin(String password) {
        User admin = new User(
                ADMIN_EMAIL,
                passwordEncoder.encode(password),
                Role.ADMIN
        );
        userRepository.save(admin);
    }
}
