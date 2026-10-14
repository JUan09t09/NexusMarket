package application.domain.ports.in;

import application.domain.models.Inventario;

public interface RegisterInventoryEntryUseCase {
    Inventario execute(String operatorId, String productId, String storeId, int quantity);
}
