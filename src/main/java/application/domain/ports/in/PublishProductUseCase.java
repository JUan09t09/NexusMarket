package application.domain.ports.in;

import application.domain.models.Product;
import application.domain.models.Variant;
import application.domain.valueObjects.ProductType;
import java.math.BigDecimal;
import java.util.List;

public interface PublishProductUseCase {
    Product execute(
        String productId,
        String sellerId,
        ProductType productType,
        String name,
        String description,
        BigDecimal basePrice,
        String storeId,
        List<Variant> variants
    );
}
