package application.domain.ports.in;

import application.domain.models.Order;

public interface ConfirmOrderPaymentUseCase {
    Order execute(String orderId, String paymentReference);
}
