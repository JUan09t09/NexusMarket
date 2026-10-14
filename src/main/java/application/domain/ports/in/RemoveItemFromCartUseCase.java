package application.domain.ports.in;

import application.domain.models.ShoppingCart;

public interface RemoveItemFromCartUseCase {
    ShoppingCart execute(String buyerId, String variantId);
}
