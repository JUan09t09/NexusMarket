package application.domain.ports.in;

import application.domain.models.Return;
import java.time.LocalDate;

public interface RequestReturnUseCase {
    Return execute(String orderId, String buyerId, String reason, LocalDate returnDate);
}
