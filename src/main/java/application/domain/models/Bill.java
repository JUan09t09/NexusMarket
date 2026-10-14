package application.domain.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Bill {
    private String invoiceId;
    private String orderId;
    private LocalDate invoiceDate;
    private BigDecimal totalAmount;

}
