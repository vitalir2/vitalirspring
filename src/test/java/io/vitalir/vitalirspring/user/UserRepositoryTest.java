package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private User validUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    public void clear() {
        validUser = new User(EMAIL, PASSWORD);
    }

    @Test
    public void whenGetUserByEmailAndItExists_returnIt() {
        testEntityManager.persist(validUser);

        var result = userRepository.getUserByEmail(EMAIL);

        assertTrue(result.isPresent());
        assertEquals(validUser, result.get());
    }

    @Test
    public void whenSaveUser_saveIt() {
        userRepository.save(validUser);

        assertEquals(validUser, testEntityManager.find(User.class, validUser.getId()));
    }
}
