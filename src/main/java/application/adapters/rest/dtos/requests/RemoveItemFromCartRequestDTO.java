package application.adapters.rest.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveItemFromCartRequestDTO {
    @NotBlank
    private String variantId;
}
