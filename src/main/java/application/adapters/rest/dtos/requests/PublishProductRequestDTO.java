package application.adapters.rest.dtos.requests;

import application.domain.models.Variant;
import application.domain.valueObjects.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishProductRequestDTO {
    private String productId;
    @NotNull
    private ProductType productType;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private BigDecimal basePrice;
    @NotBlank
    private String storeId;
    private List<Variant> variants;
}
