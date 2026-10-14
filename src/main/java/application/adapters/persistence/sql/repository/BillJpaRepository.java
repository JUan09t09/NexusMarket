package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.BillEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillJpaRepository extends JpaRepository<BillEntity, String> {
    Optional<BillEntity> findByOrderId(String orderId);
    boolean existsByOrderId(String orderId);
}
