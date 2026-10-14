package application.domain.ports.in;

import application.domain.models.Order;

public interface DispatchOrderUseCase {
    Order execute(String orderId, String operatorId);
}
