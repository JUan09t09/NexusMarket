package application.domain.ports.out;

import application.domain.models.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Product product);

    List<Product> findBySeller(Product product);

    boolean existsById(Product product);
}
