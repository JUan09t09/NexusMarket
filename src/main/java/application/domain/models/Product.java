package application.domain.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import application.domain.valueObjects.ProductStatus;
import application.domain.valueObjects.ProductType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Product {
    private String id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String sellerId;
    private String storeId;
    private ProductType productType;
    private List<Variant> variants = new ArrayList<>();
    private ProductStatus status;
}
