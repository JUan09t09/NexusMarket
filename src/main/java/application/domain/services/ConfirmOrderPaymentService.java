package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidOrderStatusException;
import application.domain.exceptions.OrderNotFoundException;
import application.domain.exceptions.PaymentRejectedException;
import application.domain.models.Order;
import application.domain.ports.in.ConfirmOrderPaymentUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.PaymentGatewayPort;
import application.domain.valueObjects.OrderStatus;

@Service
@RequiredArgsConstructor

public class ConfirmOrderPaymentService implements ConfirmOrderPaymentUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final PaymentGatewayPort paymentGatewayPort;

    @Override
    public Order execute(String orderId, String paymentReference) {
        Order orderReference = new Order();
        orderReference.setId(orderId);

        Order order = orderRepositoryPort.findById(orderReference)
            .orElseThrow(() -> new OrderNotFoundException("No existe el pedido con ID " + orderId + "."));

        if (order.getOrderStatus() != OrderStatus.PedingPayment) {
            throw new InvalidOrderStatusException("El pedido no se encuentra en estado pendiente de pago.");
        }

        order.setPaymentReference(paymentReference);
        if (!paymentGatewayPort.confirmPayment(order)) {
            throw new PaymentRejectedException("La transaccion de pago no fue confirmada.");
        }

        order.setOrderStatus(OrderStatus.Paid);
        return orderRepositoryPort.save(order);
    }
}
