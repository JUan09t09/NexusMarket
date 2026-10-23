package application.adapters.rest.dtos.requests;

import application.domain.valueObjects.ShippingStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateShipmentStatusRequestDTO {
    @NotNull
    private ShippingStatus newStatus;
    private LocalDate deliveryDate;
}
