package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.CartItemNotFoundException;
import application.domain.exceptions.CartNotFoundException;
import application.domain.exceptions.UnauthorizedBuyerException;
import application.domain.models.ShoppingCart;
import application.domain.ports.in.RemoveItemFromCartUseCase;
import application.domain.ports.out.CartRepositoryPort;

@Service
@RequiredArgsConstructor

public class RemoveItemFromCartService implements RemoveItemFromCartUseCase {
    private final CartRepositoryPort cartRepositoryPort;

    @Override
    public ShoppingCart execute(String buyerId, String variantId) {
        ShoppingCart buyerReference = new ShoppingCart();
        buyerReference.setBuyerId(buyerId);

        ShoppingCart cart = cartRepositoryPort.findByBuyer(buyerReference)
            .orElseThrow(() -> new CartNotFoundException("No existe un carrito para el comprador indicado."));

        if (cart.getBuyerId() == null || !cart.getBuyerId().equals(buyerId)) {
            throw new UnauthorizedBuyerException("El carrito no pertenece al comprador indicado.");
        }

        boolean removed = cart.getItems().removeIf(item -> item.getId() != null && item.getId().equals(variantId));
        if (!removed) {
            throw new CartItemNotFoundException("La variante no se encuentra dentro del carrito.");
        }

        return cartRepositoryPort.save(cart);
    }
}
