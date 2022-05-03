package io.vitalir.vitalirspring.features.user.data;

import io.vitalir.vitalirspring.features.user.domain.users.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends CrudRepository<User, Long>, UserRepository {
}
