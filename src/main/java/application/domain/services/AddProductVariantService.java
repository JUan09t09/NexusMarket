package application.domain.services;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.DuplicatedVariantException;
import application.domain.exceptions.InvalidVariantException;
import application.domain.exceptions.ProductNotFoundException;
import application.domain.exceptions.UnauthorizedSellerException;
import application.domain.models.Product;
import application.domain.models.User;
import application.domain.models.Variant;
import application.domain.ports.in.AddProductVariantUseCase;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.SellerRepositoryPort;

@Service
@RequiredArgsConstructor

public class AddProductVariantService implements AddProductVariantUseCase {
    private final ProductRepositoryPort productRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;

    @Override
    public Product execute(String productId, String sellerId, String color, String size, String model) {
        Product productReference = new Product();
        productReference.setId(productId);

        Product product = productRepositoryPort.findById(productReference)
            .orElseThrow(() -> new ProductNotFoundException("No existe el producto con ID " + productId + "."));

        User sellerReference = new User();
        sellerReference.setIdentifier(sellerId);

        if (!sellerRepositoryPort.isActive(sellerReference) || !Objects.equals(product.getSellerId(), sellerId)) {
            throw new UnauthorizedSellerException("El vendedor no es propietario del producto indicado.");
        }

        if (isBlank(color) && isBlank(size) && isBlank(model)) {
            throw new InvalidVariantException("La variante debe tener al menos un atributo.");
        }

        boolean duplicated = product.getVariants().stream().anyMatch(variant ->
            Objects.equals(variant.getColor(), color)
                && Objects.equals(variant.getSize(), size)
                && Objects.equals(variant.getModel(), model));

        if (duplicated) {
            throw new DuplicatedVariantException("Ya existe una variante con la misma combinacion de atributos.");
        }

        Variant variant = new Variant();
        variant.setId(UUID.randomUUID().toString());
        variant.setProductId(productId);
        variant.setColor(color);
        variant.setSize(size);
        variant.setModel(model);
        product.getVariants().add(variant);

        return productRepositoryPort.save(product);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
