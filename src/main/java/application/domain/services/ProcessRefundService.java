package application.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidRefundAmountException;
import application.domain.exceptions.InvalidReturnStatusException;
import application.domain.exceptions.PaymentRejectedException;
import application.domain.models.Refund;
import application.domain.models.Return;
import application.domain.ports.in.ProcessRefundUseCase;
import application.domain.ports.out.PaymentGatewayPort;
import application.domain.ports.out.RefundRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.valueObjects.RefundStatus;
import application.domain.valueObjects.ReturnStatus;

@Service
@RequiredArgsConstructor

public class ProcessRefundService implements ProcessRefundUseCase {
    private final ReturnRepositoryPort returnRepositoryPort;
    private final RefundRepositoryPort refundRepositoryPort;
    private final PaymentGatewayPort paymentGatewayPort;

    @Override
    public Refund execute(String returnId, BigDecimal amount, LocalDate refundDate) {
        Return returnReference = new Return();
        returnReference.setId(returnId);

        Return returnRequest = returnRepositoryPort.findById(returnReference)
            .orElseThrow(() -> new InvalidReturnStatusException("No existe la devolucion con ID " + returnId + "."));

        if (returnRequest.getReturnStatus() != ReturnStatus.Approved) {
            throw new InvalidReturnStatusException("La devolucion no se encuentra aprobada.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRefundAmountException("El monto del reembolso es invalido.");
        }

        Refund refund = new Refund();
        refund.setId(UUID.randomUUID().toString());
        refund.setReturnId(returnId);
        refund.setRefundDate(refundDate != null ? refundDate : LocalDate.now());
        refund.setAmount(amount);
        refund.setStatus(RefundStatus.Processing);
        Refund savedRefund = refundRepositoryPort.save(refund);

        if (!paymentGatewayPort.refundPayment(savedRefund)) {
            throw new PaymentRejectedException("La transaccion de reembolso no fue confirmada.");
        }

        savedRefund.setStatus(RefundStatus.Succeded);
        return refundRepositoryPort.save(savedRefund);
    }
}
