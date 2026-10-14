package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.ShoppingCartEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartJpaRepository extends JpaRepository<ShoppingCartEntity, String> {
    Optional<ShoppingCartEntity> findByBuyerId(String buyerId);
}
