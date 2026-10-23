package application.adapters.rest.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductVariantRequestDTO {
    private String color;
    private String size;
    private String model;
}
