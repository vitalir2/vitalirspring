package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles({"test"})
public class UserRepositoryTest extends UserFeatureTest {

    private User validUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    public void clear() {
        validUser = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    public void whenGetUserByEmailAndItExists_returnIt() {
        testEntityManager.persist(validUser);

        var result = userRepository.getUserByEmail(VALID_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(validUser, result.get());
    }

    @Test
    public void whenGetUserByIdAndItExists_returnIt() {
        testEntityManager.persist(validUser);

        var result = userRepository.getById(validUser.getId());

        assertTrue(result.isPresent());
        assertEquals(validUser, result.get());
    }

    @Test
    public void whenSaveUser_saveIt() {
        userRepository.save(validUser);

        assertEquals(validUser, testEntityManager.find(User.class, validUser.getId()));
    }

    @Test
    public void whenUserShouldExist_returnTrue() {
        testEntityManager.persist(validUser);

        assertTrue(userRepository.existsById(validUser.getId()));
    }

    @Test
    void whenUserDoesNotExist_returnFalse() {
        assertFalse(userRepository.existsById(validUser.getId()));
    }
}
