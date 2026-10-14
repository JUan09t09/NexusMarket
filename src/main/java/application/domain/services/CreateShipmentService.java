package application.domain.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.OrderNotFoundException;
import application.domain.exceptions.OrderNotPhysicalException;
import application.domain.exceptions.ShipmentAlreadyExistsException;
import application.domain.models.Order;
import application.domain.models.Product;
import application.domain.models.Shipment;
import application.domain.models.Variant;
import application.domain.ports.in.CreateShipmentUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.ShipmentRepositoryPort;
import application.domain.valueObjects.ProductType;
import application.domain.valueObjects.ShippingStatus;

@Service
@RequiredArgsConstructor

public class CreateShipmentService implements CreateShipmentUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Shipment execute(String orderId, long identifier, LocalDate shippingDate) {
        Order orderReference = new Order();
        orderReference.setId(orderId);

        Order order = orderRepositoryPort.findById(orderReference)
            .orElseThrow(() -> new OrderNotFoundException("No existe el pedido con ID " + orderId + "."));

        boolean hasPhysical = false;
        for (Variant item : order.getItems()) {
            Product productReference = new Product();
            productReference.setId(item.getProductId());
            Product product = productRepositoryPort.findById(productReference).orElse(null);
            if (product != null && product.getProductType() == ProductType.Physical) {
                hasPhysical = true;
                break;
            }
        }

        if (!hasPhysical) {
            throw new OrderNotPhysicalException("El pedido no contiene productos fisicos.");
        }

        Shipment shipmentReference = new Shipment();
        shipmentReference.setOrderId(orderId);
        if (shipmentRepositoryPort.existsByOrder(shipmentReference)) {
            throw new ShipmentAlreadyExistsException("El pedido ya cuenta con un envio asociado.");
        }

        Shipment shipment = new Shipment();
        shipment.setIdentifier(identifier);
        shipment.setOrderId(orderId);
        shipment.setBuyerId(order.getBuyerId());
        shipment.setShippingStatus(ShippingStatus.PENDING);
        shipment.setShippingDate(shippingDate != null ? shippingDate : LocalDate.now());

        return shipmentRepositoryPort.save(shipment);
    }
}
