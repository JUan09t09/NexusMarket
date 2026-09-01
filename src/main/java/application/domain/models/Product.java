package application.domain.models;

import java.util.List;
import application.domain.valueObjects.ProductStatus;
import application.domain.valueObjects.ProductType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class Product {
    private ProductType ProductType;
    private List<Variant> variants;
    private ProductStatus status;

}
