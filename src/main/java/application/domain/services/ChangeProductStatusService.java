package application.domain.services;

import java.util.Objects;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidProductStatusTransitionException;
import application.domain.exceptions.ProductNotFoundException;
import application.domain.exceptions.UnauthorizedSellerException;
import application.domain.models.Product;
import application.domain.models.User;
import application.domain.ports.in.ChangeProductStatusUseCase;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.valueObjects.ProductStatus;

@Service
@RequiredArgsConstructor

public class ChangeProductStatusService implements ChangeProductStatusUseCase {
    private final ProductRepositoryPort productRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;

    @Override
    public Product execute(String productId, String sellerId, ProductStatus newStatus) {
        Product productReference = new Product();
        productReference.setId(productId);

        Product product = productRepositoryPort.findById(productReference)
            .orElseThrow(() -> new ProductNotFoundException("No existe el producto con ID " + productId + "."));

        User sellerReference = new User();
        sellerReference.setIdentifier(sellerId);

        if (!sellerRepositoryPort.isActive(sellerReference) || !Objects.equals(product.getSellerId(), sellerId)) {
            throw new UnauthorizedSellerException("El vendedor no es propietario del producto indicado.");
        }

        if (!isValidTransition(product.getStatus(), newStatus)) {
            throw new InvalidProductStatusTransitionException(
                "La transicion de estado solicitada no es valida."
            );
        }

        product.setStatus(newStatus);
        return productRepositoryPort.save(product);
    }

    private boolean isValidTransition(ProductStatus current, ProductStatus next) {
        if (current == null || next == null) {
            return false;
        }
        switch (current) {
            case published:
                return next == ProductStatus.suspended || next == ProductStatus.discontinued;
            case suspended:
                return next == ProductStatus.published || next == ProductStatus.discontinued;
            default:
                return false;
        }
    }
}
