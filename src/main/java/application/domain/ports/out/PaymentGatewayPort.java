package application.domain.ports.out;

import application.domain.models.Order;
import application.domain.models.Refund;

public interface PaymentGatewayPort {

    boolean confirmPayment(Order order);

    boolean refundPayment(Refund refund);
}
