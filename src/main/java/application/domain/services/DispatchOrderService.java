package application.domain.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidOrderStatusException;
import application.domain.exceptions.OrderNotFoundException;
import application.domain.exceptions.OrderNotPhysicalException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.models.Inventario;
import application.domain.models.Order;
import application.domain.models.Product;
import application.domain.models.Shipment;
import application.domain.models.User;
import application.domain.models.Variant;
import application.domain.ports.in.DispatchOrderUseCase;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.ShipmentRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.OrderStatus;
import application.domain.valueObjects.ProductType;
import application.domain.valueObjects.ShippingStatus;
import application.domain.valueObjects.UserRole;

@Service
@RequiredArgsConstructor

public class DispatchOrderService implements DispatchOrderUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Order execute(String orderId, String operatorId) {
        User operator = new User();
        operator.setIdentifier(operatorId);

        if (!userRepositoryPort.hasRole(operator, UserRole.LogisticOperator)) {
            throw new UnauthorizedUserException("El operador no tiene rol de operador logistico.");
        }

        Order orderReference = new Order();
        orderReference.setId(orderId);

        Order order = orderRepositoryPort.findById(orderReference)
            .orElseThrow(() -> new OrderNotFoundException("No existe el pedido con ID " + orderId + "."));

        if (order.getOrderStatus() != OrderStatus.Paid) {
            throw new InvalidOrderStatusException("El pedido no se encuentra en estado pagado.");
        }

        boolean hasPhysical = false;
        for (Variant item : order.getItems()) {
            Product productReference = new Product();
            productReference.setId(item.getProductId());
            Product product = productRepositoryPort.findById(productReference).orElse(null);
            if (product != null && product.getProductType() == ProductType.Physical) {
                hasPhysical = true;
                Inventario inventoryReference = new Inventario();
                inventoryReference.setProductId(product.getId());
                inventoryReference.setStoreId(product.getStoreId());
                Inventario inventory = inventoryRepositoryPort.findByProductAndStore(inventoryReference).orElse(null);
                if (inventory != null) {
                    int amount = inventory.getAmount() == null ? 0 : inventory.getAmount();
                    inventory.setAmount(Math.max(0, amount - item.getQuantity()));
                    inventoryRepositoryPort.save(inventory);
                }
            }
        }

        if (!hasPhysical) {
            throw new OrderNotPhysicalException("Un pedido exclusivamente digital no requiere despacho.");
        }

        Shipment shipment = new Shipment();
        shipment.setIdentifier(System.currentTimeMillis());
        shipment.setOrderId(orderId);
        shipment.setBuyerId(order.getBuyerId());
        shipment.setShippingStatus(ShippingStatus.PENDING);
        shipment.setShippingDate(LocalDate.now());
        Shipment savedShipment = shipmentRepositoryPort.save(shipment);

        order.setShipmentId(String.valueOf(savedShipment.getIdentifier()));
        order.setOrderStatus(OrderStatus.Dispatched);
        return orderRepositoryPort.save(order);
    }
}
