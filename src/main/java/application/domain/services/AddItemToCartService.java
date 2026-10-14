package application.domain.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidCartQuantityException;
import application.domain.exceptions.ProductNotAvailableException;
import application.domain.exceptions.VariantNotFoundException;
import application.domain.models.Product;
import application.domain.models.ShoppingCart;
import application.domain.models.Variant;
import application.domain.ports.in.AddItemToCartUseCase;
import application.domain.ports.out.CartRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.valueObjects.ProductStatus;

@Service
@RequiredArgsConstructor

public class AddItemToCartService implements AddItemToCartUseCase {
    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public ShoppingCart execute(String buyerId, String productId, String variantId, int quantity) {
        ShoppingCart buyerReference = new ShoppingCart();
        buyerReference.setBuyerId(buyerId);

        ShoppingCart cart = cartRepositoryPort.findByBuyer(buyerReference).orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setId(UUID.randomUUID().toString());
            newCart.setBuyerId(buyerId);
            return newCart;
        });

        Product productReference = new Product();
        productReference.setId(productId);

        Product product = productRepositoryPort.findById(productReference)
            .orElseThrow(() -> new ProductNotAvailableException("El producto no existe o no esta disponible."));

        if (product.getStatus() != ProductStatus.published) {
            throw new ProductNotAvailableException("El producto no esta publicado.");
        }

        Variant variant = product.getVariants().stream()
            .filter(v -> v.getId() != null && v.getId().equals(variantId))
            .findFirst()
            .orElseThrow(() -> new VariantNotFoundException("La variante no pertenece al producto indicado."));

        if (quantity <= 0) {
            throw new InvalidCartQuantityException("La cantidad debe ser mayor que cero.");
        }

        Variant item = new Variant();
        item.setId(variant.getId());
        item.setProductId(productId);
        item.setColor(variant.getColor());
        item.setSize(variant.getSize());
        item.setModel(variant.getModel());
        item.setPrice(variant.getPrice() != null ? variant.getPrice() : product.getBasePrice());
        item.setQuantity(quantity);
        cart.getItems().add(item);

        return cartRepositoryPort.save(cart);
    }
}
