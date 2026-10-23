package application.adapters.rest.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustInventoryRequestDTO {
    @NotBlank
    private String productId;
    @NotBlank
    private String storeId;
    private int newAmount;
    private String reason;
}
