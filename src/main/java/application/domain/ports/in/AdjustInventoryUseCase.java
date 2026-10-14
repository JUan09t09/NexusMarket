package application.domain.ports.in;

import application.domain.models.Inventario;

public interface AdjustInventoryUseCase {
    Inventario execute(String operatorId, String productId, String storeId, int newAmount, String reason);
}
