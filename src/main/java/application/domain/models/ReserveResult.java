package application.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReserveResult {
    private String productId;
    private String storeId;
    private int quantityReserved;
    private int available;
    private boolean success;

}
