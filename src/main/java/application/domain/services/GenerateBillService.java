package application.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.BillAlreadyExistsException;
import application.domain.exceptions.OrderNotPaidException;
import application.domain.exceptions.OrderNotFoundException;
import application.domain.models.Bill;
import application.domain.models.Order;
import application.domain.ports.in.GenerateBillUseCase;
import application.domain.ports.out.BillRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueObjects.OrderStatus;

@Service
@RequiredArgsConstructor

public class GenerateBillService implements GenerateBillUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final BillRepositoryPort billRepositoryPort;

    @Override
    public Bill execute(String orderId) {
        Order orderReference = new Order();
        orderReference.setId(orderId);

        Order order = orderRepositoryPort.findById(orderReference)
            .orElseThrow(() -> new OrderNotFoundException("No existe el pedido con ID " + orderId + "."));

        if (order.getOrderStatus() != OrderStatus.Paid) {
            throw new OrderNotPaidException("El pedido no se encuentra en estado pagado.");
        }

        Bill billReference = new Bill();
        billReference.setOrderId(orderId);
        if (billRepositoryPort.existsByOrder(billReference)) {
            throw new BillAlreadyExistsException("El pedido ya tiene una factura generada.");
        }

        BigDecimal total = order.getItems().stream()
            .map(item -> (item.getPrice() == null ? BigDecimal.ZERO : item.getPrice())
                .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Bill bill = new Bill();
        bill.setInvoiceId(UUID.randomUUID().toString());
        bill.setOrderId(orderId);
        bill.setInvoiceDate(LocalDate.now());
        bill.setTotalAmount(total);
        Bill savedBill = billRepositoryPort.save(bill);

        order.setBillId(savedBill.getInvoiceId());
        orderRepositoryPort.save(order);

        return savedBill;
    }
}
