package application.adapters.persistence.sql.repository;

import application.adapters.persistence.sql.entity.ProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {
    List<ProductEntity> findBySellerId(String sellerId);
}
