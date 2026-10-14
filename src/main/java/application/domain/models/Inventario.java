package application.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Inventario {
    private String id;
    private String productId;
    private String storeId;
    private Integer amount;
    private Integer reserved;
    private boolean available;

}
