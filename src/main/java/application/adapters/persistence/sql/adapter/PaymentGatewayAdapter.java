package application.adapters.persistence.sql.adapter;

import application.domain.models.Order;
import application.domain.models.Refund;
import application.domain.ports.out.PaymentGatewayPort;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayAdapter implements PaymentGatewayPort {

    @Override
    public boolean confirmPayment(Order order) {
        return order != null
            && order.getPaymentReference() != null
            && !order.getPaymentReference().isBlank();
    }

    @Override
    public boolean refundPayment(Refund refund) {
        return refund != null && refund.getAmount() != null;
    }
}
