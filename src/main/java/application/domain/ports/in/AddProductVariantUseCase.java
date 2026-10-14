package application.domain.ports.in;

import application.domain.models.Product;

public interface AddProductVariantUseCase {
    Product execute(String productId, String sellerId, String color, String size, String model);
}
