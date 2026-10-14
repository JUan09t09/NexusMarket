package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.ReturnEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnJpaRepository extends JpaRepository<ReturnEntity, String> {
    Optional<ReturnEntity> findByOrderId(String orderId);
    boolean existsByOrderId(String orderId);
}
