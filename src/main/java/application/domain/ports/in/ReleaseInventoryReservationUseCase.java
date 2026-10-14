package application.domain.ports.in;

import application.domain.models.Inventario;

public interface ReleaseInventoryReservationUseCase {
    Inventario execute(String productId, String storeId, int quantity);
}
