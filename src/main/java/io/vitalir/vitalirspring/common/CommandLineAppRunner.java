package io.vitalir.vitalirspring.common;

import io.vitalir.vitalirspring.features.user.Role;
import io.vitalir.vitalirspring.features.user.User;
import io.vitalir.vitalirspring.features.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppRunner implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String DEFAULT_PASSWORD = "password";

    private final UserRepository userRepository;

    public CommandLineAppRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        var foundAdmin = userRepository.getUserByEmail(ADMIN_EMAIL);
        if (foundAdmin.isPresent()) {
            return;
        }
        String password;
        try {
            password = args[0];
        } catch (ArrayIndexOutOfBoundsException exception) {
            password = DEFAULT_PASSWORD;
        }
        addAdmin(password);
    }

    private void addAdmin(String password) {
        User admin = new User(
                ADMIN_EMAIL,
                password,
                Role.ADMIN
        );
        userRepository.save(admin);
    }
}
