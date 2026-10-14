package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByIdentifier(String identifier);
    boolean existsByEmail(String email);
    boolean existsByIdentifier(String identifier);
}
