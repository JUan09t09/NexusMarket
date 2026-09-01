package application.domain.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Bill {
    private String invoiceId;
    private String invoiceDate;
    private BigDecimal totalAmount;

}
