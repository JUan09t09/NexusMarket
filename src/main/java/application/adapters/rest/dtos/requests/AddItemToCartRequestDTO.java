package application.adapters.rest.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemToCartRequestDTO {
    @NotBlank
    private String productId;
    @NotBlank
    private String variantId;
    private int quantity;
}
