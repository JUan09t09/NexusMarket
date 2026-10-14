package application.domain.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Variant {
    private String id;
    private String productId;
    private String color;
    private String size;
    private String model;
    private int quantity;
    private BigDecimal price;
}
