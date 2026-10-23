package application.adapters.rest.dtos.requests;

import application.domain.valueObjects.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeProductStatusRequestDTO {
    @NotNull
    private ProductStatus newStatus;
}
