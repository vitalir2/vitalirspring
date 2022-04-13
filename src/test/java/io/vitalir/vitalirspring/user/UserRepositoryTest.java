package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.User;
import io.vitalir.vitalirspring.features.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles({"test"})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void whenGetUserByEmailAndItExists_returnIt() {
        var email = "User@gmail.com";
        var user = new User(email, "1234");
        testEntityManager.persist(user);

        var result = userRepository.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }
}
