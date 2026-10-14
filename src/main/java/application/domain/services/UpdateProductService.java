package application.domain.services;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidProductDataException;
import application.domain.exceptions.ProductNotEditableException;
import application.domain.exceptions.ProductNotFoundException;
import application.domain.exceptions.UnauthorizedSellerException;
import application.domain.models.Product;
import application.domain.models.User;
import application.domain.ports.in.UpdateProductUseCase;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.valueObjects.ProductStatus;

@Service
@RequiredArgsConstructor

public class UpdateProductService implements UpdateProductUseCase {
    private final ProductRepositoryPort productRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;

    @Override
    public Product execute(String productId, String sellerId, String name, String description, BigDecimal basePrice) {
        Product productReference = new Product();
        productReference.setId(productId);

        Product product = productRepositoryPort.findById(productReference)
            .orElseThrow(() -> new ProductNotFoundException("No existe el producto con ID " + productId + "."));

        User sellerReference = new User();
        sellerReference.setIdentifier(sellerId);

        if (!sellerRepositoryPort.isActive(sellerReference) || !Objects.equals(product.getSellerId(), sellerId)) {
            throw new UnauthorizedSellerException("El vendedor no es propietario del producto indicado.");
        }

        if (name == null || name.isBlank() || basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductDataException("Los datos basicos del producto son invalidos.");
        }

        if (product.getStatus() == ProductStatus.discontinued) {
            throw new ProductNotEditableException("Un producto descontinuado no admite modificaciones.");
        }

        product.setName(name);
        product.setDescription(description);
        product.setBasePrice(basePrice);
        return productRepositoryPort.save(product);
    }
}
