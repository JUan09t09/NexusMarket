package application.domain.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import application.domain.valueObjects.RefundStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Refund {
    private LocalDate refundDate;
    private BigDecimal amount;
    private RefundStatus status;
}
