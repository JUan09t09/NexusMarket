package application.domain.ports.in;

import application.domain.models.Shipment;
import application.domain.valueObjects.ShippingStatus;
import java.time.LocalDate;

public interface UpdateShipmentStatusUseCase {
    Shipment execute(long shipmentId, String operatorId, ShippingStatus newStatus, LocalDate deliveryDate);
}
