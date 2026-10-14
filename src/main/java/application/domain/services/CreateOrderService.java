package application.domain.services;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.CartNotFoundException;
import application.domain.exceptions.EmptyCartException;
import application.domain.exceptions.InsufficientStockException;
import application.domain.models.Inventario;
import application.domain.models.Order;
import application.domain.models.Product;
import application.domain.models.ShoppingCart;
import application.domain.models.Variant;
import application.domain.ports.in.CreateOrderUseCase;
import application.domain.ports.out.CartRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.valueObjects.OrderStatus;
import application.domain.valueObjects.ProductType;

@Service
@RequiredArgsConstructor

public class CreateOrderService implements CreateOrderUseCase {
    private final CartRepositoryPort cartRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public Order execute(String buyerId, String cartId) {
        ShoppingCart cartReference = new ShoppingCart();
        cartReference.setId(cartId);

        ShoppingCart cart = cartRepositoryPort.findById(cartReference)
            .orElseThrow(() -> new CartNotFoundException("No existe el carrito con ID " + cartId + "."));

        if (cart.getBuyerId() == null || !cart.getBuyerId().equals(buyerId)) {
            throw new CartNotFoundException("El carrito no existe o no pertenece al comprador indicado.");
        }

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("El carrito no contiene al menos una variante.");
        }

        for (Variant item : cart.getItems()) {
            Product productReference = new Product();
            productReference.setId(item.getProductId());
            Product product = productRepositoryPort.findById(productReference).orElse(null);
            if (product != null && product.getProductType() == ProductType.Physical) {
                Inventario inventoryReference = new Inventario();
                inventoryReference.setProductId(product.getId());
                inventoryReference.setStoreId(product.getStoreId());
                Inventario inventory = inventoryRepositoryPort.findByProductAndStore(inventoryReference).orElse(null);
                int amount = inventory == null || inventory.getAmount() == null ? 0 : inventory.getAmount();
                if (amount < item.getQuantity()) {
                    throw new InsufficientStockException(
                        "No hay inventario disponible para el producto " + product.getId() + "."
                    );
                }
            }
        }

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setBuyerId(buyerId);
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setOrderStatus(OrderStatus.PedingPayment);

        return orderRepositoryPort.save(order);
    }
}
