package application.adapters.rest.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShipmentRequestDTO {
    @NotBlank
    private String orderId;
    private long identifier;
    private LocalDate shippingDate;
}
