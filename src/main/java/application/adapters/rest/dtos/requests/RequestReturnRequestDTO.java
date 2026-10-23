package application.adapters.rest.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestReturnRequestDTO {
    @NotBlank
    private String orderId;
    private String reason;
    private LocalDate returnDate;
}
