package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.RefundEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundJpaRepository extends JpaRepository<RefundEntity, String> {
    Optional<RefundEntity> findByReturnId(String returnId);
}
