package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidOrderStatusException;
import application.domain.exceptions.OrderNotFoundException;
import application.domain.exceptions.PendingReturnException;
import application.domain.models.Order;
import application.domain.models.Return;
import application.domain.ports.in.FinalizeOrderUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.valueObjects.OrderStatus;

@Service
@RequiredArgsConstructor

public class FinalizeOrderService implements FinalizeOrderUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final ReturnRepositoryPort returnRepositoryPort;

    @Override
    public Order execute(String orderId) {
        Order orderReference = new Order();
        orderReference.setId(orderId);

        Order order = orderRepositoryPort.findById(orderReference)
            .orElseThrow(() -> new OrderNotFoundException("No existe el pedido con ID " + orderId + "."));

        if (order.getOrderStatus() != OrderStatus.Delivered) {
            throw new InvalidOrderStatusException("El pedido no se encuentra en estado entregado.");
        }

        Return returnReference = new Return();
        returnReference.setOrderId(orderId);
        if (returnRepositoryPort.existsByOrder(returnReference)) {
            throw new PendingReturnException("El pedido tiene una devolucion en curso y no puede finalizarse.");
        }

        order.setOrderStatus(OrderStatus.Finalized);
        return orderRepositoryPort.save(order);
    }
}
