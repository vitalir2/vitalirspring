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

    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "1234";
    private static final User VALID_USER = new User(EMAIL, PASSWORD);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void whenGetUserByEmailAndItExists_returnIt() {
        testEntityManager.persist(VALID_USER);

        var result = userRepository.getUserByEmail(EMAIL);

        assertTrue(result.isPresent());
        assertEquals(VALID_USER, result.get());
    }

    @Test
    public void whenSaveUser_saveIt() {
        userRepository.save(VALID_USER);

        assertEquals(VALID_USER, testEntityManager.find(User.class, VALID_USER.getId()));
    }
}
