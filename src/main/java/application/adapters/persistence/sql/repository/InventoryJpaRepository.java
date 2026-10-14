package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.InventarioEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventarioEntity, String> {
    Optional<InventarioEntity> findByProductIdAndStoreId(String productId, String storeId);
    List<InventarioEntity> findByProductId(String productId);
}
