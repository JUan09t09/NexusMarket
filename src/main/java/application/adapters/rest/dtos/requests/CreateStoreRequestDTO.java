package application.adapters.rest.dtos.requests;

import application.domain.valueObjects.WarehouseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStoreRequestDTO {
    @NotBlank
    private String sellerId;
    @NotBlank
    private String warehouseName;
    @NotNull
    private WarehouseType warehouseType;
    @NotBlank
    private String warehouseAddress;
}
