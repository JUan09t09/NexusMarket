package application.domain.ports.in;

import application.domain.models.ReserveResult;

public interface ReserveInventoryUseCase {
    ReserveResult execute(String productId, String storeId, int quantityReserve);
}
