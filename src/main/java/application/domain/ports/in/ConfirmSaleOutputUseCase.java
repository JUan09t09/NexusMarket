package application.domain.ports.in;

import application.domain.models.Inventario;

public interface ConfirmSaleOutputUseCase {
    Inventario execute(String productId, String storeId, int quantity, String orderId);
}
