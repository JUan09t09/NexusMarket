package application.domain.ports.out;

import application.domain.models.Product;
import java.util.Optional;
import java.util.List;

public interface ProductRepositoryPort {
    public Product save(Product product);
    Optional<Product> findById(String productId);
    List<Product> findBySeller(String sellerId);
}
