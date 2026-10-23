package application.adapters.rest.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessRefundRequestDTO {
    @NotBlank
    private String returnId;
    @NotNull
    private BigDecimal amount;
    private LocalDate refundDate;
}
