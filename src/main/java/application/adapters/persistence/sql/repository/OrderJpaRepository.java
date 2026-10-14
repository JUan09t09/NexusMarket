package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.OrderEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {
    Optional<OrderEntity> findByIdAndBuyerId(String id, String buyerId);
}
