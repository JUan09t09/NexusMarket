package application.domain.ports.in;

import application.domain.models.Order;

public interface ConfirmCartUseCase {
    Order execute(String buyerId, String cartId);
}
