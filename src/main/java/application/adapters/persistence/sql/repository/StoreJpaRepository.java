package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.StoreEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreJpaRepository extends JpaRepository<StoreEntity, String> {
    Optional<StoreEntity> findById(String id);
    List<StoreEntity> findBySellerId(String sellerId);
    boolean existsById(String id);
}
