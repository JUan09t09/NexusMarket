package application.domain.ports.in;

import application.domain.models.Refund;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProcessRefundUseCase {
    Refund execute(String returnId, BigDecimal amount, LocalDate refundDate);
}
