package application.domain.ports.in;

import application.domain.models.Order;

public interface CancelOrderUseCase {
    Order execute(String orderId, String buyerId, String reason);
}
