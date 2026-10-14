package application.domain.ports.in;

import application.domain.models.Product;
import application.domain.valueObjects.ProductStatus;

public interface ChangeProductStatusUseCase {
    Product execute(String productId, String sellerId, ProductStatus newStatus);
}
