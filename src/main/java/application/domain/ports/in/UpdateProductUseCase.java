package application.domain.ports.in;

import application.domain.models.Product;
import java.math.BigDecimal;

public interface UpdateProductUseCase {
    Product execute(String productId, String sellerId, String name, String description, BigDecimal basePrice);
}
