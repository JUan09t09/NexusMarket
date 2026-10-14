package application.domain.ports.in;

import application.domain.models.Shipment;
import java.time.LocalDate;

public interface CreateShipmentUseCase {
    Shipment execute(String orderId, long identifier, LocalDate shippingDate);
}
