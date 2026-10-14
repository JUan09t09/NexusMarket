package application.domain.ports.in;

import application.domain.models.Order;

public interface CreateOrderUseCase {
    Order execute(String buyerId, String cartId);
}
