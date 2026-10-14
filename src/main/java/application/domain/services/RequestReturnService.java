package application.domain.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.OrderNotReturnableException;
import application.domain.exceptions.OrderNotFoundException;
import application.domain.exceptions.UnauthorizedBuyerException;
import application.domain.models.Order;
import application.domain.models.Return;
import application.domain.models.Variant;
import application.domain.ports.in.RequestReturnUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.valueObjects.OrderStatus;
import application.domain.valueObjects.ReturnStatus;

@Service
@RequiredArgsConstructor

public class RequestReturnService implements RequestReturnUseCase {
    private final ReturnRepositoryPort returnRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    public Return execute(String orderId, String buyerId, String reason, LocalDate returnDate) {
        Order orderReference = new Order();
        orderReference.setId(orderId);

        Order order = orderRepositoryPort.findById(orderReference)
            .orElseThrow(() -> new OrderNotFoundException("No existe el pedido con ID " + orderId + "."));

        if (order.getBuyerId() == null || !order.getBuyerId().equals(buyerId)) {
            throw new UnauthorizedBuyerException("El pedido no pertenece al comprador indicado.");
        }

        if (order.getOrderStatus() != OrderStatus.Delivered && order.getOrderStatus() != OrderStatus.Finalized) {
            throw new OrderNotReturnableException("El pedido no ha sido entregado y no admite devolucion.");
        }

        Return existingReference = new Return();
        existingReference.setOrderId(orderId);
        if (returnRepositoryPort.existsByOrder(existingReference)) {
            throw new OrderNotReturnableException("El pedido ya tiene una devolucion registrada.");
        }

        Return returnRequest = new Return();
        returnRequest.setId(UUID.randomUUID().toString());
        returnRequest.setOrderId(orderId);
        returnRequest.setBuyerId(buyerId);
        returnRequest.setReason(reason);
        returnRequest.setReturnDate(returnDate != null ? returnDate : LocalDate.now());
        returnRequest.setReturnStatus(ReturnStatus.Pending);

        if (!order.getItems().isEmpty()) {
            Variant first = order.getItems().get(0);
            returnRequest.setProductId(first.getProductId());
            returnRequest.setQuantity(first.getQuantity());
        }

        return returnRepositoryPort.save(returnRequest);
    }
}
