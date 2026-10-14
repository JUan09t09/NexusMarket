package application.domain.services;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.CartNotFoundException;
import application.domain.exceptions.EmptyCartException;
import application.domain.exceptions.InsufficientStockException;
import application.domain.exceptions.UnauthorizedBuyerException;
import application.domain.models.Inventario;
import application.domain.models.Order;
import application.domain.models.ShoppingCart;
import application.domain.models.Variant;
import application.domain.ports.in.ConfirmCartUseCase;
import application.domain.ports.out.CartRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueObjects.OrderStatus;

@Service
@RequiredArgsConstructor

public class ConfirmCartService implements ConfirmCartUseCase {
    private final CartRepositoryPort cartRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public Order execute(String buyerId, String cartId) {
        ShoppingCart cartReference = new ShoppingCart();
        cartReference.setId(cartId);

        ShoppingCart cart = cartRepositoryPort.findById(cartReference)
            .orElseThrow(() -> new CartNotFoundException("No existe el carrito con ID " + cartId + "."));

        if (cart.getBuyerId() == null || !cart.getBuyerId().equals(buyerId)) {
            throw new UnauthorizedBuyerException("El carrito no pertenece al comprador indicado.");
        }

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("El carrito no contiene al menos una variante.");
        }

        for (Variant item : cart.getItems()) {
            Inventario inventoryReference = new Inventario();
            inventoryReference.setProductId(item.getProductId());
            for (Inventario inventory : inventoryRepositoryPort.findByProduct(inventoryReference)) {
                int amount = inventory.getAmount() == null ? 0 : inventory.getAmount();
                if (amount < item.getQuantity()) {
                    throw new InsufficientStockException("No hay existencias suficientes para el producto " + item.getProductId() + ".");
                }
            }
        }

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setBuyerId(buyerId);
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setOrderStatus(OrderStatus.PedingPayment);
        Order savedOrder = orderRepositoryPort.save(order);

        cart.getItems().clear();
        cartRepositoryPort.save(cart);

        return savedOrder;
    }
}
