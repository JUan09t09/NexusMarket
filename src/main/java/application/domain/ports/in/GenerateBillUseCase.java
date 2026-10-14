package application.domain.ports.in;

import application.domain.models.Bill;

public interface GenerateBillUseCase {
    Bill execute(String orderId);
}
