package application.domain.ports.out;

import application.domain.models.User;
import application.domain.valueObjects.UserRole;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByIdentification(User user);

    Optional<User> findByEmail(User user);

    boolean existsByIdentification(User user);

    boolean existsByEmail(User user);

    boolean hasRole(User user, UserRole role);

    boolean isActive(User user);
}
