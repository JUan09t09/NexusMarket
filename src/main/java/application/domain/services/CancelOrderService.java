package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.OrderNotCancellableException;
import application.domain.exceptions.OrderNotFoundException;
import application.domain.exceptions.UnauthorizedBuyerException;
import application.domain.models.Inventario;
import application.domain.models.Order;
import application.domain.models.Variant;
import application.domain.ports.in.CancelOrderUseCase;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueObjects.OrderStatus;

@Service
@RequiredArgsConstructor

public class CancelOrderService implements CancelOrderUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public Order execute(String orderId, String buyerId, String reason) {
        Order orderReference = new Order();
        orderReference.setId(orderId);

        Order order = orderRepositoryPort.findById(orderReference)
            .orElseThrow(() -> new OrderNotFoundException("No existe el pedido con ID " + orderId + "."));

        if (order.getBuyerId() == null || !order.getBuyerId().equals(buyerId)) {
            throw new UnauthorizedBuyerException("El pedido no pertenece al comprador indicado.");
        }

        if (order.getOrderStatus() == OrderStatus.Dispatched
                || order.getOrderStatus() == OrderStatus.Delivered
                || order.getOrderStatus() == OrderStatus.Finalized) {
            throw new OrderNotCancellableException("El pedido ya fue despachado o finalizado y no puede cancelarse.");
        }

        for (Variant item : order.getItems()) {
            Inventario inventoryReference = new Inventario();
            inventoryReference.setProductId(item.getProductId());
            for (Inventario inventory : inventoryRepositoryPort.findByProduct(inventoryReference)) {
                int reserved = inventory.getReserved() == null ? 0 : inventory.getReserved();
                int release = Math.min(reserved, item.getQuantity());
                inventory.setReserved(reserved - release);
                inventoryRepositoryPort.save(inventory);
            }
        }

        order.setCancelled(true);
        return orderRepositoryPort.save(order);
    }
}
