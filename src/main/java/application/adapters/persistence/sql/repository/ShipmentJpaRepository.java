package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.ShipmentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentJpaRepository extends JpaRepository<ShipmentEntity, Long> {
    Optional<ShipmentEntity> findByOrderId(String orderId);
    Optional<ShipmentEntity> findByIdentifier(long identifier);
    boolean existsByOrderId(String orderId);
}
