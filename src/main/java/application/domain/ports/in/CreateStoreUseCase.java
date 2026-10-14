package application.domain.ports.in;

import application.domain.models.Store;
import application.domain.valueObjects.WarehouseType;

public interface CreateStoreUseCase {
    Store execute(
        String executorAdminId,
        String sellerId,
        String warehouseName,
        WarehouseType warehouseType,
        String warehouseAddress
    );
}
