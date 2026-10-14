package application.domain.models;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import application.domain.valueObjects.ReturnStatus;

@Getter
@Setter

public class Return {
    private String id;
    private String orderId;
    private String buyerId;
    private String productId;
    private int quantity;
    private LocalDate returnDate;
    private String reason;
    private ReturnStatus returnStatus;

}
