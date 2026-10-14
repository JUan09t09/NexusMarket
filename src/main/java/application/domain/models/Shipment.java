package application.domain.models;

import java.time.LocalDate;
import application.domain.valueObjects.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class Shipment {
    private long identifier;
    private String orderId;
    private String buyerId;
    private ShippingStatus shippingStatus;
    private LocalDate shippingDate;
    private LocalDate deliveryDate;

}
