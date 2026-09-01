package application.domain.models;

import java.time.LocalDate;
import application.domain.valueObjects.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class Shipment {
    private ShippingStatus shippingStatus;
    private LocalDate shipmentDate;
    private LocalDate deliveryDate;
    private long identifier;

}
