package application.domain.ports.in;

import application.domain.models.ShoppingCart;

public interface AddItemToCartUseCase {
    ShoppingCart execute(String buyerId, String productId, String variantId, int quantity);
}
